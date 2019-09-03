package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.fixture.MessageFixture;
import com.ascent.hr.ascenthrmessageservice.fixture.MessageQueueFixture;
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
    public void enqueMessageToNewMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        String message = messageQueue.getMessages().get(0).getString(messageQueue.getMessages().get(0).getBody());
        messageQueue = iMessageQueueService.enQueue(messageQueue.getName(), messageQueue.getExchangeName(), message);
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(1, messageQueue.getSize());
    }

    @Test
    public void enqueMessageToExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        String message = messageQueue.getMessages().get(0).getString(messageQueue.getMessages().get(0).getBody());
        messageQueue = iMessageQueueService.enQueue(messageQueue.getName(), messageQueue.getExchangeName(), message);

        messageQueue.getMessages().add(MessageFixture.getMessage2());
        message = messageQueue.getMessages().get(1).getString(messageQueue.getMessages().get(1).getBody());
        messageQueue = iMessageQueueService.enQueue(messageQueue.getName(), messageQueue.getExchangeName(), message);

        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(2, messageQueue.getSize());
    }


}
