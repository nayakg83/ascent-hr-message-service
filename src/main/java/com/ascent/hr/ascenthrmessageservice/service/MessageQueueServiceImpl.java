package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.exception.MessageErrorCode;
import com.ascent.hr.ascenthrmessageservice.exception.MessageErrorConstant;
import com.ascent.hr.ascenthrmessageservice.exception.MessageException;
import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gopal
 * @since 1/9/19
 */
@Service
@Slf4j
public class MessageQueueServiceImpl implements IMessageQueueService {

    @Autowired
    private MessageQueueRepository messageQueueRepository;

    @Override
    public MessageQueue getQueue(String queueName) {
        return  messageQueueRepository.findByName(queueName);
    }

    @Override
    public MessageQueue createQueue(String queueName) {

        MessageQueue messageQueue = messageQueueRepository.findByName(queueName);
        if (messageQueue == null) {
            messageQueue = new MessageQueue();
            messageQueue.name(queueName);
            return messageQueueRepository.save(messageQueue);
        }else{
            log.info("can not create message queue as the queue with same name already exists");
            throw new MessageException(MessageErrorConstant.MESSAGE_QUEUE_ALREADY_EXISTS, MessageErrorCode.MESSAGE_QUEUE_ALREADY_EXISTS);
        }

    }

    @Override
    public MessageQueue updateQueueName(MessageQueue messageQueue, String queueName) {
        messageQueue.name(queueName);
        return messageQueueRepository.save(messageQueue);
    }

    @Override
    public boolean deleteQueue(MessageQueue messageQueue) {
        String queueName = messageQueue.getName();
        messageQueueRepository.delete(messageQueue);
        messageQueue = messageQueueRepository.findByName(messageQueue.getName());
        if(messageQueue == null) {
            log.info("successfully deleted the message queue" + queueName);
            return true;
        }
        log.info("Issue while deleting the message queue" + queueName);
        return false;
    }


    @Override
    public MessageQueue enQueue(MessageQueue messageQueue, String inMessage) {

        Message message = new Message();
        message.setCreatedDate(MessageUtil.getDate());

        message.setDeliveryStatus(DeliveryStatus.READY);
        message.setRetryAttempts(0);
        message.setBody(message.makeByteArray(inMessage));

        messageQueue.getMessages().add(message);
        messageQueue.size();
        return messageQueueRepository.save(messageQueue);
    }

    @Override
    public MessageQueue deQueue(MessageQueue messageQueue) {

        Message message = messageQueue.getMessages().stream().findFirst().get();
        if(message != null) {
            messageQueue.getMessages().remove(message);
            messageQueue.size();
            messageQueue = messageQueueRepository.save(messageQueue);
            log.info("Successfully deque the first message with id" +message.getId()+"from the the message queue" + messageQueue.getName());
        }
        return messageQueue;
    }

    @Override
    public boolean purge(MessageQueue messageQueue) {

        messageQueue.getMessages().clear();
        messageQueue.size();
        messageQueue = messageQueueRepository.save(messageQueue);
        if(messageQueue.getMessages().isEmpty()) {
            log.info("Successfully purged all the messages from message queue with name" + messageQueue.getName());
            return true;
        }
        log.info("Issue while purging all the messages from message queue with name" + messageQueue.getName());
        return false;
    }

    @Override
    public Message peek(MessageQueue messageQueue, String messageId) {
        return messageQueue.getMessages().stream().filter(message -> message.getId().toString().equalsIgnoreCase(messageId)).findFirst().orElse(null);
    }
}
