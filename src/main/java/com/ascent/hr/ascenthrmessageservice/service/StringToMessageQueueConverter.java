package com.ascent.hr.ascenthrmessageservice.service;

import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import com.ascent.hr.ascenthrmessageservice.repository.MessageQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @author gopal
 * @since 8/9/19
 */

@Component
public class StringToMessageQueueConverter implements Converter<String, MessageQueue> {

    @Autowired
    private MessageQueueRepository messageQueueRepository;


    @Override
    public MessageQueue convert(final String source) {
        return messageQueueRepository.findById(source).get();
    }


}
