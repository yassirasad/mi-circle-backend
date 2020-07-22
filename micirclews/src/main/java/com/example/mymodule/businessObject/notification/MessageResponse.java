package com.example.mymodule.businessObject.notification;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.List;

/**
 * Created by kamaldua on 01/21/2016.
 */
public class MessageResponse extends ApiResponse {
    public List<MessageFormatOfFriend> frndMessages;
    public List<MessageFormatOfCircle> crclMessages;
}
