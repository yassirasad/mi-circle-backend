package com.example.mymodule.micirclefileupload.dataLayer.panic;

import com.example.mymodule.micirclefileupload.businessObject.common.GCMSender;
import com.example.mymodule.micirclefileupload.businessObject.common.NotificationType;
import com.example.mymodule.micirclefileupload.businessObject.common.PushNotification;
import com.example.mymodule.micirclefileupload.businessObject.panic.PanicInitiateRequest;
import com.example.mymodule.micirclefileupload.businessObject.panic.PanicResponse;


import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.common.Constants;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by kamaldua on 02/03/2016.
 */
public class DLPanic {
    public static PanicResponse panicInitiate(PanicInitiateRequest details) throws Exception
    {
        PanicResponse msg = new PanicResponse();
        ArrayList<String> deviceList = new ArrayList<String>();
        String panicUserName = "";
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.PANIC_INITIATE;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, details.deviceSession.trim());
            cs.setString(2, details.blobKey.trim());
            cs.setDouble(3, details.latitude);
            cs.setDouble(4, details.longitude);
            cs.setString(5, details.address.trim());

            cs.registerOutParameter(6, java.sql.Types.VARCHAR);

            boolean results = cs.execute();

            panicUserName = cs.getString(6);

            int rsCount = 0;

            while (results) {
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    if(rsCount == 0)
                    {
                        msg.panicID = rs.getInt(1);
                        msg.panicLocationID = rs.getInt(2);
                        msg.userID = rs.getInt(3);
                        msg.latitude = rs.getDouble(4);

                        msg.longitude =  rs.getDouble(5);
                        msg.createdOn = rs.getTimestamp(6);
                        msg.isPanicUser = rs.getBoolean(7);
                        msg.imageUrl = rs.getString(8).trim().length() > 0
                                ? (Constants.IMAGE_SHORT_URL + rs.getString(8))
                                : rs.getString(8).trim();
                        msg.userName = rs.getString(9).trim();
                    }
                    else if(rsCount == 1)
                    {
                        deviceList.add(rs.getString(1));
                    }
                }
                rs.close();

                //Check for next result set
                results = cs.getMoreResults();
                rsCount = rsCount+1;
            }
            cs.close();
            conn.close();

            if(deviceList.size() > 0)
            {
                //PushNotification.sendData(Constants.API_KEY,deviceList);
                JSONObject data = new JSONObject();

                data.put("PanicId", msg.panicID);
                String message = "[" + panicUserName + "] raised a panic.";
                data.put("message", message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.PANIC, deviceList, data);
            }
        }
        catch(Exception ex)
        {
           throw ex;
        }
        return msg;
    }
}
