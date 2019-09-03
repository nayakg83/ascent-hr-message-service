package com.ascent.hr.ascenthrmessageservice.repository;

import com.ascent.hr.ascenthrmessageservice.model.MessageQueue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gopal
 * @since 2/9/19
 */
public interface MessageQueueRepository extends JpaRepository<MessageQueue, String> {

    @Override
    MessageQueue save(MessageQueue messageQueue);

    MessageQueue findByName(String name);

    MessageQueue findByExchangeName(String name);
}
