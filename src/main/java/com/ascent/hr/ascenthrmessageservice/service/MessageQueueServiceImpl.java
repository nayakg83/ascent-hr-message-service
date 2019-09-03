package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Queue;

import java.util.List;

/**
 * @author gopal
 * @since 1/9/19
 */
@Service
public class MessageQueueServiceImpl implements IMessageQueueService {

    @Autowired
    private MessageQueueRepository messageQueueRepository;


    @Override
    public List<MessageQueue> findAll() {
        return messageQueueRepository.findAll();
    }

    @Override
    public MessageQueue updateQueueName(MessageQueue messageQueue, String queueName) {
        messageQueue.name(queueName);
        return messageQueueRepository.save(messageQueue);
    }

    @Override
    public boolean deleteQueue(MessageQueue messageQueue) {
        messageQueueRepository.delete(messageQueue);
        messageQueue = messageQueueRepository.findByName(messageQueue.getName());
        if(messageQueue == null)
            return true;
        return false;
    }


    @Override
    public MessageQueue enQueue(String queueName, String exchangeName, String inMessage) {

        MessageQueue messageQueue = messageQueueRepository.findByName(queueName);
        if (messageQueue == null) {
            messageQueue = new MessageQueue();
            messageQueue.name(queueName);
        }

        Message message = new Message();
        message.setCreatedDate(MessageUtil.getDate());

        message.setDeliveryStatus(DeliveryStatus.READY);
        message.setRetryAttempts(0);
        message.setBody(message.makeByteArray(inMessage));

        messageQueue.getMessages().add(message);
        MessageQueue messageQueue1 = messageQueueRepository.findByExchangeName(exchangeName);
        if(messageQueue1 == null)
            messageQueue.exchangeName(exchangeName);
        messageQueue.size();
        return messageQueueRepository.save(messageQueue);
    }

    @Override
    public MessageQueue deQueue(MessageQueue messageQueue) {

        Message message = messageQueue.getMessages().stream().findFirst().get();
        if(message != null) {
            messageQueue.getMessages().remove(message);
            messageQueue = messageQueueRepository.save(messageQueue);
        }
        return messageQueue;
    }

    @Override
    public boolean purge(MessageQueue messageQueue) {

        messageQueue.getMessages().clear();
        messageQueue = messageQueueRepository.save(messageQueue);
        if(messageQueue.getMessages().isEmpty())
            return true;
        return false;
    }

    @Override
    public Message peek(MessageQueue messageQueue, String messageId) {
        return messageQueue.getMessages().stream().filter(message -> message.getId().toString().equalsIgnoreCase(messageId)).findFirst().orElse(null);
    }
}
