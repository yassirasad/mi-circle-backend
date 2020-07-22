package com.example.mymodule.micirclefileupload.dataLayer.feed;

import com.example.mymodule.micirclefileupload.businessObject.feed.FeedImage;
import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

/**
 * Created by Yassir on 04-Sep-18.
 */

public class DLFeed {
    public static String imageUpload(FeedImage details) throws Exception
    {
        String oldBlobKey = "";
        try
        {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_IMAGE_UPLOAD);
            cs.setString(1, details.deviceSession.trim());
            cs.setInt(2, details.feedId);
            cs.setString(3, details.blobKey.trim());
            cs.registerOutParameter(4, Types.VARCHAR);
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
