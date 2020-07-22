package com.example.mymodule.micirclefileupload.businessObject.notification;

import com.example.mymodule.micirclefileupload.dataLayer.common.ApiResponse;

import java.sql.Timestamp;

/**
 * Created by kamaldua on 01/30/2016.
 */
public class MessageFormatOfFriend extends ApiResponse {
    public int msgId;
    public int fromId;
    public int toId;
    public int msgType;
    public String message;
    public String img;
    public String imgThumb;
    public Timestamp msgOn;
}
