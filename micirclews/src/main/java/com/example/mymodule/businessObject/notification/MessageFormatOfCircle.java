package com.example.mymodule.businessObject.notification;

import com.google.appengine.repackaged.com.google.api.client.util.DateTime;

import java.sql.Timestamp;

/**
 * Created by kamaldua on 01/21/2016.
 */
public class MessageFormatOfCircle {
    public int msgId;
    public int crclId;
    public int fromId;
    public int msgType;
    public String message;
    public String img;
    public String imgThumb;
    public Timestamp msgOn;
}
