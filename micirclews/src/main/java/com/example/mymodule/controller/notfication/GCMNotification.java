package com.example.mymodule.controller.notfication;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.common.base.Strings;

import java.io.IOException;

/**
 * Created by kamaldua on 01/01/2016.
 */
public class GCMNotification {

    public static boolean sendMessage()
    {
        final String GCM_API_KEY = "yourKey";
        final int retries = 3;
        final String notificationToken = "deviceNotificationToken";
        Sender sender = new Sender(GCM_API_KEY);
        Message msg = new Message.Builder().build();

        try {
            Result result = sender.send(msg, notificationToken, retries);

            if (!isNotNullNotEmptyNotWhiteSpaceOnlyByJava(result.getErrorCodeName())) {
                //logger.debug("GCM Notification is sent successfully");
                return true;
            }

            //logger.error("Error occurred while sending push notification :" + result.getErrorCodeName());
        } catch (InvalidRequestException e) {
            //logger.error("Invalid Request", e);
        } catch (IOException e) {
            //logger.error("IO Exception", e);
        }
        return false;
    }
    public static boolean isNotNullNotEmptyNotWhiteSpaceOnlyByJava(final String string)
    {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }
}
