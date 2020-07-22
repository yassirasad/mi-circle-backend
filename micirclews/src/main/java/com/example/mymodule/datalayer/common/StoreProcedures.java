package com.example.mymodule.datalayer.common;

/**
 * Created by Yassir on 07/25/2017.
 */
public final class StoreProcedures {

    public static class UserProfile
    {
        public static final String USER_CONTACT_NO_REGISTER = "{CALL db_micircle.P_ContactNoRegister(?, ?, ?)}";
        public static final String USER_CONTACT_NO_VERIFY = "{CALL db_micircle.P_ContactNoVerify(?, ?, ?)}";
        public static final String USER_SIGNUP = "{CALL db_micircle.P_UserSignUp(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        public static final String USER_VERIFICATION = "{CALL db_micircle.P_UserVerification(?, ?, ?, ?, ?, ?, ?, ?)}";
        public static final String REFERRAL_CODE_VALIDATION = "{CALL db_micircle.P_ReferralCodeValidate(?, ?)}";
        public static final String USER_LOGIN = "{CALL db_micircle.P_UserLogin(?, ?, ?, ?, ?, ?,?,?)}";
        public static final String USER_GET_PROFILE_INFO = "{CALL db_micircle.P_UserGetProfile_V1(?)}";
        public static final String USER_SET_PROFILE_INFO = "{CALL db_micircle.P_UserSetProfile(?,?,?,?,?,?,?,?,?,?)}";
        public static final String USER_PASSWORD_FORGET = "{CALL db_micircle.P_UserResetPasswordRequest(?,?,?,?)}";
        public static final String USER_PASSWORD_RESET_OTP_VER = "{CALL db_micircle.P_UserResetPasswordOTPVerification(?,?)}";
        public static final String USER_PASSWORD_RESET_NEW_PASS = "{CALL db_micircle.P_UserResetPassword(?,?)}";
        public static final String MEMBER_GET_PROFILE = "{CALL db_micircle.P_UserProfileForMember(?,?)}";
        public static final String USER_PASSWORD_NEW = "{CALL db_micircle.P_UserPasswordChange(?,?,?)}";
        public static final String USER_IMAGE_GET = "{CALL db_micircle.P_ImageGet(?,?,?,?)}";
        public static final String USER_IMAGE_DELETE = "{CALL db_micircle.P_UserImageDelete(?,?,?,?)}";
        public static final String USER_STATUS_TEXT_GET = "{CALL db_micircle.P_UserStatusTextGet(?,?)}";
        public static final String USER_STATUS_TEXT_SET = "{CALL db_micircle.P_UserStatusTextSet(?,?)}";

        public static final String USER_PASSWORD_FORGOT = "{CALL db_micircle.P_UserPasswordForgot(?,?,?)}";
        public static final String USER_PASSWORD_RESET = "{CALL db_micircle.P_UserPasswordReset(?,?,?,?)}";

        public static final String USER_EMAIL_CHANGE = "{CALL db_micircle.P_UserEmailChange(?,?)}";
        public static final String USER_EMAIL_VERIFY = "{CALL db_micircle.P_UserEmailVerify(?,?,?)}";
        public static final String USER_EMAIL_OTP_VERIFY = "{CALL db_micircle.P_UserEmailOTPVerify(?,?)}";

        //For  GCM
        public static final String USER_GCM_SET = "{CALL db_micircle.P_UserSetGCMId(?, ?)}";
        public static final String USER_GCM_GET = "{CALL db_micircle.P_UserGetGCMId(?, ?)}";

        //For User Contacts
        public static final String REGISTERD_CONTACTS = "{CALL db_micircle.P_ContactsRegisterd(?, ?)}";

        //For circle
        public static final String USER_CIRCLES = "{CALL db_micircle.P_CircleGet(?)}";
        public static final String CIRCLE_CREATE = "{CALL db_micircle.P_CircleCreate(?,?,?,?,?,?,?,?,?,?,?)}";
        public static final String CIRCLE_DELETE = "{CALL db_micircle.P_CircleDelete(?,?,?)}";
        public static final String CIRCLE_DELETE_MEMBER = "{CALL db_micircle.P_CircleDeleteMember(?,?,?,?,?,?,?,?)}";
        public static final String CIRCLE_LEAVE = "{CALL db_micircle.P_CircleLeave(?,?,?,?,?,?)}";
        public static final String CIRCLE_MEMBERS = "{CALL db_micircle.P_MemberGetCircles(?,?)}";

        public static final String CIRCLE_ADD_MEMBER = "{CALL db_micircle.P_CircleAddMember(?,?,?,?,?,?,?)}";
        public static final String CIRCLE_ADD_MULTIPLE_MEMBER = "{CALL db_micircle.P_CircleAddMultipleMember(?,?,?,?,?,?,?,?)}";

        public static final String CIRCLE_LOCATION_ADD = "{CALL db_micircle.P_CircleAddLocation(?,?,?)}";
        public static final String CIRCLE_LOCATION_DELETE = "{CALL db_micircle.P_CircleDeleteLocation(?,?,?)}";

        public static final String CIRCLES_LOCATIONS_ADD = "{CALL db_micircle.P_CirclesAddLocations(?,?,?)}";
        public static final String CIRCLES_LOCATIONS_DELETE = "{CALL db_micircle.P_CirclesDeleteLocations(?,?,?)}";

        public static final String CIRCLES_LOCATIONS_UNSHARE = "{CALL db_micircle.P_CircleMemberLocationTrack(?,?,?)}";

        public static final String CIRCLE_DELETE_AFTER_AGE_CROBJOB = "{CALL db_micircle.P_CircleDeleteAfterAgeCrobJob()}";
        public static final String CIRCLE_AGE_UPDATE = "{CALL db_micircle.P_CircleAgeUpdate(?,?,?)}";
        //For location
        public static final String LOCATION_ADD = "{CALL db_micircle.P_LocationAdd(?,?,?,?,?,?)}";
        public static final String LOCATION_GET = "{CALL db_micircle.P_LocationGet(?)}";
        public static final String LOCATION_DELETE = "{CALL db_micircle.P_LocationDelete(?,?)}";

        public static final String LOCATION_ACTIVITY_GET = "{CALL db_micircle.P_LocationActivityGet(?,?,?)}";
        public static final String LOCATION_ACTIVITY_SET = "{CALL db_micircle.P_LocationActivitySet(?,?,?)}";
        public static final String LOCATION_GET_ALL = "{CALL db_micircle.P_LocationAllActivityGet(?,?)}";

        //For friend
        public static final String FRIEND_ADD = "{CALL db_micircle.P_FriendRequest(?,?)}";
        public static final String FRIEND_ADD_WITHID = "{CALL db_micircle.P_FriendRequestWithUserID(?,?)}";
        public static final String FRIEND_GET = "{CALL db_micircle.P_FriendGet(?)}";
        public static final String FRIEND_SEARCH = "{CALL db_micircle.P_FindFriend(?,?,?,?)}";
        public static final String FRIEND_SUGGESTION = "{CALL db_micircle.P_FriendSuggestionGet_V1(?)}";
        public static final String FRIEND_IGNORE = "{CALL db_micircle.P_FriendSuggestionIgnore(?,?)}";

        public static final String FRIEND_LOCATION_ADD = "{CALL db_micircle.P_FriendLocationAdd(?,?,?)}";
        public static final String FRIEND_LOCATION_DELETE = "{CALL db_micircle.P_FriendLocationDelete(?,?,?)}";

        public static final String FRIENDS_LOCATIONS_ADD = "{CALL db_micircle.P_FriendsLocationsAdd_V1(?,?,?,?,?)}";
        public static final String FRIENDS_LOCATIONS_DELETE = "{CALL db_micircle.P_FriendsLocationsDelete_V1(?,?,?,?,?)}";
        public static final String FRIEND_LOCATION_LEAVE = "{CALL db_micircle.P_LocationLeave(?,?,?,?)}";

        public static final String FRIEND_BLOCK = "{CALL db_micircle.P_FriendBlock(?,?)}";
        public static final String FRIEND_UNBLOCK = "{CALL db_micircle.P_FriendUnBlock(?,?)}";
        public static final String FRIENDS_REMOVE = "{CALL db_micircle.P_FriendRemove(?,?,?,?,?,?)}";

        //For Notification
        public static final String NOTIFICATION_GET = "{CALL db_micircle.P_NotificationGet(?)}";
        public static final String NOTIFICATION_RESPONSE = "{CALL db_micircle.P_NotificationResponse_V1(?,?,?,?,?,?,?)}";
        public static final String NOTIFICATION_GET_ALL = "{CALL db_micircle.P_NotificationGetAll(?)}";
        public static final String LAST_ACTIVE_STATUS_UPDATE = "{CALL db_micircle.P_SetLastActiveStatus(?)}";
        public static final String NOTIFICATION_FRIEND_RESPONSE = "{CALL db_micircle.P_NotificationFriendAddResponse(?,?,?)}";
        public static final String NOTIFICATION_CIRCLE_RESPONSE = "{CALL db_micircle.P_NotificationCircleMemberAddResponse(?,?,?,?)}";

        public static final String MESSAGE_GET = "{CALL db_micircle.P_MessagesGet(?,?,?)}";

        //For Panic
        public static final String PANIC_RESPOND = "{CALL db_micircle.P_PanicRespond(?,?,?,?,?)}";
        public static final String PANIC_DATA = "{CALL db_micircle.P_PanicGet(?,?,?,?,?,?,?,?)}";
        public static final String PANIC_RELEASE = "{CALL db_micircle.P_PanicRelease(?,?,?)}";
        public static final String PANIC_ALL_ACTIVE = "{CALL db_micircle.P_PanicAllActiveGet(?)}";

        public static final String GCM_LOG =  "{CALL db_micircle.P_GCMLog(?, ?)}";

        public static final String OPENFIRE_CALLBACK_OFFLINE =  "{CALL db_micircle.P_OpenfireCallbackOnOffline(?,?,?)}";

        public static final String OTP_URL = "{CALL db_micircle.P_OTPGetURL(?)}";

        public static final String INACTIVE_USERS_CRONJOB = "{CALL db_micircle.P_InactiveUsers()}";
        public static final String REGISTERED_CONTACTS_SUGGESTION = "{CALL db_micircle.P_RegisteredContactsSuggestion(?)}";

        public static final String FEED_LOGIN = "{CALL db_micircle.P_FeedLogin(?,?,?)}";
        public static final String FEED_CATEGORIES = "{CALL db_micircle.P_FeedCategoryGet()}";
        public static final String FEED_BLOBKEY = "{CALL db_micircle.P_FeedBlobKey(?,?)}";
        public static final String FEED_CREATE = "{CALL db_micircle.P_FeedCreate(?,?,?,?,?,?,?,?)}";
        public static final String FEED_EDIT = "{CALL db_micircle.P_FeedEdit(?,?,?,?,?,?,?,?,?,?,?)}";
        public static final String FEED_DELETE = "{CALL db_micircle.P_FeedDelete(?,?)}";
        public static final String FEED = "{CALL db_micircle.P_Feed(?,?,?)}";
        public static final String FEEDS = "{CALL db_micircle.P_Feeds(?,?,?)}";
        public static final String FEED_LIKE_DISLIKE = "{CALL db_micircle.P_FeedLikeDislike(?,?,?)}";
        public static final String FEED_REPORT = "{CALL db_micircle.P_FeedReport(?,?)}";
        public static final String COUNTRIES = "{CALL db_micircle.P_Countries()}";
    }
}
