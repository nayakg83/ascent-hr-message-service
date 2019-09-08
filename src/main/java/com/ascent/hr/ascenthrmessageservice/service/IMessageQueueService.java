package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;

import java.util.List;

/**
 * @author gopal
 * @since 1/9/19
 */
public interface IMessageQueueService {

    public MessageQueue createQueue(String queueName);

    public MessageQueue updateQueueName(MessageQueue messageQueue, String queueName);

    public boolean deleteQueue(MessageQueue messageQueue);

    public MessageQueue getQueue(String queueName);

    public MessageQueue enQueue(MessageQueue messageQueue, String message);

    public MessageQueue deQueue(MessageQueue messageQueue);

    public boolean purge(MessageQueue messageQueue);

    public Message peek(MessageQueue messageQueue, String messageId);
}
