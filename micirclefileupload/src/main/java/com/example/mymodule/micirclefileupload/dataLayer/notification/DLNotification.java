package com.example.mymodule.micirclefileupload.dataLayer.notification;

import com.example.mymodule.micirclefileupload.businessObject.location.LocationImage;
import com.example.mymodule.micirclefileupload.businessObject.notification.MessageFormatOfCircle;
import com.example.mymodule.micirclefileupload.businessObject.notification.MessageFormatOfFriend;
import com.example.mymodule.micirclefileupload.businessObject.notification.Notification;
import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.common.Constants;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by kamaldua on 01/16/2016.
 */
public class DLNotification {

    public static MessageFormatOfCircle messageSaveForCircle(Notification details) throws Exception
    {
        MessageFormatOfCircle msg = new MessageFormatOfCircle();

        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.MESSAGE_SEND;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, details.deviceSession.trim());
            cs.setString(2, details.receiverID.trim());
            cs.setString(3, details.messageFor.trim());
            cs.setString(4, details.messageType.trim());
            cs.setString(5, details.message.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
               msg.msgId = rs.getInt(1);
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
            }

            cs.close();
            conn.close();

        }
        catch(Exception ex)
        {
            throw ex;
        }
        return msg;
    }

    public static MessageFormatOfFriend messageSaveForFriend(Notification details) throws Exception
    {
        MessageFormatOfFriend msg = new MessageFormatOfFriend();

        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.MESSAGE_SEND;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, details.deviceSession.trim());
            cs.setString(2, details.receiverID.trim());
            cs.setString(3, details.messageFor.trim());
            cs.setString(4, details.messageType.trim());
            cs.setString(5, details.message.trim());

            ResultSet rs = cs.executeQuery();

            while(rs.next())
            {
                msg.msgId = rs.getInt(1);
                msg.fromId = rs.getInt(2);
                msg.toId = rs.getInt(3);

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
            }

            cs.close();
            conn.close();

        }
        catch(Exception ex)
        {
           throw ex;
        }
        return msg;
    }
}
