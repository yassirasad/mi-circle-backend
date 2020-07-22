package com.example.mymodule.businessObject.common;

/**
 * Created by kamaldua on 06/07/2016.
 */
public class NotificationType {
    public final static int PANIC = 1;
    public final static int PANIC_RELEASE = 2;
    public final static int PANIC_RESPOND = 3;

    //For Friends
    public final static int FRIEND_REQUEST_NEW = 4;
    public final static int FRIEND_REQUEST_ACCEPTED = 14;
    public final static int FRIEND_REMOVED = 5;

    public final static int FRIEND_LOCATION_ADD = 6;
    public final static int FRIEND_LOCATION_DELETE = 7;
    public final static int FRIEND_LOCATION_LEAVE = 16;

    //For Circles
    public final static int CIRCLE_DELETE = 8;
    public final static int CIRCLE_LOCATION_ADD = 9;
    public final static int CIRCLE_LOCATION_DELETE = 10;

    public final static int CIRCLE_MEMBER_ADD = 11;
    public final static int CIRCLE_MEMBER_DELETE = 12;
    public final static int CIRCLE_MEMBER_LEAVE = 15;

    //Locations
    public final static int LOCATION_CHECK_IN_OR_CHECK_OUT = 13;

    //Openfire
    public final static int OPENFIRE_CALLBACK_ON_OFFLINE = 20;
}
