package com.ascent.hr.ascenthrmessageservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gopal
 * @since 1/9/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageException extends RuntimeException{

    private Integer errorCode;

    public MessageException(String message, Integer errorCode){
        super(message);
        this.errorCode = errorCode;

    }

}
