package com.example.mymodule.datalayer.location;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.businessObject.location.Location;
import com.example.mymodule.businessObject.location.LocationActivity;
import com.example.mymodule.businessObject.location.LocationActivityAllRequest;
import com.example.mymodule.businessObject.location.LocationActivityAllResponse;
import com.example.mymodule.businessObject.location.LocationActivityRequest;
import com.example.mymodule.businessObject.location.LocationActivityResponse;
import com.example.mymodule.businessObject.location.LocationActivitySetRequest;
import com.example.mymodule.businessObject.location.LocationAddRequest;
import com.example.mymodule.businessObject.location.LocationCircleActivity;
import com.example.mymodule.businessObject.location.LocationDeleteRequest;
import com.example.mymodule.businessObject.location.LocationFriend;
import com.example.mymodule.businessObject.location.LocationFriendActivity;
import com.example.mymodule.businessObject.location.LocationFriendRequest;
import com.example.mymodule.businessObject.location.LocationGetResponse;
import com.example.mymodule.businessObject.location.LocationsFriendsRequest;
import com.example.mymodule.businessObject.location.UnshareLocFromCircle;
import com.example.mymodule.businessObject.location.UnshareLocationFromCirclesRequest;
import com.example.mymodule.businessObject.notification.NotificationItem;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kamaldua on 09/21/2015.
 */
public class DLLocation {

    public static LocationGetResponse addLocation(LocationAddRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.LOCATION_ADD, req);
        /*LocationGetResponse res = new LocationGetResponse();
        res.locations = new ArrayList<Location>();
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_ADD);
            cs.setString(1,req.deviceSession.trim());
            cs.setString(2,req.name.trim());
            cs.setString(3,req.address.trim());
            cs.setDouble(4, req.latitude);
            cs.setDouble(5, req.longitude);
            cs.setInt(6, req.radius);

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                //LocationID, Name, Latitude, Longitude, Radius
                Location loc = new Location();
                loc.locationID = rs.getInt(1);
                loc.name = rs.getString(2);
                loc.address = rs.getString(3);
                loc.latitude = rs.getDouble(4);
                loc.longitude = rs.getDouble(5);
                loc.radius = rs.getInt(6);

                res.locations.add(loc);
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
        */
    }
    public static LocationGetResponse getLocations(String deviceSession)
    {
        return getLocationInformation(StoreProcedures.UserProfile.LOCATION_GET, deviceSession);
        /*LocationGetResponse res = new LocationGetResponse();
        res.locations = new ArrayList<Location>();

        try
        {

            Connection conn = ConnectionManager.getConnection();

            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_GET);
            cs.setString(1,deviceSession.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                //LocationID, Name, Latitude, Longitude, Radius
                Location loc = new Location();
                loc.locationID = rs.getInt(1);
                loc.name = rs.getString(2);
                loc.address = rs.getString(3);
                loc.latitude = rs.getDouble(4);
                loc.longitude = rs.getDouble(5);
                loc.radius = rs.getInt(6);

                res.locations.add(loc);
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
        */
    }
    public static LocationGetResponse deleteLocation(LocationDeleteRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.LOCATION_DELETE, req);
        /*LocationGetResponse res = new LocationGetResponse();
        res.locations = new ArrayList<Location>();
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_DELETE);
            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2,req.locationID);

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                //LocationID, Name, Latitude, Longitude, Radius
                Location loc = new Location();
                loc.locationID = rs.getInt(1);
                loc.name = rs.getString(2);
                loc.address = rs.getString(3);
                loc.latitude = rs.getDouble(4);
                loc.longitude = rs.getDouble(5);
                loc.radius = rs.getInt(6);

                res.locations.add(loc);
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
        */
    }

    public static LocationGetResponse shareLocationWithFriend(LocationFriendRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.FRIEND_LOCATION_ADD, req);
    }

    public static LocationGetResponse unShareLocationWithFriend(LocationFriendRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.FRIEND_LOCATION_DELETE, req);
    }

    public static LocationGetResponse addLocationsWithFriends(LocationsFriendsRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_ADD, req);
    }

    public static LocationGetResponse deleteLocationsWithFriends(LocationsFriendsRequest req)
    {
        return getLocationInformation(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_DELETE, req);
    }

    public static LocationGetResponse leaveLocationOfFriend(LocationDeleteRequest req){
        return getLocationInformation(StoreProcedures.UserProfile.FRIEND_LOCATION_LEAVE, req);
    }

    public static LocationGetResponse getLocationInformation(String query, Object req)
    {
        LocationGetResponse res = new LocationGetResponse();
        res.locations = new ArrayList<Location>();
        int userID = 0;
        String userName = null;
        String locationIDsWithComma = "";
        List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(query);

            if(query.equals(StoreProcedures.UserProfile.LOCATION_ADD)) {
                LocationAddRequest request = (LocationAddRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setString(2,request.name.trim());
                cs.setString(3,request.address.trim());
                cs.setDouble(4, request.latitude);
                cs.setDouble(5, request.longitude);
                cs.setInt(6, request.radius);
            }
            else if(query.equals(StoreProcedures.UserProfile.LOCATION_GET)) {
                cs.setString(1,((String)req).trim());
            }
            else if(query.equals(StoreProcedures.UserProfile.LOCATION_DELETE)) {
                LocationDeleteRequest request = (LocationDeleteRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2,request.locationID);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIEND_LOCATION_ADD)) {
                LocationFriendRequest request = (LocationFriendRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setString(2,request.contactNoWithCountryCode);
                cs.setInt(3,request.locationID);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIEND_LOCATION_DELETE)) {
                LocationFriendRequest request = (LocationFriendRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setString(2,request.contactNoWithCountryCode);
                cs.setInt(3,request.locationID);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_ADD) ||
                    query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_DELETE) ) {
                LocationsFriendsRequest request = (LocationsFriendsRequest)req;
                cs.setString(1,request.deviceSession.trim());

                StringBuilder result = new StringBuilder();
                for(int item : request.friendIDs) {
                    result.append(String.valueOf(item) );
                    result.append(",");
                }
                String friendIDsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(2,friendIDsWithComma);

                result = new StringBuilder();
                for(int item : request.locationIDs) {
                    result.append(String.valueOf(item) );
                    result.append(",");
                }
                locationIDsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(3, locationIDsWithComma);

                cs.registerOutParameter(4, java.sql.Types.INTEGER);
                cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            }
            else if(query.equals(StoreProcedures.UserProfile.FRIEND_LOCATION_LEAVE)){
                LocationDeleteRequest request = (LocationDeleteRequest) req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2,request.locationID);
            }

            boolean results = cs.execute();
            int rsCount = 0;

            while (results) {

                ResultSet rs = cs.getResultSet();

                //Retrieve data from the result set.
                while (rs.next()) {
                    if(rsCount == 0) //get all circles
                    {
                        //LocationID, Name, Latitude, Longitude, Radius
                        Location loc = new Location();
                        loc.locationID = rs.getInt(1);
                        loc.name = rs.getString(2);
                        loc.address = rs.getString(3);
                        loc.latitude = rs.getDouble(4);
                        loc.longitude = rs.getDouble(5);
                        loc.radius = rs.getInt(6);
                        String imageUrl = rs.getString(7).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(7))
                                : rs.getString(7).trim();
                        loc.imageURL = imageUrl;
                        loc.isAdmin = rs.getBoolean(8);

                        loc.sharedWithFriends = new ArrayList<LocationFriend>();
                        res.locations.add(loc);
                    }
                    else if(rsCount == 1) //get circles members
                    {
                        int locationID = rs.getInt(1);
                        String contactNo = rs.getString(2).trim();
                        int friendUserID = rs.getInt(3);
                        int friendLocationID = rs.getInt(4);

                        for(Location lc : res.locations )
                        {
                            if(lc.locationID == locationID)
                            {
                                LocationFriend lf = new LocationFriend();
                                lf.contactNo = contactNo;
                                lf.friendUserID = friendUserID;
                                lf.friendLocationID = friendLocationID;

                                lc.sharedWithFriends.add(lf);
                                break;
                            }
                        }
                    }
                    else if(rsCount == 2 && (query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_ADD) ||
                            query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_DELETE) )) //get circles members
                    {
                        userID = cs.getInt(4);
                        userName = cs.getString(5);
                        NotificationItem item = new NotificationItem();
                        item.gcmId = rs.getString(1);
                        item.message = rs.getString(2);

                        notificationItems.add(item);
                    }
                }

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            if(query.equals(StoreProcedures.UserProfile.FRIEND_LOCATION_LEAVE)){
                NotificationItem item = new NotificationItem();
                item.gcmId = cs.getString(3);
                item.message = cs.getString(4);

                notificationItems.add(item);
            }

            cs.close();
            conn.close();

            //Notifications
            if(notificationItems.size() > 0)
            {
                for(NotificationItem item : notificationItems)
                {
                    ArrayList<String> deviceList = new ArrayList<String>();
                    JSONObject data = new JSONObject();
                    String message = "";
                    deviceList.add(item.gcmId);

                    if(query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_ADD))
                    {
                        message = "["+userID+"]["+userName+"] shared location(s) ["+locationIDsWithComma+"]["+item.message+"] with you.";
                        data.put("message", message);
                        GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_LOCATION_ADD, deviceList, data);
                    }
                    else if(query.equals(StoreProcedures.UserProfile.FRIENDS_LOCATIONS_DELETE))
                    {
                        message = "["+userID+"]["+userName+"] unshared location(s) ["+locationIDsWithComma+"]["+item.message+"] with you.";
                        data.put("message", message);
                        GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_LOCATION_DELETE, deviceList, data);
                    }
                    else if (query.equals(StoreProcedures.UserProfile.FRIEND_LOCATION_LEAVE)){
                        data.put("message",item.message);
                        GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_LOCATION_LEAVE, deviceList, data);
                    }
                }
            }
            res.status = 1;
            res.message = "successful";
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static LocationActivityResponse getLocationActivities(LocationActivityRequest req) {

        LocationActivityResponse res = new LocationActivityResponse();
        res.frnds = new ArrayList<LocationFriendActivity>();
        res.crcls = new ArrayList<LocationCircleActivity>();

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_ACTIVITY_GET);

            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2,req.locationID);
            cs.setInt(3,req.maxlocActivityID);

            boolean results = cs.execute();
            int rsCount = 0;

            while (results) {

                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {
                    if (rsCount == 0) //get all friends activities
                    {
                        LocationFriendActivity activity = new LocationFriendActivity();
                        activity.friendUserID = rs.getInt(1);
                        activity.isCheckedIn = rs.getBoolean(2);
                        activity.createdOn = rs.getTimestamp(3);
                        activity.isAdmin = rs.getBoolean(4);
                        activity.userName = rs.getString(5);
                        activity.locID = rs.getInt(6);
                        activity.locName = rs.getString(7);
                        activity.locActId = rs.getInt(8);

                        res.frnds.add(activity);
                    }
                    else if (rsCount == 1) //get all circles activities
                    {
                        LocationCircleActivity activity = new LocationCircleActivity();
                        activity.circleID = rs.getInt(1);
                        activity.friendUserID = rs.getInt(2);
                        activity.isCheckedIn = rs.getBoolean(3);
                        activity.createdOn = rs.getTimestamp(4);
                        activity.isAdmin = rs.getBoolean(5);
                        activity.userName = rs.getString(6);
                        activity.circleName = rs.getString(7);
                        activity.locID = rs.getInt(8);
                        activity.locName = rs.getString(9);
                        activity.locActId = rs.getInt(10);

                        res.crcls.add(activity);
                    }
                }

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse setLocationActivities(LocationActivitySetRequest req) {

        ApiResponse res = new ApiResponse();
        //Notifications
        HashMap<String, String> notifications = new HashMap<String, String>();

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_ACTIVITY_SET);

            cs.setString(1,req.deviceSession.trim());

            StringBuilder result = new StringBuilder();
            for(int item : req.locationsIdsForCheckIn) {
                result.append(String.valueOf(item) );
                result.append(",");
            }
            String checkInsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            cs.setString(2,checkInsWithComma.trim());

            result = new StringBuilder();
            for(int item : req.locationsIdsForCheckOut) {
                result.append(String.valueOf(item) );
                result.append(",");
            }
            String checkOutsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            cs.setString(3,checkOutsWithComma.trim());

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String gcmId = rs.getString(1);
                String message = rs.getString(2);
                notifications.put(gcmId, message);
            }

            cs.close();
            conn.close();

            //Notifications
            if(notifications.size() > 0)
            {
                for(Map.Entry m : notifications.entrySet()){
                    String gcmId = (String)m.getKey();
                    String message = (String)m.getValue();
                    ArrayList<String> deviceList = new ArrayList<String>();

                    if(gcmId.length() > 0)
                    {
                        deviceList.add(gcmId);
                        JSONObject data = new JSONObject();

                        data.put("message", message);
                        GCMSender.sendData(Constants.API_KEY, NotificationType.LOCATION_CHECK_IN_OR_CHECK_OUT, deviceList, data);
                    }
                }
            }
            res.status = 1;
            res.message = "successful";
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static LocationActivityAllResponse getAllLocationActivities(LocationActivityAllRequest req) {

        LocationActivityAllResponse res = new LocationActivityAllResponse();
        res.locationActivities = new ArrayList<LocationFriendActivity>();
        res.circleActivities = new ArrayList<LocationCircleActivity>();

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.LOCATION_GET_ALL);

            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.maxlocActivityID);

            boolean results = cs.execute();
            int rsCount = 0;

            while (results) {

                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {
                    if (rsCount == 0) //get all friends activities
                    {
                        LocationFriendActivity activity = new LocationFriendActivity();
                        activity.friendUserID = rs.getInt(1);
                        activity.isCheckedIn = rs.getBoolean(2);
                        activity.createdOn = rs.getTimestamp(3);
                        activity.isAdmin = rs.getBoolean(4);
                        activity.userName = rs.getString(5);
                        activity.locID = rs.getInt(6);
                        activity.locName = rs.getString(7);
                        activity.locActId = rs.getInt(8);

                        res.locationActivities.add(activity);
                    }
                    else if (rsCount == 1) //get all circles activities
                    {
                        LocationCircleActivity activity = new LocationCircleActivity();
                        activity.circleID = rs.getInt(1);
                        activity.friendUserID = rs.getInt(2);
                        activity.isCheckedIn = rs.getBoolean(3);
                        activity.createdOn = rs.getTimestamp(4);
                        activity.isAdmin = rs.getBoolean(5);
                        activity.userName = rs.getString(6);
                        activity.circleName = rs.getString(7);
                        activity.locID = rs.getInt(8);
                        activity.locName = rs.getString(9);
                        activity.locActId = rs.getInt(10);

                        res.circleActivities.add(activity);
                    }
                }

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse unShareLocationsFromCircles(UnshareLocationFromCirclesRequest req) {

        ApiResponse res = new ApiResponse();

        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_UNSHARE);

            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2, req.locID);

            StringBuilder result = new StringBuilder();
            for(UnshareLocFromCircle item : req.circleTracking) {
                result.append(String.valueOf(item.cclID));
                result.append(":");
                result.append(String.valueOf(item.isDsbl));
                result.append(",");
            }
            String cclDisabledInfo = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            cs.setString(3,cclDisabledInfo.trim());

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


}
