package com.ascent.hr.ascenthrmessageservice.rest;

import com.ascent.hr.ascenthrmessageservice.fixture.MessageQueueFixture;
import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.service.IMessageQueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




/**
 * @author gopal
 * @since 2/9/19
 */

@RunWith(SpringRunner.class)
@WebMvcTest
public class MessageQueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMessageQueueService iMessageQueueService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void enqueMessageToNewMessageQueue()throws Exception{


        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();

        when(iMessageQueueService.enQueue(anyString(), anyString(), anyString())).thenReturn(messageQueue);

        String messageJson = "{\"createdDate\":\"01/09/2019 04:30\",\"retryAttempts\":\"0\", \"deliveryStatus\":\"READY\", \"body\":\"test\"}";


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/messages/enqueue")
                .accept(MediaType.APPLICATION_JSON)
                .param("queueName", messageQueue.getName())
                .param("exchangeName", messageQueue.getExchangeName())
                //.param("message", "test")
                .content(messageJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        MessageQueue resultMessageQueue = objectMapper.readValue(response.getContentAsString(), MessageQueue.class);
        Assert.assertNotNull(resultMessageQueue);
        Assert.assertNotNull(resultMessageQueue.getId());

        Assert.assertEquals(messageQueue.getName(), resultMessageQueue.getName());
        Assert.assertEquals(messageQueue.getExchangeName(), resultMessageQueue.getExchangeName());
        Assert.assertEquals(1, resultMessageQueue.getSize());
        Assert.assertEquals(1, resultMessageQueue.getMessages().size());
        Assert.assertEquals("test", resultMessageQueue.getMessages().get(0).getString(resultMessageQueue.getMessages().get(0).getBody()));
        Assert.assertEquals(DeliveryStatus.READY, resultMessageQueue.getMessages().get(0).getDeliveryStatus());

    }




}
