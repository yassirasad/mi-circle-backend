package com.example.mymodule.datalayer;

import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Random;
import java.text.MessageFormat;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by Yassir on 19-Jan-18.
 */

public class OTP {
    public final static int REGISTRATION = 1;
    public final static int PASSWORD = 2;

    public static String generateOTP(){
        Random rand = new Random();
        int n = rand.nextInt(10000); // returns integer from 0 to 9999.

        String otp;
        if (n==0)
            otp = "0000";
        else if (n<10)
            otp = "000"+n;
        else if (n<100)
            otp = "00"+n;
        else if (n<1000)
            otp = "0"+n;
        else
            otp = Integer.toString(n);

        return otp;
    }

    public static void sendOTP(String mobile, String otp, int otpType) {
        /* String link = "http://smsgateway.me/api/v3/messages/send/variable?" +
                "email=viki73bhardwaj@gmail.com&password=rubryxnet123&device=76140&number={0}&message={1}";
        */
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.OTP_URL);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeQuery();
            String otpURL = cs.getString(1);
            cs.close();
            conn.close();

            String message;
            if (otpType == REGISTRATION)
                message = "{0}%20is%20the%20OTP%20for%20your%20mi-Circle%20registration";
            else if (otpType == PASSWORD)
                message = "{0}%20is%20the%20OTP%20for%20your%20mi-Circle%20password%20reset";
            else
                message = "{0}%20is%20the%20OTP%20for%20your%20mi-Circle%20account";

            message = MessageFormat.format(message, otp);

            // "http://68.193.120.103:1688/services/api/messaging?To=+91987654321&Message=1234%20is%20the%20OTP%20for%20your%20mi-Circle%20account"
            String link = MessageFormat.format("{0}?To={1}&Message={2}", otpURL, mobile, message);

            URL url = new URL(link);

            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setRequestMethod("POST");
            httpURLConn.setReadTimeout(10000); //10 sec
            int responseCode = httpURLConn.getResponseCode();

            //URLConnection conn = url.openConnection();
            //// conn.connect();
            //conn.getInputStream();
        }
        catch (Exception ex){
        }
    }
}
