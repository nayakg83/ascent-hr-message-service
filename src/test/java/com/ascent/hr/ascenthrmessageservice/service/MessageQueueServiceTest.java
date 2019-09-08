package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.fixture.MessageFixture;
import com.ascent.hr.ascenthrmessageservice.fixture.MessageQueueFixture;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import com.ascent.hr.ascenthrmessageservice.service.IMessageQueueService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gopal
 * @since 2/9/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessageQueueServiceTest {


    @Autowired
    private MessageQueueRepository messageQueueRepository;

    @Autowired
    private IMessageQueueService iMessageQueueService;

    @Before
    public void cleanup(){
        messageQueueRepository.deleteAll();
    }


    @Test
    public void createMessageQueueTest(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = iMessageQueueService.createQueue(messageQueue.getName());
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(0, messageQueue.getSize());
    }


    @Test
    public void updateMessageQueueNameTest(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iMessageQueueService.updateQueueName(messageQueue, "test1");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(0, messageQueue.getSize());
        Assert.assertEquals("test1", messageQueue.getName());
    }

    @Test
    public void DeleteMessageQueueTest(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        boolean isDeleted = iMessageQueueService.deleteQueue(messageQueue);
        Assert.assertTrue(isDeleted);
    }

    @Test
    public void enqueMessageToExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        String message = "test";
        messageQueue = iMessageQueueService.enQueue(messageQueue, message);
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(1, messageQueue.getSize());
    }

    @Test
    public void enqueMessageToExistingMessageQueue1(){
        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test1");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(2, messageQueue.getSize());
    }

    @Test
    public void dequeMessageFromExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test1");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(3, messageQueue.getSize());
        messageQueue = iMessageQueueService.deQueue(messageQueue);
        Assert.assertEquals(2, messageQueue.getSize());
        Assert.assertEquals("test1", messageQueue.getMessages().get(0).getString(messageQueue.getMessages().get(0).getBody()));
        Assert.assertEquals("test1", messageQueue.getMessages().get(0).getString(messageQueue.getMessages().get(0).getBody()));

    }


    @Test
    public void purgeMessagesFromExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test1");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(3, messageQueue.getSize());
        boolean isPurged = iMessageQueueService.purge(messageQueue);
        Assert.assertEquals(0, messageQueue.getSize());
        Assert.assertTrue(isPurged);
    }

    @Test
    public void pollFirstMessageFromExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test1");
        messageQueue = iMessageQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(3, messageQueue.getSize());
        Message message = iMessageQueueService.peek(messageQueue, messageQueue.getMessages().get(0).getId());
        Assert.assertEquals(3, messageQueue.getSize());
        Assert.assertNotNull(message);
        Assert.assertEquals("test", message.getString(message.getBody()));
    }

}
