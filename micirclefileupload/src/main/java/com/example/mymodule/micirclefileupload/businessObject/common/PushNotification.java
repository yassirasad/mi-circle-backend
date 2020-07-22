package com.example.mymodule.micirclefileupload.businessObject.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Created by kamaldua on 06/04/2016.
 */
public final class PushNotification {

    public static void sendData(String apiKey,  ArrayList<String> devicesList)
    {
        String gcmMessage = "";
        Message message = new Message.Builder().build();
        try {
            //Please add here your project API key: "Key for browser apps (with referers)".
            //If you added "API key Key for server apps (with IP locking)" or "Key for Android apps (with certificates)" here
            //then you may get error responses.
            Sender sender = new  Sender(apiKey);

            // use this to send message with payload data
            message = new Message.Builder()
                    .collapseKey("message")
                    .timeToLive(3)
                    .delayWhileIdle(true)
                    .addData("message", "Welcome to Push Notifications") //you can get this message on client side app
                    .build();

            //Use this code to send notification message to a single device
            /*Result result = sender.send(message,
                    "APA91bEbKqwTbvvRuc24vAYljcrhslOw-jXBqozgH8C2OB3H8R7U00NbIf1xp151ptweX9VkZXyHMik022cNrEETm7eM0Z2JnFksWEw1niJ2sQfU3BjQGiGMq8KsaQ7E0jpz8YKJNbzkTYotLfmertE3K7RsJ1_hAA",
                    1);
            System.out.println("Message Result: "+result.toString()); //Print message result on console
            */

            //Use this code to send notification message to multiple devices
           /* ArrayList<String> devicesList = new ArrayList<String>();
            //add your devices RegisterationID, one for each device
            devicesList.add("APA91bEbKqwTbvvRuc24vAYljcrhslOw-jXBqozgH8C2OB3H8R7U00NbIf1xp151ptweX9VkZXyHMik022cNrEETm7eM0Z2JnFksWEw1niJ2sQfU3BjQGiGMq8KsaQ7E0jpz8YKJNbzkTYotLfmertE3K7RsJ1_hAA");
            devicesList.add("APA91bEVcqKmPnESzgnGpEstHHymcpOwv52THv6u6u2Rl-PaMI4mU3Wkb9bZtuHp4NLs4snBl7aXXVkNn-IPEInGO2jEBnBI_oKEdrEoTo9BpY0i6a0QHeq8LDZd_XRzGRSv_R0rjzzZ1b6jXY60QqAI4P3PL79hMg");
            */
            //Use this code for multicast messages
            MulticastResult multicastResult = sender.send(message, devicesList, 0);
            gcmMessage = "Message Result: "+multicastResult.toString();
            System.out.println("Message Result: "+multicastResult.toString());//Print multicast message result on console

        } catch (Exception e) {
            gcmMessage = e.getMessage();
            //e.printStackTrace();
        }

        try
        {
            Connection conn = ConnectionManager.getConnection();

            String log = StoreProcedures.UserProfile.GCM_LOG;
            CallableStatement cs = conn.prepareCall(log);
            cs.setString(1, message.toString());
            cs.setString(2, gcmMessage.toString());

            cs.executeQuery();

        } catch (Exception e) {
        }

    }
}
