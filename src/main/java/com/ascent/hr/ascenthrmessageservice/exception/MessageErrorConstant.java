package com.ascent.hr.ascenthrmessageservice.exception;

/**
 * @author gopal
 * @since 5/9/19
 */
public class MessageErrorConstant {

    public static final String MESSAGE_QUEUE_NAME_EMPTY = "Message queue name is either missing or empty";
    public static final String MESSAGE_DETAILS_EMPTY = "Message details are either missing or empty";
    public static final String MESSAGE_QUEUE_NOT_EXIST = "Message queue does not exist";
    public static final String MESSAGE_ID_NOT_PROVIDED = "Message id not provided";
    public static final String MESSAGE_NOT_PURGED = "Issue while purging message, please try again!";
    public static final String MESSAGE_NOT_DELETED = "Issue with message deletion, please try again!";
    public static final String MESSAGE_QUEUE_ALREADY_EXISTS = "Message queue with same name already exists";


}
