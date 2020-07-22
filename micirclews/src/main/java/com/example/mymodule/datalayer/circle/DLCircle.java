package com.example.mymodule.datalayer.circle;


import com.example.mymodule.businessObject.circle.CircleAddMemberRequest;
import com.example.mymodule.businessObject.circle.CircleAddMultipleMembersRequest;
import com.example.mymodule.businessObject.circle.CircleAddMultipleMembersResponse;
import com.example.mymodule.businessObject.circle.CircleAddNewMemberStatus;
import com.example.mymodule.businessObject.circle.CircleAgeUpdateRequest;
import com.example.mymodule.businessObject.circle.CircleCreateRequest;
import com.example.mymodule.businessObject.circle.CircleDeleteRequest;
import com.example.mymodule.businessObject.circle.CircleGroup;
import com.example.mymodule.businessObject.circle.CircleGroupList;
import com.example.mymodule.businessObject.circle.CircleLocation;
import com.example.mymodule.businessObject.circle.CircleMember;
import com.example.mymodule.businessObject.circle.CircleMemberDeleteRequest;
import com.example.mymodule.businessObject.circle.CircleMemberLocation;
import com.example.mymodule.businessObject.circle.CirclemembersForXMPP;
import com.example.mymodule.businessObject.circle.CirclemembersForXMPPList;
import com.example.mymodule.businessObject.circle.CirclesMemberLocations;
import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.controller.openfireService.restAPI;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kamaldua on 08/16/2015.
 */
public class DLCircle {
    public static CircleGroupList getCircles(DeviceSessionEntity request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.USER_CIRCLES, request);
    }

    public static CircleGroupList createCircle(CircleCreateRequest request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_CREATE, request);
    }

    public static CircleGroupList deleteCircle(CircleDeleteRequest request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_DELETE, request);
    }

    public static CircleGroupList deleteMemberFromCircle(CircleMemberDeleteRequest request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_DELETE_MEMBER, request);
    }

    /** @Created by Yassir **/
    public static CircleGroupList leaveCircle(CircleDeleteRequest request){
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_LEAVE,request);
    }

    public static ApiResponse addMemberToCircle(CircleAddMemberRequest request)
    {
        ApiResponse res = new ApiResponse();
        String gcmId = "";
        String message = "";
        ArrayList<String> deviceList = new ArrayList<String>();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.CIRCLE_ADD_MEMBER);
            cs.setString(1,request.deviceSession.trim());
            cs.setInt(2, request.circleID);
            cs.setInt(3, request.userID);

            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);

            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.registerOutParameter(7, java.sql.Types.VARCHAR);

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                gcmId = rs.getString(1);
                message = rs.getString(2);
            }

            String xmppCircleName = cs.getString(4);
            String friendContact = cs.getString(5);

            String xmppFriendPanicCircleName = cs.getString(6);
            String selfContact = cs.getString(7);

            cs.close();
            conn.close();

            if(gcmId.length() > 0)
            {
                deviceList.add(gcmId);
                JSONObject data = new JSONObject();

                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.CIRCLE_MEMBER_ADD, deviceList, data);
            }

            res.status = 1;
            res.message = "Request send successfully";


            if(res.status == 1 && xmppCircleName.length() > 0 && friendContact.length() > 0 )
            {
                restAPI.addMemberToCircle(xmppCircleName,friendContact);

                if(xmppFriendPanicCircleName.length() > 0 && selfContact.length() > 0)
                {
                    restAPI.addMemberToCircle(xmppFriendPanicCircleName,selfContact);
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
    //multiple members
    public static CircleAddMultipleMembersResponse addMultipleMembersToCircle(CircleAddMultipleMembersRequest request)
    {
        CircleAddMultipleMembersResponse res = new CircleAddMultipleMembersResponse();
        HashMap<String, String> notifications = new HashMap<String, String>();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.CIRCLE_ADD_MULTIPLE_MEMBER);
            cs.setString(1, request.deviceSession.trim());
            cs.setInt(2, request.circleID);

            StringBuilder result = new StringBuilder();
            for(int item : request.userIDs) {
                result.append(item);
                result.append(",");
            }
            String contactsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
            cs.setString(3,contactsWithComma.trim());
            cs.setBoolean(4,false); //IsCalledFromCreateCircle

            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            cs.registerOutParameter(7, java.sql.Types.VARCHAR);
            cs.registerOutParameter(8, java.sql.Types.VARCHAR);

            res.members = new ArrayList<CircleAddNewMemberStatus>();

            ResultSet rs = cs.executeQuery();

            String xmppCircleName = cs.getString(5);
            String xmppUsersConts = cs.getString(6);
            String selfContNo = cs.getString(7);
            String xmppPanicCircle = cs.getString(8);

            while(rs.next())
            {
                CircleAddNewMemberStatus mem = new CircleAddNewMemberStatus();
                mem.userID = rs.getInt(1);
                mem.status = rs.getBoolean(2);
                mem.err = rs.getString(3);

                if(mem.status == true)
                {
                    notifications.put(rs.getString(6), rs.getString(7));
                }
                res.members.add(mem);
            }

            cs.close();
            conn.close();

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
                        GCMSender.sendData(Constants.API_KEY, NotificationType.CIRCLE_MEMBER_ADD, deviceList, data);
                    }
                }
            }
            res.status = 1;
            res.message = "Request send successfully";

            if(res.status == 1 && xmppCircleName.length() > 0 && xmppUsersConts.length()>0)
            {
                List<String> usersList = Arrays.asList(xmppUsersConts.split(","));
                for(String st : usersList)
                {
                    if(st.trim().length() > 0)
                    {
                        restAPI.addMemberToCircle(xmppCircleName, st);
                    }
                }

                if(xmppPanicCircle.length() > 0)
                {
                    List<String> panicCrcls = Arrays.asList(xmppPanicCircle.split(","));
                    for(String st : panicCrcls)
                    {
                        if(st.trim().length() > 0)
                        {
                            restAPI.addMemberToCircle(st, selfContNo);
                        }
                    }
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
    public static CircleGroupList addLocationToCircle(CircleMemberLocation request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_LOCATION_ADD, request);
    }

    public static CircleGroupList addLocationsToCircles(CirclesMemberLocations request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_ADD, request);
    }

    public static CircleGroupList deleteLocationToCircle(CircleMemberLocation request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLE_LOCATION_DELETE, request);
    }

    public static CircleGroupList deleteLocationsToCircles(CirclesMemberLocations request)
    {
        return getCircleInformation(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_DELETE, request);
    }

    public static CircleGroupList getCircleInformation(String query, Object req)
    {
        CircleGroupList res = new CircleGroupList();
        res.circles = new ArrayList<CircleGroup>();

        CirclemembersForXMPPList memsList = new CirclemembersForXMPPList();
        memsList.mems = new ArrayList<CirclemembersForXMPP>();

        String xmppCirclename = "";
        String xmppCirclePass = "";
        String xmppPanicCircleName = "";
        String xmppPanicUserConNo ="";

        //for create circles
        String xmppUsersConts = "";
        String selfContNo = "";
        String xmppPanicCircles = "";

        //Notifications
        HashMap<String, String> notifications = new HashMap<String, String>();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(query);
            if(query.equals(StoreProcedures.UserProfile.USER_CIRCLES)) {
                cs.setString(1, ((DeviceSessionEntity)req).deviceSession.trim());
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_CREATE))
            {
                CircleCreateRequest request = (CircleCreateRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setString(2,request.circleName.trim());

                StringBuilder result = new StringBuilder();
                for(int item : request.userIds) {
                    result.append(item);
                    result.append(",");
                }
                String contactsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(3,contactsWithComma.trim());

                result = new StringBuilder();
                for(int item : request.locationIDs) {
                    result.append(item);
                    result.append(",");
                }
                String locationsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(4,locationsWithComma.trim());
                cs.setTimestamp(5, java.sql.Timestamp.valueOf(request.circleAge));
                cs.setBoolean(6,request.isPublic);
                cs.registerOutParameter(7, java.sql.Types.VARCHAR);
                cs.registerOutParameter(8, java.sql.Types.VARCHAR);

                cs.registerOutParameter(9, java.sql.Types.VARCHAR);
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);
                cs.registerOutParameter(11, java.sql.Types.VARCHAR);

            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE))
            {
                CircleDeleteRequest request = (CircleDeleteRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2,request.circleID);
                cs.registerOutParameter(3, java.sql.Types.VARCHAR);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE_MEMBER))
            {
                CircleMemberDeleteRequest request = (CircleMemberDeleteRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2, request.circleID);
                cs.setString(3, request.contactToDelete.trim());
                cs.registerOutParameter(4, java.sql.Types.VARCHAR);
                cs.registerOutParameter(5, java.sql.Types.VARCHAR);
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);
                cs.registerOutParameter(7, java.sql.Types.VARCHAR);
                cs.registerOutParameter(8, java.sql.Types.VARCHAR);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LEAVE)){
                CircleDeleteRequest request = (CircleDeleteRequest)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2,request.circleID);
                cs.registerOutParameter(3, java.sql.Types.VARCHAR);
                cs.registerOutParameter(4, java.sql.Types.VARCHAR);
                cs.registerOutParameter(5, java.sql.Types.VARCHAR);
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LOCATION_ADD)
            || query.equals(StoreProcedures.UserProfile.CIRCLE_LOCATION_DELETE)){

                CircleMemberLocation request = (CircleMemberLocation)req;
                cs.setString(1,request.deviceSession.trim());
                cs.setInt(2, request.circleID);
                cs.setInt(3, request.locationID);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_ADD)
                    || query.equals(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_DELETE)){


                CirclesMemberLocations request = (CirclesMemberLocations)req;
                cs.setString(1,request.deviceSession.trim());

                StringBuilder result = new StringBuilder();
                for(int item : request.circleIDs) {
                    result.append(String.valueOf(item) );
                    result.append(",");
                }
                String circlesIDWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(2, circlesIDWithComma);

                result = new StringBuilder();
                for(int item : request.locationIDs) {
                    result.append(String.valueOf(item) );
                    result.append(",");
                }
                String locationsIDWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";
                cs.setString(3, locationsIDWithComma);
            }

            boolean results = cs.execute();
            int rsCount = 0;

            if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE_MEMBER)) {
                xmppCirclename = cs.getString(4);
                xmppPanicCircleName = cs.getString(5);
                xmppPanicUserConNo = cs.getString(6);
                notifications.put(cs.getString(7), cs.getString(8));

            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LEAVE)){
                xmppCirclename = cs.getString(3);
                selfContNo = cs.getString(4);
                notifications.put(cs.getString(5), cs.getString(6));
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE)) {
                xmppCirclename = cs.getString(3);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_CREATE))
            {
                xmppCirclename = cs.getString(7);
                xmppCirclePass = cs.getString(8);

                xmppUsersConts = cs.getString(9);
                selfContNo = cs.getString(10);
                xmppPanicCircles = cs.getString(11);
            }


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
                        cg.circleAge = rs.getTimestamp(6);
                        cg.xmppCrclName = rs.getString(7);
                        res.circles.add(cg);
                    }
                    else if(rsCount == 1) //get circles members
                    {
                        CircleMember cm = new CircleMember();

                        int circleID = rs.getInt(1);
                        cm.circleID = circleID;
                        cm.contactNo = rs.getString(2).trim();

                        String imageUrl = rs.getString(3).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(3))
                                : rs.getString(3).trim();
                        cm.imageUrl = imageUrl;
                        cm.isAdmin = rs.getBoolean(4);
                        cm.memberUserID = rs.getInt(5);
                        cm.countryCode = rs.getString(6);
                        cm.isAcc = rs.getBoolean(7);
                        cm.name = rs.getString(8);

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
                    else if(rsCount == 2) //get locations
                    {
                        CircleLocation cl =  new CircleLocation();
                        int circleID = rs.getInt(1);

                        cl.name = rs.getString(2).trim();
                        cl.latitude = rs.getDouble(3);
                        cl.longitude = rs.getDouble(4);
                        cl.radius = rs.getInt(5);
                        cl.isTrkEnbl = rs.getBoolean(6);
                        cl.locId = rs.getInt(7);
                        cl.locImg =  rs.getString(8).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(8))
                                : rs.getString(8).trim();
                        cl.locAdd = rs.getString(9).trim();

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
                   else if(rsCount == 3) //only for create circle notifications, delete circle, Location Add, Circles locations add, location remove, locations remove
                   {
                       String gcmId = rs.getString(1);
                       String message = rs.getString(2);
                       notifications.put(gcmId, message);
                   }
                    else if(rsCount == 4) //only for create circle
                    {
                        CirclemembersForXMPP mem =   new CirclemembersForXMPP();
                        mem.cont = rs.getString(1);
                        mem.isAdmin = rs.getBoolean(2);

                        memsList.mems.add(mem);
                    }

                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
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
                        int miType = 0;

                        if(query.equals(StoreProcedures.UserProfile.CIRCLE_CREATE))
                        {
                            miType = NotificationType.CIRCLE_MEMBER_ADD;
                        }
                        else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE))
                        {
                            miType = NotificationType.CIRCLE_DELETE;
                        }
                        else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE_MEMBER))
                        {
                            miType = NotificationType.CIRCLE_MEMBER_DELETE;
                        }
                        else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LEAVE)){
                            miType = NotificationType.CIRCLE_MEMBER_LEAVE;
                        }
                        else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LOCATION_ADD) || query.equals(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_ADD))
                        {
                            miType = NotificationType.CIRCLE_LOCATION_ADD;
                        }
                        else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LOCATION_DELETE) || query.equals(StoreProcedures.UserProfile.CIRCLES_LOCATIONS_DELETE))
                        {
                            miType = NotificationType.CIRCLE_LOCATION_DELETE;
                        }

                        GCMSender.sendData(Constants.API_KEY, miType, deviceList, data);
                    }
                }
            }

            res.status = 1;
            res.message = "successful";

            //XMPP server entry and delete.
            if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE_MEMBER) && res.status == 1 && xmppCirclename.length() > 0) {
                CircleMemberDeleteRequest request = (CircleMemberDeleteRequest)req;
                restAPI.removeMemberFromCircle(xmppCirclename, request.contactToDelete.trim());

                if(xmppPanicCircleName.length() > 0 && xmppPanicUserConNo.length() > 0)
                {
                    restAPI.removeMemberFromCircle(xmppPanicCircleName, xmppPanicUserConNo);
                }
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_LEAVE) && res.status == 1 && xmppCirclename.length() > 0){
                restAPI.removeMemberFromCircle(xmppCirclename,selfContNo.trim());
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_DELETE) && res.status == 1 && xmppCirclename.length() > 0) {

                restAPI.deleteCircle(xmppCirclename);
            }
            else if(query.equals(StoreProcedures.UserProfile.CIRCLE_CREATE) && res.status == 1 && memsList.mems.size() > 0 && xmppCirclename.length() > 0 && xmppCirclePass.length() > 0)
            {
                ArrayList<String> xmppCircleMembers = new ArrayList<String>();
                ArrayList<String>  xmppCircleOwners = new ArrayList<String>();

                for (CirclemembersForXMPP mem : memsList.mems) {
                    if(mem.isAdmin == true)
                    {
                        String owner = Constants.getMemberWithURL(mem.cont);
                        xmppCircleOwners.add(owner);
                    }
                    else
                    {
                        String member = Constants.getMemberWithURL(mem.cont);
                        xmppCircleMembers.add(member);
                    }
                }

                restAPI.createCircle(xmppCirclename, xmppCirclePass, xmppCircleOwners, xmppCircleMembers);

                if(xmppPanicCircles.length() > 0 && selfContNo.length() > 0)
                {
                    List<String> panicCrcls = Arrays.asList(xmppPanicCircles.split(","));
                    for(String st : panicCrcls)
                    {
                        if(st.trim().length() > 0)
                        {
                            restAPI.addMemberToCircle(st, selfContNo);
                        }
                    }
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

    public static ApiResponse circleAgeUpdate(CircleAgeUpdateRequest req )
    {
        ApiResponse res = new ApiResponse();
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String sp = StoreProcedures.UserProfile.CIRCLE_AGE_UPDATE;
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1,req.deviceSession.trim());
            cs.setInt(2,req.circleId);
            cs.setTimestamp(3, java.sql.Timestamp.valueOf(req.circleAge));

            cs.executeUpdate();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch(Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }
    /*
    public static MemberCircleResponse getMemberCircles(UserContactRequest req)
    {
        MemberCircleResponse res = new MemberCircleResponse();
        res.members = new ArrayList<MemberOfCircle>();
        String contactsWithComma;
        try
        {
            StringBuilder result = new StringBuilder();
            for(String item : req.contacts) {
                result.append(item.trim());
                result.append(",");
            }
            contactsWithComma = result.length() > 0 ? result.substring(0, result.length() - 1): "";

            Connection conn = ConnectionManager.getConnection();

            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.CIRCLE_MEMBERS);
            cs.setString(1,req.deviceSession.trim());
            cs.setString(2,contactsWithComma.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                MemberOfCircle member = null;
                boolean isMemberExist = false;
                //contact,imageurl,circlename, circleimage,ispublic,isadmin
                String contactNo = rs.getString(1);
                for(MemberOfCircle item : res.members)
                {
                    if(item.contactNo.equals(contactNo))
                    {
                        member = item;
                        isMemberExist = true;
                        break;
                    }
                }

                if(isMemberExist == false)
                {
                    member = new MemberOfCircle();
                    member.contactNo = contactNo;
                    member.imageUrl = (Constants.IMAGE_UPLOAD_URL + rs.getString(2));
                    member.circles = new ArrayList<MemberCircle>();
                    res.members.add(member);
                }

                //add circle to member
                MemberCircle circle = new MemberCircle();
                circle.circleName = rs.getString(3);
                circle.circleImageUrl = rs.getString(4);
                circle.isPublic = rs.getBoolean(5);
                circle.isAdmin = rs.getBoolean(6);

                member.circles.add(circle);
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
    */
}
