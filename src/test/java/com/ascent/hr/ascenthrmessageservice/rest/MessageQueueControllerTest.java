package com.ascent.hr.ascenthrmessageservice.rest;

import com.ascent.hr.ascenthrmessageservice.fixture.MessageQueueFixture;
import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import com.ascent.hr.ascenthrmessageservice.service.IMessageQueueService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;




/**
 * @author gopal
 * @since 2/9/19
 */

@RunWith(SpringRunner.class)
@WebMvcTest(MessageQueueController.class)
public class MessageQueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMessageQueueService iMessageQueueService;

    @MockBean
    private MessageQueueRepository mockMessageQueueRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createMessageQueueTest() throws Exception{
        MessageQueue onlyQueue = MessageQueueFixture.firstQueue();

        when(iMessageQueueService.createQueue(anyString())).thenReturn(onlyQueue);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/messages/queues")
                .accept(MediaType.APPLICATION_JSON)
                .param("queueName", onlyQueue.getName())
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
    }


    @Test
    public void getMessageQueueTest() throws Exception{
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/messages/queues/{queueId}",messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        MessageQueue resultMessageQueue = this.objectMapper.readValue(response.getContentAsString(), MessageQueue.class);
        Assert.assertNotNull(resultMessageQueue);
        Assert.assertEquals(messageQueue.getId(), resultMessageQueue.getId());


    }


    @Test
    public void deleteMessageQueueTest() throws Exception{
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iMessageQueueService.deleteQueue(any())).thenReturn(true);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/messages/queues/{queueId}",messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        String resultResponse = response.getContentAsString();
        Assert.assertEquals(MessageResourceConstant.MESSAGE_QUEUE_DELETED, resultResponse);

    }

    @Test
    public void enqueMessageToNewMessageQueue()throws Exception{

        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iMessageQueueService.enQueue(any(), anyString())).thenReturn(messageQueue);

        String messageJson = "{\"content\":\"test\"}";
        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/messages/enqueue/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(messageJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        MessageQueue resultMessageQueue = objectMapper.readValue(response.getContentAsString(), MessageQueue.class);
        Assert.assertNotNull(resultMessageQueue);
        Assert.assertNotNull(resultMessageQueue.getId());

        Assert.assertEquals(messageQueue.getName(), resultMessageQueue.getName());
        Assert.assertEquals(1, resultMessageQueue.getSize());
        Assert.assertEquals(1, resultMessageQueue.getMessages().size());
        Assert.assertEquals("test", resultMessageQueue.getMessages().get(0).getString(resultMessageQueue.getMessages().get(0).getBody()));
        Assert.assertEquals(DeliveryStatus.READY, resultMessageQueue.getMessages().get(0).getDeliveryStatus());
    }


    @Test
    public void dequeueMessageFromExistingMessageQueue()throws Exception{

        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iMessageQueueService.deQueue(any())).thenReturn(messageQueue);

        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/messages/dequeue/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        MessageQueue resultMessageQueue = objectMapper.readValue(response.getContentAsString(), MessageQueue.class);
        Assert.assertNotNull(resultMessageQueue);
        Assert.assertNotNull(resultMessageQueue.getId());
        Assert.assertEquals(messageQueue.getName(), resultMessageQueue.getName());

    }


    @Test
    public void purgeMessagesFromExistingMessageQueue() throws Exception{

        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iMessageQueueService.purge(any())).thenReturn(true);

        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/messages/purge/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Messages purged!", response.getContentAsString());

    }


    @Test
    public void peekMessagesFromExistingMessageQueue() throws Exception{

        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        when(mockMessageQueueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iMessageQueueService.peek(any(), anyString())).thenReturn(messageQueue.getMessages().get(0));

        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/messages/peek/{queueId}", messageQueue.getId())
                .param("messageId",messageQueue.getMessages().get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Message message = this.objectMapper.readValue(response.getContentAsString(), Message.class);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals(messageQueue.getMessages().get(0).getId(), message.getId());

    }



}
