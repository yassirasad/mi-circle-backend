package com.example.mymodule.datalayer.panic;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.GCMSender;
import com.example.mymodule.businessObject.common.NotificationType;
import com.example.mymodule.businessObject.panic.AllActivePanicRequest;
import com.example.mymodule.businessObject.panic.PanicActiveData;
import com.example.mymodule.businessObject.panic.PanicActiveDataResponse;
import com.example.mymodule.businessObject.panic.PanicData;
import com.example.mymodule.businessObject.panic.PanicDataRequest;
import com.example.mymodule.businessObject.panic.PanicDataResponse;
import com.example.mymodule.businessObject.panic.PanicFollowRequest;
import com.example.mymodule.businessObject.panic.PanicMember;
import com.example.mymodule.businessObject.panic.PanicReleaseRequest;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.StreamHandler;

/**
 * Created by kamaldua on 02/03/2016.
 */
public class DLPanic {
    public static ApiResponse panicRespond(PanicFollowRequest req)
    {
        ApiResponse res = new ApiResponse();
        ArrayList<String> deviceList = new ArrayList<String>();
        String panicUserName = "";
        String respondUserName = "";

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.PANIC_RESPOND);
            cs.setString(1, req.deviceSession);
            cs.setInt(2, req.panicID);
            cs.setBoolean(3, req.isAccepted);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);

            ResultSet rs = cs.executeQuery();

            panicUserName = cs.getString(4);
            respondUserName = cs.getString(5);

            while(rs.next())
            {
                deviceList.add( rs.getString(1));
            }

            cs.close();
            conn.close();

            if(deviceList.size() > 0)
            {
                JSONObject data = new JSONObject();

                data.put("PanicId", req.panicID);
                String message = "[" + respondUserName + "] respond on [" + panicUserName + "] panic. He/She is " +
                        (req.isAccepted ? "following now." : " not following any more.");
                data.put("message", message);

                GCMSender.sendData(Constants.API_KEY, NotificationType.PANIC_RESPOND, deviceList, data);
            }

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
    public static PanicDataResponse getPanicData(PanicDataRequest req)
    {
        PanicDataResponse res = new PanicDataResponse();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.PANIC_DATA);
            cs.setString(1, req.deviceSession);
            cs.setInt(2, req.panicID);
            cs.setDouble(3, req.latitude);
            cs.setDouble(4, req.longitude);
            cs.setString(5, req.distanceAway);
            cs.setString(6, req.timeAway);

            cs.registerOutParameter(7, java.sql.Types.BIT);
            cs.registerOutParameter(8, java.sql.Types.VARCHAR);

            res.data = new ArrayList<PanicData>();
            res.members = new ArrayList<PanicMember>();

            boolean results = cs.execute();
            int rsCount = 0;

            while (results) {
                ResultSet rs = cs.getResultSet();
                //Retrieve data from the result set.
                while (rs.next()) {
                    if(rsCount == 0) //get locations
                    {
                        PanicData pd = new PanicData();
                        pd.userID = rs.getInt(1);
                        pd.userName = rs.getString(2);
                        pd.latitude = rs.getDouble(3);
                        pd.longitude = rs.getDouble(4);
                        pd.createdOn = rs.getTimestamp(5);
                        pd.isPanicUser = rs.getBoolean(6);
                        pd.distanceAway = rs.getString(7);
                        pd.timeAway = rs.getString(8);

                        res.data.add(pd);
                    }
                    else //get members
                    {
                        PanicMember pm = new PanicMember();

                        pm.name = rs.getString(1);
                        pm.isAccepted = rs.getInt(2);
                        pm.userID = rs.getInt(3);
                        res.members.add(pm);
                    }

                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }

            res.isPanicReleased = cs.getBoolean(7);
            res.imgUrl = cs.getString(8).trim().length() > 0
                    ? (Constants.IMAGE_URL + cs.getString(8))
                    : cs.getString(8).trim();

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

    public static ApiResponse panicRelease(PanicReleaseRequest req)
    {
        ApiResponse res = new ApiResponse();
        ArrayList<String> deviceList = new ArrayList<String>();
        String panicUserName = "";
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.PANIC_RELEASE);
            cs.setString(1, req.deviceSession);
            cs.setInt(2, req.panicID);
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            ResultSet rs = cs.executeQuery();
            panicUserName = cs.getString(3);

            while(rs.next())
            {
                deviceList.add( rs.getString(1));
            }

            cs.close();
            conn.close();

            if(deviceList.size() > 0)
            {
                JSONObject data = new JSONObject();

                data.put("PanicId", req.panicID);
                String message = "[" + panicUserName + "] released the panic.";
                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.PANIC_RELEASE, deviceList, data);
            }
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

    public static PanicActiveDataResponse getAllActivePanic(DeviceSessionEntity req)
    {
        PanicActiveDataResponse res = new PanicActiveDataResponse();

        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.PANIC_ALL_ACTIVE);
            cs.setString(1, req.deviceSession);

            ResultSet rs = cs.executeQuery();
            res.panics = new ArrayList<PanicActiveData>();

            while(rs.next())
            {
                //LocationID, Name, Latitude, Longitude, Radius
                PanicActiveData notification = new PanicActiveData();
                notification.panicID = rs.getInt(1);
                notification.createdOn = rs.getTimestamp(2);
                notification.panicImage = rs.getString(3).trim().length() > 0
                        ? (Constants.IMAGE_URL + rs.getString(3))
                        : rs.getString(3).trim();
                notification.add = rs.getString(4);
                notification.userID = rs.getInt(5);
                notification.panicUserName = rs.getString(6);
                notification.userImage = rs.getString(7).trim().length() > 0
                        ? (Constants.IMAGE_SHORT_URL + rs.getString(7))
                        : rs.getString(7).trim();
                notification.isAcc = rs.getInt(8);
                notification.lat = rs.getString(9);
                notification.lng = rs.getString(10);

                res.panics.add(notification);
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
