package com.example.mymodule.controller.notfication;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.util.ArrayList;

/**
 * Created by kamaldua on 06/04/2016.
 */
public final class PushNotification {

    public static void sendData(String fromId,  ArrayList<String> devicesList)
    {
        try {
            //Please add here your project API key: "Key for browser apps (with referers)".
            //If you added "API key Key for server apps (with IP locking)" or "Key for Android apps (with certificates)" here
            //then you may get error responses.
            Sender sender = new  Sender(fromId);

            // use this to send message with payload data
            Message message = new Message.Builder()
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
            System.out.println("Message Result: "+multicastResult.toString());//Print multicast message result on console

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
