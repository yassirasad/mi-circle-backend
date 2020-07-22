package com.example.mymodule.businessObject.notification;

/**
 * Created by kamaldua on 01/21/2016.
 */
public class MessageSendRequest {
    public String deviceSession;
    public int receiverId; // FriendUserID or CircleID
    public int messageFor; // 1 for Friend, 2 for Circle
    public int MessageType; // 1 Text, 2 Media
    public String message;
}
