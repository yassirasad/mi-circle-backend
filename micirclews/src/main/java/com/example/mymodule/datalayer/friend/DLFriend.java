package com.example.mymodule.datalayer.friend;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.businessObject.friend.FriendData;
import com.example.mymodule.businessObject.friend.FriendIgnoreRequest;
import com.example.mymodule.businessObject.friend.FriendRemoveBlockRequest;
import com.example.mymodule.businessObject.friend.FriendSearch;
import com.example.mymodule.businessObject.friend.FriendSearchResponse;
import com.example.mymodule.businessObject.friend.FriendSerachRequest;
import com.example.mymodule.businessObject.friend.FriendSuggestion;
import com.example.mymodule.businessObject.friend.FriendSuggestionResponse;
import com.example.mymodule.businessObject.friend.GetFriendResponse;
import com.example.mymodule.businessObject.friend.NewFriendRequest;
import com.example.mymodule.businessObject.friend.NewFriendWithIDRequest;
import com.example.mymodule.controller.openfireService.restAPI;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by kamaldua on 09/22/2015.
 */
public class DLFriend {

    public static ApiResponse sendFriendRequest(NewFriendRequest req) {
        ApiResponse res = new ApiResponse();
        String userName = "";
        String contactNum = "";
        String gcmId = "";
        ArrayList<String> deviceList = new ArrayList<String>();
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIEND_ADD);
            cs.setString(1, req.deviceSession.trim());
            cs.setString(2, req.contactNo.trim());

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                userName = rs.getString(1);
                contactNum = rs.getString(2);
                gcmId = rs.getString(3);
            }

            cs.close();
            conn.close();

            if(gcmId.length() > 0)
            {
                deviceList.add(gcmId);
                JSONObject data = new JSONObject();

                String message = userName + " (" + contactNum + ") wants to be your friend.";
                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_REQUEST_NEW, deviceList, data);
            }
            res.status = 1;
            res.message = "successful";
        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static GetFriendResponse getFriendsInfo(Object req, String query) {
        GetFriendResponse res = new GetFriendResponse();
        String contactForUser = "";
        String contactForFriend = "";
        String contactUserName = "";
        String deletedFriendGCMId = "";
        ArrayList<String> deviceList = new ArrayList<String>();

        res.friends = new ArrayList<FriendData>();
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(query);

            if(query.equals(StoreProcedures.UserProfile.FRIEND_GET)) {
                cs.setString(1, ((DeviceSessionEntity)req).deviceSession.trim());
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIEND_BLOCK)) {
                cs.setString(1, ((FriendRemoveBlockRequest)req).deviceSession.trim());
                cs.setInt(2, ((FriendRemoveBlockRequest)req).friendUserID);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIEND_UNBLOCK)) {
                cs.setString(1, ((FriendRemoveBlockRequest)req).deviceSession.trim());
                cs.setInt(2, ((FriendRemoveBlockRequest)req).friendUserID);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIENDS_REMOVE)) {
                cs.setString(1, ((FriendRemoveBlockRequest)req).deviceSession.trim());
                cs.setInt(2, ((FriendRemoveBlockRequest)req).friendUserID);

                cs.registerOutParameter(3, java.sql.Types.VARCHAR);
                cs.registerOutParameter(4, java.sql.Types.VARCHAR);
                cs.registerOutParameter(5, java.sql.Types.VARCHAR);
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            }

            ResultSet rs = cs.executeQuery();

            if(query.equals(StoreProcedures.UserProfile.FRIENDS_REMOVE)) {
                contactForUser = cs.getString(3);
                contactForFriend = cs.getString(4);
                contactUserName = cs.getString(5);
                deletedFriendGCMId = cs.getString(6);
            }

            while (rs.next()) {
                //LocationID, Name, Latitude, Longitude, Radius
                FriendData frnd = new FriendData();
                frnd.countryCode = rs.getString(1);
                frnd.contactNo = rs.getString(2);
                frnd.firstName = rs.getString(3);
                frnd.imageUrl = rs.getString(4).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(4))
                        : rs.getString(4).trim();

                frnd.FriendUserID = rs.getInt(5);
                frnd.isBlocked = rs.getBoolean(6);
                frnd.eMailId = rs.getString(7).trim();
                frnd.isAcc = rs.getBoolean(8);
                frnd.statusText = rs.getString(9);  // Added by Yassir
                res.friends.add(frnd);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

            if(res.status == 1 && contactForUser.length() > 0)
            {
                restAPI.deleteExistingFriend(contactForUser, contactForFriend);
            }

            //friend remove notification
            if(deletedFriendGCMId.length() > 0)
            {
                deviceList.add(deletedFriendGCMId);
                JSONObject data = new JSONObject();

                String message = contactUserName + " removed you from his/her friend list.";
                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_REMOVED, deviceList, data);
            }

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static GetFriendResponse getFriends(DeviceSessionEntity req) {
        return getFriendsInfo(req, StoreProcedures.UserProfile.FRIEND_GET);
    }


    public static FriendSearchResponse searchFriends(FriendSerachRequest req) {
        FriendSearchResponse res = new FriendSearchResponse();
        res.users = new ArrayList<FriendSearch>();
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIEND_SEARCH);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.findTypeID);
            cs.setString(3, req.searchString.trim());
            cs.setInt(4, req.limitID);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                FriendSearch frnd = new FriendSearch();
                frnd.userID = rs.getInt(1);
                frnd.firstName = rs.getString(2);
                String imageUrl = rs.getString(3).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(3))
                        : rs.getString(3).trim();
                frnd.imageUrl = imageUrl;
                frnd.isAlreadyFriend = rs.getBoolean(4);
                frnd.isFriendRequestSent = rs.getBoolean(5);
                frnd.isFriendRequestReceived = rs.getBoolean(6);

                res.users.add(frnd);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FriendSuggestionResponse suggestFriends(DeviceSessionEntity req) {
        FriendSuggestionResponse res = new FriendSuggestionResponse();
        res.suggestions = new ArrayList<FriendSuggestion>();
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIEND_SUGGESTION);
            cs.setString(1, req.deviceSession.trim());

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                FriendSuggestion frnd = new FriendSuggestion();
                frnd.friendUserID = rs.getInt(1);
                frnd.fullName = rs.getString(2).trim();
                String imageUrl = rs.getString(3).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(3)) : "";
                frnd.imageUrl = imageUrl;

                res.suggestions.add(frnd);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse ignoreFriend(FriendIgnoreRequest req) {

            FriendSuggestionResponse res = new FriendSuggestionResponse();
            res.suggestions = new ArrayList<FriendSuggestion>();
            try {
                Connection conn = ConnectionManager.getConnection();
                CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIEND_IGNORE);
                cs.setString(1, req.deviceSession.trim());
                cs.setInt(2, req.friendUserID);

                cs.executeQuery();

                cs.close();
                conn.close();

                res.status = 1;
                res.message = "successful";
            } catch (Exception ex) {
                res.status = 0;
                res.message = ex.getMessage();
            }
            return res;
        }

    public static ApiResponse sendFriendRequestWithID(NewFriendWithIDRequest req) {
        ApiResponse res = new ApiResponse();
        String userName = "";
        String contactNum = "";
        String gcmId = "";
        ArrayList<String> deviceList = new ArrayList<String>();
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIEND_ADD_WITHID);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.friendUserID);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                userName = rs.getString(1);
                contactNum = rs.getString(2);
                gcmId = rs.getString(3);
            }

            cs.close();
            conn.close();

            if(gcmId.length() > 0)
            {
                deviceList.add(gcmId);
                JSONObject data = new JSONObject();

                String message = userName + " (" + contactNum + ") wants to be your friend.";
                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_REQUEST_NEW, deviceList, data);
            }

            res.status = 1;
            res.message = "successful";
        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static GetFriendResponse blockFriend(FriendRemoveBlockRequest req) {
        return getFriendsInfo(req, StoreProcedures.UserProfile.FRIEND_BLOCK);
    }

    public static GetFriendResponse unBlockFriend(FriendRemoveBlockRequest req) {
        return getFriendsInfo(req, StoreProcedures.UserProfile.FRIEND_UNBLOCK);
    }

    public static GetFriendResponse unFriend(FriendRemoveBlockRequest req) {
        return getFriendsInfo(req, StoreProcedures.UserProfile.FRIENDS_REMOVE);
    }
}


