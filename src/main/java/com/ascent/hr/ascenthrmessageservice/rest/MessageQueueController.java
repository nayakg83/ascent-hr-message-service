package com.ascent.hr.ascenthrmessageservice.rest;

import com.ascent.hr.ascenthrmessageservice.exception.MessageErrorCode;
import com.ascent.hr.ascenthrmessageservice.exception.MessageErrorConstant;
import com.ascent.hr.ascenthrmessageservice.exception.MessageException;
import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.service.IMessageQueueService;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author gopal
 * @since 1/9/19
 */
@RestController
@RequestMapping(value = "/messages")
public class MessageQueueController {


    @Autowired
    private IMessageQueueService iMessageQueueService;

    @GetMapping("/queues")
    public ResponseEntity<?> getMessageQueues(@RequestParam String queueName){
        if(MessageUtil.isNullOrEmpty(queueName))
            throw new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NAME_EMPTY, MessageErrorCode.MESSAGE_QUEUE_NAME_EMPTY);
        MessageQueue messageQueue = iMessageQueueService.getQueue(queueName);
        return ResponseEntity.ok(messageQueue);
    }


    /**
     * @Function responsible for getting message queue details
     * @param queueName
     * @return MessageQueue
     */
    @PostMapping("/queues")
    public ResponseEntity<?> createMessageQueue(@RequestParam String queueName){
        if(MessageUtil.isNullOrEmpty(queueName))
            throw new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NAME_EMPTY, MessageErrorCode.MESSAGE_QUEUE_NAME_EMPTY);
        MessageQueue messageQueue = iMessageQueueService.createQueue(queueName);
        return ResponseEntity.ok(messageQueue);
    }

    /**
     * @Function responsible for getting message queue details
     * @param messageQueue
     * @return
     */
    @GetMapping("/queues/{queueId}")
    public ResponseEntity<?> getMessageQueue(@PathVariable(value = "queueId") MessageQueue messageQueue){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        return ResponseEntity.ok(messageQueue);
    }

    /**
     * @Function responsible to update existing queue name
     * @param messageQueue
     * @param queueName
     * @return
     */
    @PutMapping("/queues/{queueId}")
    public ResponseEntity<?> updateMessageQueue(@PathVariable(value = "queueId") MessageQueue messageQueue, @RequestParam String queueName){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        messageQueue = iMessageQueueService.updateQueueName(messageQueue, queueName);
        return ResponseEntity.ok(messageQueue);
    }

    /**
     * @Function responsible to remove message queue
     * @param messageQueue
     * @return
     */
    @DeleteMapping("/queues/{queueId}")
    public ResponseEntity<?> deleteMessageQueue(@PathVariable(value = "queueId") MessageQueue messageQueue){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        boolean isDeleted = iMessageQueueService.deleteQueue(messageQueue);
        if(isDeleted)
            return ResponseEntity.ok(MessageResourceConstant.MESSAGE_QUEUE_DELETED);
        else
            throw new MessageException(MessageErrorConstant.MESSAGE_NOT_PURGED, MessageErrorCode.MESSAGE_NOT_DELETED);
    }


    /**
     * @Function responsible for message queue creation along with enqueque message
     * @param messageQueue
     * @param message
     * @return messageQueue
     */
    @PostMapping(value = "/enqueue/{queueId}")
    public ResponseEntity<?> enqueueMessage(@PathVariable(value = "queueId") MessageQueue messageQueue, @RequestBody Message message){
        if(MessageUtil.isNullOrEmpty(messageQueue))
            throw new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        if(MessageUtil.isNullOrEmpty(message))
            throw  new MessageException(MessageErrorConstant.MESSAGE_DETAILS_EMPTY, MessageErrorCode.MESSAGE_DETAILS_EMPTY);
        messageQueue = iMessageQueueService.enQueue(messageQueue, message.getContent());
        return ResponseEntity.ok(messageQueue);
    }

    /**
     * @Function responsible for dequeue message from message queue
     * @param messageQueue
     * @return
     */
    @PutMapping(value = "/dequeue/{queueId}")
    public ResponseEntity<?> dequeueMessage(@PathVariable(value = "queueId") MessageQueue messageQueue){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        messageQueue = iMessageQueueService.deQueue(messageQueue);
        return ResponseEntity.ok(messageQueue);
    }

    /**
     * @Function responsible for purging all messages from message queue
     * @param messageQueue
     * @return
     */
    @PutMapping(value = "/purge/{queueId}")
    public ResponseEntity<?> purgeMessages(@PathVariable(value = "queueId") MessageQueue messageQueue){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);

        boolean isPurged = iMessageQueueService.purge(messageQueue);
        if(isPurged)
            return ResponseEntity.ok(MessageResourceConstant.MESSAGE_PURGED);
        else
            throw new MessageException(MessageErrorConstant.MESSAGE_NOT_PURGED, MessageErrorCode.MESSAGE_NOT_PURGED);
    }

    /**
     * @Function responsible for getting all messages of particular message queue
     * @param messageQueue
     * @param messageId
     * @return
     */
    @GetMapping(value = "/peek/{queueId}")
    public ResponseEntity<?> peekMessages(@PathVariable(value = "queueId") MessageQueue messageQueue, @RequestParam String messageId){
        if(messageQueue == null)
            throw  new MessageException(MessageErrorConstant.MESSAGE_QUEUE_NOT_EXIST, MessageErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        if(MessageUtil.isNullOrEmpty(messageId))
            throw  new MessageException(MessageErrorConstant.MESSAGE_ID_NOT_PROVIDED, MessageErrorCode.MESSAGE_ID_NOT_PROVIDED);

        com.ascent.hr.ascenthrmessageservice.model.Message message = iMessageQueueService.peek(messageQueue, messageId);

        return ResponseEntity.ok(message);
    }




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message{
        private String content;
    }


}
