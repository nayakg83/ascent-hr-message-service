package com.ascent.hr.ascenthrmessageservice.fixture;

import com.ascent.hr.ascenthrmessageservice.model.DeliveryStatus;
import com.ascent.hr.ascenthrmessageservice.model.Message;
import com.ascent.hr.ascenthrmessageservice.util.MessageUtil;

/**
 * @author gopal
 * @since 2/9/19
 */
public class MessageFixture {

    public static Message getMessage(){
        Message message = new Message();
        message = getCommonProperty(message);
        message.setBody("test".getBytes());
        return message;
    }

    public static Message getMessage1(){
        Message message = new Message();
        message = getCommonProperty(message);
        message.setBody("test1".getBytes());
        return message;
    }

    public static Message getMessage2(){
        Message message = new Message();
        message = getCommonProperty(message);
        message.setBody("test2".getBytes());
        return message;
    }


    public static Message getCommonProperty(Message message){
        message.setDeliveryStatus(DeliveryStatus.READY);
        message.setRetryAttempts(0);
        message.setCreatedDate(MessageUtil.getDate());
        return  message;
    }

}
