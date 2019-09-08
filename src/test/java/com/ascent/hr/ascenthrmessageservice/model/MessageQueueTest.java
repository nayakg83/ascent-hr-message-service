package com.ascent.hr.ascenthrmessageservice.model;

import com.ascent.hr.ascenthrmessageservice.fixture.MessageQueueFixture;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author gopal
 * @since 2/9/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class MessageQueueTest {

    @Autowired
    private MessageQueueRepository messageQueueRepository;

    @Before
    public void cleanup(){
        messageQueueRepository.deleteAll();
    }


    @Test
    public void createMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        messageQueueRepository.save(messageQueue);
        messageQueue = MessageQueueFixture.secondMessageQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        List<MessageQueue> messageQueues = messageQueueRepository.findAll();
        Assert.assertNotNull(messageQueues);
        Assert.assertEquals(2, messageQueues.size());
    }

    @Test
    public void addNewMessageToExistingMessageQueue(){
        MessageQueue messageQueue = MessageQueueFixture.firstMessageQueue();
        messageQueue = messageQueueRepository.save(messageQueue);

        Message msg = new Message();
        msg.setCreatedDate(MessageUtil.getDate());
        msg.setRetryAttempts(0);
        msg.setDeliveryStatus(DeliveryStatus.READY);
        msg.setBody("test1".getBytes());

        messageQueue.getMessages().add(msg);
        messageQueue.size();

        messageQueueRepository.save(messageQueue);
        List<MessageQueue> messageQueues = messageQueueRepository.findAll();
        Assert.assertNotNull(messageQueues);
        Assert.assertEquals(1, messageQueues.size());
        Assert.assertEquals(2, messageQueues.get(0).getMessages().size());
    }



}
