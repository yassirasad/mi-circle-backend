package com.example.mymodule.businessObject.common;

/**
 * Created by kamaldua on 08/10/2015.
 */
public final class Constants {

    public static String IMAGE_URL  ="https://mi-circle-image-upload.appspot.com/serve?imageUrl=";
    public static String IMAGE_SHORT_URL  ="https://mi-circle-image-upload.appspot.com/serveShort?imageUrl=";

    public static String SMTP_HOST  = "smtp.stopnsolve.com";
    public static String SMTP_PORT  = "587";
    public static String SMTP_MAIL_ADD  = "admin@stopnsolve.com";
    public static String SMTP_MAIL_NAME = "mi-Circle";
    public static String SMTP_USER_NAME  = "mi-Circle";
    public static String SMTP_USER_PASS  = "micircle@123";

    public static String XMPP_URL = "104.155.199.27";
    public static int XMPP_PORT = 9090;
    public static String XMPP_AUTH_TOKEN = "06Yp7LR7Wj9eqLIf";

    // public static String API_KEY = "AIzaSyDkh9PYMdp9QbSIJG6-N0uma6-LoOOJ2jc";           //GCM key
    public static String API_KEY = "AAAAAYyHchM:APA91bHQcLHvo4TqPqqKtlSG_7VeuJvqJKKC9dd1Mjqfjo" +
            "RU8oxSVKqo8JHSirIFT701o7hV8Ao6332Annnzjoa5OCB_laFANc0DLsB4dtE_bMBXiKMaRxMhC0YzyhPNPH01EJw4RNVR";    //FCM key
    public static String GOOGLE_API_KEY = "AIzaSyC_lMhD2e9aGCIaiLbVqy3k9Zi8yDu5tv0";       //mi-circle key

    public static String getGroupName(String contactNo)
    {
        String gpName = contactNo + "-" + "group";
        return gpName;
    }

    public static String getMemberWithURL(String contactNo)
    {
        String member = (contactNo + "@" + XMPP_URL);
        return member;
    }
}
