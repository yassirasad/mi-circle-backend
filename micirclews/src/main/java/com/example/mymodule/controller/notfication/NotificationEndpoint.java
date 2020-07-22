package com.example.mymodule.controller.notfication;

import com.example.mymodule.businessObject.circle.CircleGroupList;
import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.friend.GetFriendResponse;
import com.example.mymodule.businessObject.notification.GetNotificationResponse;
import com.example.mymodule.businessObject.notification.MessageGetRequest;
import com.example.mymodule.businessObject.notification.MessageResponse;
import com.example.mymodule.businessObject.notification.Notification;
import com.example.mymodule.businessObject.notification.NotificationCircleRequest;
import com.example.mymodule.businessObject.notification.NotificationFriendRequest;
import com.example.mymodule.businessObject.notification.NotificationSection;
import com.example.mymodule.datalayer.notification.DLNotification;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;

/**
 * Created by kamaldua on 09/22/2015.
 */
@Api(name = "notificationApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))

public class NotificationEndpoint {

    @ApiMethod(name = "getNotifications", path = "getNotifications", httpMethod = ApiMethod.HttpMethod.POST)
    public GetNotificationResponse getNotifications(DeviceSessionEntity req) {

        return DLNotification.getNotifications(req);
    }

    @ApiMethod(name = "notificationResponse", path = "notificationResponse", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse notificationResponse(Notification req) {

        return DLNotification.notificationResponse(req);
    }

    @ApiMethod(name = "getAllNotifications", path = "getAllNotifications", httpMethod = ApiMethod.HttpMethod.POST)
    public NotificationSection getAllNotifications(DeviceSessionEntity req)
    {
        return DLNotification.getAllNotifications(req);
    }

    @ApiMethod(name = "setLastActiveStatus", path = "setLastActiveStatus", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse setLastActiveStatus(DeviceSessionEntity req)
    {
        return DLNotification.setLastActiveStatus(req);
    }

    @ApiMethod(name = "notificationFriendAddResponse", path = "notificationFriendAddResponse", httpMethod = ApiMethod.HttpMethod.POST)
    public GetFriendResponse notificationFriendAddResponse(NotificationFriendRequest req)
    {
        return DLNotification.notificationFriendAddResponse(req);
    }

    @ApiMethod(name = "notificationCircleAddResponse", path = "notificationCircleAddResponse", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList notificationCircleAddResponse(NotificationCircleRequest req)
    {
        return DLNotification.notificationCircleAddResponse(req);
    }

    @ApiMethod(name = "getMessages", path = "getMessages", httpMethod = ApiMethod.HttpMethod.POST)
    public MessageResponse getMessages(MessageGetRequest req)
    {
        return DLNotification.getMessages(req);
    }

    @ApiMethod(name = "test", path = "test", httpMethod = ApiMethod.HttpMethod.GET)
    public ApiResponse test()
    {
        ApiResponse res = new ApiResponse();
        try {
            CustomHttpRequest.sendGet("https://mi-circle-image-upload.appspot.com/imguploadpath", "");
        }catch (IOException ex)
        {

        }
        return res;
        //return DLNotification.getMessages(req);
    }

    @ApiMethod(name = "testPath", path = "testPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse testPath()
    {
        ApiResponse res = new ApiResponse();
        try {
            CustomHttpRequest.sendGet("https://mi-circle-image-upload.appspot.com/imguploadpath", "");
        }catch (IOException ex)
        {

        }
        return res;
        //return DLNotification.getMessages(req);
    }
}
