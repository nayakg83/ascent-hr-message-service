package com.ascent.hr.ascenthrmessageservice.fixture;

import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;

/**
 * @author gopal
 * @since 2/9/19
 */
public class MessageQueueFixture {


    public static MessageQueue firstMessageQueue(){
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.name("test");
        Message message = MessageFixture.getMessage();
        messageQueue.getMessages().add(message);
        messageQueue.exchangeName("test");
        messageQueue.size();

        return messageQueue;
    }

    public static MessageQueue secondMessageQueue(){
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.name("test1");
        Message message = MessageFixture.getMessage1();
        messageQueue.getMessages().add(message);
        messageQueue.exchangeName("test1");
        messageQueue.size();

        return messageQueue;
    }



}
