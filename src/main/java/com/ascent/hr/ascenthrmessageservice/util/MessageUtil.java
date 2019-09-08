package com.ascent.hr.ascenthrmessageservice.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author gopal
 * @since 1/9/19
 */
public class MessageUtil {

    public static boolean isNullOrEmpty(Object object){
        if(object == null) {
            return true;
        }else if(object.toString().length() == 0) {
            return true;
        }
        return false;
    }


    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return simpleDateFormat.format(Calendar.getInstance().getTime());

    }

    public static String getString(byte[] message){
        return  new String(message);
    }

}
