package com.example.mymodule.micirclefileupload.dataLayer.circle;

import com.example.mymodule.micirclefileupload.businessObject.circle.CircleImage;
import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;

/**
 * Created by kamaldua on 08/22/2015.
 */
public class DLCircle {
    public static String imageUpload(CircleImage details) throws Exception
    {
        String oldBlobKey = "";
        try
        {
            Connection conn = ConnectionManager.getConnection();

            String signUp = StoreProcedures.UserProfile.CIRCLE_IMAGE_UPLOAD;
            CallableStatement cs = conn.prepareCall(signUp);
            cs.setString(1, details.deviceSession.trim());
            cs.setString(2, details.circleUniqueID.trim());
            cs.setString(3, details.blobKey.trim());
            cs.registerOutParameter(4,  java.sql.Types.VARCHAR);

            cs.executeUpdate();
            oldBlobKey = cs.getString(4);
            cs.close();
            conn.close();

        }
        catch(Exception ex)
        {
            throw ex;
        }
        return oldBlobKey;
    }
}
