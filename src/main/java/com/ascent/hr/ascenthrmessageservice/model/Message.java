package com.ascent.hr.ascenthrmessageservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author gopal
 * @since 1/9/19
 */

@Getter
@Entity
public class Message implements Serializable {

    @Id
    @Column(length = 64)
    private String id = UUID.randomUUID().toString();

    @Setter
    private String createdDate;

    @Setter
    private int retryAttempts;

    @Setter
    private DeliveryStatus deliveryStatus;

    @Setter
    private byte[] body;

    public String getString(byte[] payload){
        return new String(payload);
    }

    public byte[] makeByteArray(String payload){
        return payload.getBytes();
    }

    public int messageLength(String payload){
        return payload.getBytes().length;
    }

}
