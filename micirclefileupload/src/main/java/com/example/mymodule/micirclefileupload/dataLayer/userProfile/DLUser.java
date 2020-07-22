package com.example.mymodule.micirclefileupload.dataLayer.userProfile;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUpload;
import com.example.mymodule.micirclefileupload.businessObject.common.GCMSender;
import com.example.mymodule.micirclefileupload.businessObject.common.NotificationType;
import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.common.Constants;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by kamaldua on 08/09/2015.
 */
public class DLUser {

    public static String imageUpload(ImageUpload details) throws Exception
    {
        String oldBlobKey = "";
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.USER_IMAGE_UPLOAD;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, details.deviceSession.trim());
            cs.setString(2, details.blobKey.trim());
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.executeUpdate();

            oldBlobKey = cs.getString(3);
            cs.close();
            conn.close();

        }
        catch(Exception ex)
        {
           throw ex;
        }
        return oldBlobKey;
    }

    // * Created by Yassir on 12/May/2017
    public static void notifyImgChangeToFriends(String deviceSession, String profileImageURL)
    {
        ArrayList<String> deviceList = new ArrayList<>();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FRIENDS_GCM_IDS);
            cs.setString(1,deviceSession.trim());
            cs.registerOutParameter(2, Types.INTEGER);
            cs.registerOutParameter(3,Types.VARCHAR);

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                deviceList.add(rs.getString(1));
            }
            int userID = cs.getInt(2);
            String fullName = cs.getString(3);

            cs.close();
            conn.close();

            if(deviceList.size()>0)
            {
                JSONObject data = new JSONObject();
                data.put("friendUserID",userID);
                data.put("friendImageURL",profileImageURL);
                String message = fullName + " has changed his/her profile picture.";
                data.put("message",message);
                GCMSender.sendData(Constants.API_KEY, NotificationType.FRIEND_IMAGE_CHANGE, deviceList, data);
            }
        }

        catch (Exception ex) {}
    }

}
