package com.example.mymodule.micirclefileupload.dataLayer.userProfile;

/**
 * Created by kamaldua on 08/09/2015.
 */
public class StoreProcedures {
    public static class UserProfile
    {
        public static final String USER_IMAGE_UPLOAD = "{CALL db_micircle.P_UserUploadImage(?, ?, ?)}";

        public static final String CIRCLE_IMAGE_UPLOAD = "{CALL db_micircle.P_CircleUploadImage(?, ?, ?, ?)}";

        public static final String LOCATION_IMAGE_UPLOAD = "{CALL db_micircle.P_LocationUploadImage(?, ?, ?, ?)}";

        public static final String FEED_IMAGE_UPLOAD = "{CALL db_micircle.P_FeedUploadImage(?,?,?,?)}";

        public static final String MESSAGE_SEND =  "{CALL db_micircle.P_MessageSend(?, ?, ?, ?, ?)}";

        public static final String PANIC_INITIATE =  "{CALL db_micircle.P_PanicInitiate(?, ?, ?, ?, ?, ?)}";

        public static final String FRIENDS_GCM_IDS = "{CALL db_micircle.P_FriendsGetGCMIds(?,?,?)}";

        public static final String GCM_LOG =  "{CALL db_micircle.P_GCMLog(?, ?)}";

    }
}
