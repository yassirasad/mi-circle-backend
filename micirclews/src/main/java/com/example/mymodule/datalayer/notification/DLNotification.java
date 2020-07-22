package com.example.mymodule.datalayer.notification;

import com.example.mymodule.businessObject.circle.CircleGroup;
import com.example.mymodule.businessObject.circle.CircleGroupList;
import com.example.mymodule.businessObject.circle.CircleLocation;
import com.example.mymodule.businessObject.circle.CircleMember;
import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.businessObject.friend.FriendData;
import com.example.mymodule.businessObject.friend.GetFriendResponse;
import com.example.mymodule.businessObject.notification.GetNotification;
import com.example.mymodule.businessObject.notification.GetNotificationResponse;
import com.example.mymodule.businessObject.notification.MessageFormatOfCircle;
import com.example.mymodule.businessObject.notification.MessageFormatOfFriend;
import com.example.mymodule.businessObject.notification.MessageGetRequest;
import com.example.mymodule.businessObject.notification.MessageResponse;
import com.example.mymodule.businessObject.notification.Notification;
import com.example.mymodule.businessObject.notification.NotificationCircleRequest;
import com.example.mymodule.businessObject.notification.NotificationFriendRequest;
import com.example.mymodule.businessObject.notification.NotificationSection;
import com.example.mymodule.controller.openfireService.restAPI;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by kamaldua on 09/22/2015.
 */
public class DLNotification {

    public static GetNotificationResponse getNotifications(DeviceSessionEntity req)
    {
        GetNotificationResponse res = new GetNotificationResponse();
        res.notifications = new ArrayList<GetNotification>();
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.NOTIFICATION_GET);
            cs.setString(1,req.deviceSession.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                //LocationID, Name, Latitude, Longitude, Radius
                GetNotification notification = new GetNotification();
                notification.appNotificationID = rs.getInt(1);
                notification.notificationType = rs.getInt(2);
                notification.message = rs.getString(3);
                notification.validationText = rs.getString(4);
                notification.imageUrl = rs.getString(5).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                        : rs.getString(5).trim();

                res.notifications.add(notification);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse notificationResponse(Notification req)
    {
        ApiResponse res = new ApiResponse();
        ArrayList<String> deviceList = new ArrayList();     // @author Yassir
        String gcmId = null;      // @author Yassir

        try
        {
            Connection conn = ConnectionManager.getConnection();

            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.NOTIFICATION_RESPONSE);
            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2,req.notificationType);
            cs.setInt(3,req.appNotificationID);
            cs.setInt(4,req.acceptanceStatus);

            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.registerOutParameter(7, java.sql.Types.VARCHAR);         // @author Yassir

            ResultSet rs = cs.executeQuery();

            // @author Yassir
            while (rs.next()) {
                gcmId = rs.getString(1);
            }

            String contactForUser = cs.getString(5);
            String contactForFriend = cs.getString(6);
            String fullNameOfUser = cs.getString(7);        // @author Yassir

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

            if(res.status == 1 && req.notificationType == 1 && req.acceptanceStatus == 0 && contactForUser.length() > 0)
            {
                try {
                    restAPI.addNewFriend(contactForUser,contactForFriend);
                }
                catch (Exception exx){

                }

                // @author Yassir
                if(gcmId.length() > 0)
                {
                    deviceList.add(gcmId);
                    JSONObject data = new JSONObject();
                    String message = fullNameOfUser + " ("+contactForUser+") has accepted your friend request.";
                    data.put("message", message);
                    GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_REQUEST_ACCEPTED, deviceList, data);
                }

            }
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static NotificationSection getAllNotifications(DeviceSessionEntity req)
    {
        NotificationSection res = new NotificationSection();

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.NOTIFICATION_GET_ALL);
            cs.setString(1,req.deviceSession.trim());

            boolean results = cs.execute();
            int rsCount = 0;

            //Loop through the available result sets.
            while (results) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    if(rsCount == 0) //get all circles
                    {
                        res.lastUpdatedTime = rs.getTimestamp(1);
                    }
                    else if(rsCount == 1) //get friend messages ids
                    {
                        res.setFrndMessages(rs.getInt(1)) ;
                    }
                    else if(rsCount == 2) //get circle messages ids
                    {
                        res.setCrclMessages(rs.getInt(1));
                    }
                    else if(rsCount == 3) //new friend requests
                    {
                        NotificationSection.FriendRequest frReq = res.getFriendRequestObj();
                        //fr.FriendRequestID , fr.UserID 'FriendUserID', u.CountryCode, u.ContactNo, IFNULL(u.FirstName,'') FirstName, IFNULL(u.ImageUrl,'') ImageUrl
                        frReq.fReqID = rs.getInt(1);
                        frReq.fUserID = rs.getInt(2);
                        frReq.cyCode = rs.getString(3);
                        frReq.contNo = rs.getString(4);
                        frReq.name = rs.getString(5);
                        String imageUrl = rs.getString(6).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(6))
                                : rs.getString(6).trim();
                        frReq.image = imageUrl;

                        res.newFrsReq.add(frReq);
                    }
                    else if(rsCount == 4) //someone respond his friend request
                    {
                        //fr.FriendRequestID, fr.FriendUserID, u.CountryCode, u.ContactNo, IFNULL(u.FirstName,'') FirstName, IFNULL(u.ImageUrl,'') ImageUrl,status
                        NotificationSection.FriendRequest frReq = res.getFriendRequestObj();

                        frReq.fReqID = rs.getInt(1);
                        frReq.fUserID = rs.getInt(2);
                        frReq.cyCode = rs.getString(3);
                        frReq.contNo = rs.getString(4);
                        frReq.name = rs.getString(5);
                        String imageUrl = rs.getString(6).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(6))
                                : rs.getString(6).trim();
                        frReq.image = imageUrl;
                        frReq.status = rs.getString(7);

                        res.resFrsReq.add(frReq);
                    }
                    else if(rsCount == 5) //someone respond his friend request
                    {
                        res.setDelFriend(rs.getInt(1));
                    }
                    else if(rsCount == 6) //-- 4. Friends Image Changed
                    {
                        String thumb = rs.getString(2).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(2))
                                : rs.getString(2).trim();
                        String img = rs.getString(2).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(2))
                                : rs.getString(2).trim();

                        res.addFriendImage(rs.getInt(1),thumb,img);
                    }
                    else if(rsCount == 7) //-- 1. Someone send add into Circle request
                    {
                        int reqID = rs.getInt(1);
                        String cCode = rs.getString(2);
                        String contNo = rs.getString(3);
                        String thumb = rs.getString(4).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(4))
                                : rs.getString(4).trim();
                        String img = rs.getString(4).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(4))
                                : rs.getString(4).trim();
                        String cName = rs.getString(5);

                        res.addCircleRequest(reqID, cCode, contNo, thumb, img, cName);
                   }
                    else if(rsCount == 8) //-- 2. member respond to Circle add request
                    {
                        int crcID = rs.getInt(1);
                        int userID = rs.getInt(2);
                        String cCode = rs.getString(3);
                        String conNo = rs.getString(4);

                        String thumb = rs.getString(5).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                : rs.getString(5).trim();
                        String img = rs.getString(5).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(5))
                                : rs.getString(5).trim();

                        boolean isAdmin = rs.getBoolean(6);

                        res.addNewMemberInCircle(crcID, userID, cCode, conNo, thumb, img, isAdmin);
                    }
                    else if(rsCount == 9) //-- 3. Circle member deleted
                    {
                        int crcID = rs.getInt(1);
                        int userID = rs.getInt(2);

                        res.delMemberInCircle(crcID,userID);
                    }
                    else if(rsCount == 10) //-- 4. Circle deleted by Admin
                    {
                        res.setDeletedCircle(rs.getInt(1));
                    }
                    else if(rsCount == 11) //-- 5. Circles Image changed
                    {
                        int crcID = rs.getInt(1);
                        String thumb = rs.getString(2).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(2))
                                : rs.getString(2).trim();
                        String img = rs.getString(2).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(2))
                                : rs.getString(2).trim();

                        res.addCircleImage(crcID, thumb, img);
                    }
                    else if(rsCount == 12) //-- 6. Circle Member image changed
                    {
                        int crcID = rs.getInt(1);
                        int userID = rs.getInt(2);
                        String thumb = rs.getString(3).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(3))
                                : rs.getString(3).trim();
                        String img = rs.getString(3).trim().length() > 0
                                ? (Constants.IMAGE_URL + rs.getString(3))
                                : rs.getString(3).trim();

                        res.addCircleMemberImageChange(crcID,userID,thumb,img);
                    }

                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            int length = res.getFrndMessages().size();
            res.frMessIds = new int[length];

            for(int i = 0; i < length; i++)
            {
                res.frMessIds[i] = res.getFrndMessages().get(i).msgId;
            }

            length = res.getCrclMessages().size();
            res.cleMessIds = new int[length];

            for(int i = 0; i < length; i++)
            {
                res.cleMessIds[i] = res.getCrclMessages().get(i).msgId;
            }

            length = res.getDelFriend().size();
            res.deleteYouFromFriendList = new int[length];

            for(int i = 0; i < length; i++)
            {
                res.deleteYouFromFriendList[i] = res.getDelFriend().get(i).frId;
            }

            length = res.getDelCircles().size();
            res.deletedCircles = new int[length];

            for(int i = 0; i < length; i++)
            {
                res.deletedCircles[i] = res.getDelCircles().get(i).cId;
            }
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse setLastActiveStatus(DeviceSessionEntity req)
    {
        ApiResponse res = new ApiResponse();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LAST_ACTIVE_STATUS_UPDATE);
            cs.setString(1, req.deviceSession.trim());

            cs.executeQuery();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static GetFriendResponse notificationFriendAddResponse(NotificationFriendRequest req)
    {
        GetFriendResponse res = new GetFriendResponse();
        res.friends = new ArrayList<FriendData>();
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.NOTIFICATION_FRIEND_RESPONSE);
            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2, req.friendRequestID);
            cs.setInt(3, req.acceptanceStatus);

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                FriendData fd = new FriendData();
                fd.countryCode = rs.getString(1);
                fd.contactNo = rs.getString(2);
                fd.firstName = rs.getString(3);
                fd.imageUrl =
                rs.getString(4).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(4))
                        : rs.getString(4).trim();
                fd.FriendUserID = rs.getInt(5);
                fd.isBlocked = rs.getBoolean(6);
                fd.eMailId = rs.getString(7);

                res.friends.add(fd);
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static CircleGroupList notificationCircleAddResponse(NotificationCircleRequest req)
    {
        CircleGroupList res = new CircleGroupList();
        res.circles = new ArrayList<CircleGroup>();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.NOTIFICATION_CIRCLE_RESPONSE);
            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2, req.circleMemberRequestID);
            cs.setInt(3, req.acceptanceStatus);
            cs.setString(4,req.circleMirrorName.trim());

            boolean results = cs.execute();
            int rsCount = 0;

            //Loop through the available result sets.
            while (results) {
                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {
                    if(rsCount == 0) //get all circles
                    {
                        CircleGroup cg = new CircleGroup();
                        cg.circleID =  rs.getInt(1);
                        cg.circleName =  rs.getString(2).trim();
                        cg.isAdmin =  rs.getBoolean(3);
                        cg.isPublic =  rs.getBoolean(4);
                        cg.circleImageUrl =  rs.getString(5).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                : rs.getString(5).trim();

                        res.circles.add(cg);
                    }
                    else if(rsCount == 1) //get circles members
                    {
                        CircleMember cm = new CircleMember();

                        int circleID = rs.getInt(1);
                        cm.contactNo = rs.getString(2).trim();

                        String imageUrl = rs.getString(3).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(3))
                                : rs.getString(3).trim();
                        cm.imageUrl = imageUrl;
                        cm.isAdmin = rs.getBoolean(4);
                        cm.memberUserID = rs.getInt(5);
                        cm.countryCode = rs.getString(6);

                        for(CircleGroup cg : res.circles )
                        {
                            if(cg.circleID == circleID)
                            {
                                if(cg.members == null)
                                {
                                    cg.members = new ArrayList<CircleMember>();
                                }
                                cg.members.add(cm);
                                break;
                            }
                        }
                    }
                    else //get locations
                    {
                        CircleLocation cl =  new CircleLocation();
                        int circleID = rs.getInt(1);

                        cl.name = rs.getString(2).trim();
                        cl.latitude = rs.getDouble(3);
                        cl.longitude = rs.getDouble(4);
                        cl.radius = rs.getInt(5);

                        for(CircleGroup cg : res.circles )
                        {
                            if(cg.circleID == circleID)
                            {
                                if(cg.locations == null)
                                {
                                    cg.locations = new ArrayList<CircleLocation>();
                                }
                                cg.locations.add(cl);
                                break;
                            }
                        }
                    }

                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static MessageResponse getMessages(MessageGetRequest req)
    {
        MessageResponse res = new MessageResponse();
        res.frndMessages = new ArrayList<MessageFormatOfFriend>();
        res.crclMessages = new ArrayList<MessageFormatOfCircle>();

        try
        {
            String contactsWithComma;
            StringBuilder result;

            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.MESSAGE_GET);

            /* for friend message id*/
            String frndMsgsWithComma = "";
            result = new StringBuilder();
            for(int id : req.frndMsgsID) {
                result.append(id);
                result.append(",");
            }

            frndMsgsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            /* end of friend message id */

            /* for circle message id*/
            String crclMsgsWithComma = "";
            result = new StringBuilder();
            for(int id : req.crclMsgsID) {
                result.append(id);
                result.append(",");
            }

            crclMsgsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            /* end of circle message id */

            cs.setString(1, req.deviceSession.trim());
            cs.setString(2, frndMsgsWithComma);
            cs.setString(3, crclMsgsWithComma);

            boolean results = cs.execute();
            int rsCount = 0;

            //Loop through the available result sets.
            while (results) {
                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {


                    if(rsCount == 0) //get all frnd messages
                    {
                        MessageFormatOfFriend msg = new MessageFormatOfFriend();
                        msg.msgId =  rs.getInt(1);
                        msg.fromId =  rs.getInt(2);
                        msg.toId =  rs.getInt(3);
                        int msgType = rs.getInt(4);
                        msg.msgType =  msgType;
                        if(msgType == 1)
                        {
                            msg.message =  rs.getString(5);
                        }
                        else
                        {
                            msg.img =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                            msg.imgThumb =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                        }
                        msg.msgOn = rs.getTimestamp(6);
                        res.frndMessages.add(msg);
                    }
                    else if(rsCount == 1) //get circles messages
                    {
                        MessageFormatOfCircle msg = new MessageFormatOfCircle();
                        msg.msgId =  rs.getInt(1);
                        msg.crclId = rs.getInt(2);
                        msg.fromId = rs.getInt(3);
                        int msgType = rs.getInt(4);
                        msg.msgType =  msgType;
                        if(msgType == 1)
                        {
                            msg.message =  rs.getString(5);
                        }
                        else
                        {
                            msg.img =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                            msg.imgThumb =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                        }
                        msg.msgOn = rs.getTimestamp(6);
                        res.crclMessages.add(msg);
                    }
                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static MessageResponse sendMessages(MessageGetRequest req)
    {
        MessageResponse res = new MessageResponse();
        res.frndMessages = new ArrayList<MessageFormatOfFriend>();
        res.crclMessages = new ArrayList<MessageFormatOfCircle>();

        try
        {
            String contactsWithComma;
            StringBuilder result;

            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.MESSAGE_GET);

            /* for friend message id*/
            String frndMsgsWithComma = "";
            result = new StringBuilder();
            for(int id : req.frndMsgsID) {
                result.append(id);
                result.append(",");
            }

            frndMsgsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            /* end of friend message id */

            /* for circle message id*/
            String crclMsgsWithComma = "";
            result = new StringBuilder();
            for(int id : req.crclMsgsID) {
                result.append(id);
                result.append(",");
            }

            crclMsgsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            /* end of circle message id */

            cs.setString(1, req.deviceSession.trim());
            cs.setString(2, frndMsgsWithComma);
            cs.setString(3, crclMsgsWithComma);

            boolean results = cs.execute();
            int rsCount = 0;

            //Loop through the available result sets.
            while (results) {
                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {


                    if(rsCount == 0) //get all frnd messages
                    {
                        MessageFormatOfFriend msg = new MessageFormatOfFriend();
                        msg.msgId =  rs.getInt(1);
                        msg.fromId =  rs.getInt(2);
                        msg.toId =  rs.getInt(3);
                        int msgType = rs.getInt(4);
                        msg.msgType =  msgType;
                        if(msgType == 1)
                        {
                            msg.message =  rs.getString(5);
                        }
                        else
                        {
                            msg.message =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                        }
                        msg.msgOn = rs.getTimestamp(6);
                        res.frndMessages.add(msg);
                    }
                    else if(rsCount == 1) //get circles messages
                    {
                        MessageFormatOfCircle msg = new MessageFormatOfCircle();
                        msg.msgId =  rs.getInt(1);
                        msg.crclId = rs.getInt(2);
                        msg.fromId = rs.getInt(3);
                        int msgType = rs.getInt(4);
                        msg.msgType =  msgType;
                        if(msgType == 1)
                        {
                            msg.message =  rs.getString(5);
                        }
                        else
                        {
                            msg.message =  rs.getString(5).trim().length() > 0
                                    ? (Constants.IMAGE_SHORT_URL + rs.getString(5))
                                    : rs.getString(5).trim();
                        }
                        msg.msgOn = rs.getTimestamp(6);
                        res.crclMessages.add(msg);
                    }
                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }
}

