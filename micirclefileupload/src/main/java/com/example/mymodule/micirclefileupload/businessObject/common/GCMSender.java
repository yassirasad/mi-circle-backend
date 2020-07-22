package com.example.mymodule.micirclefileupload.businessObject.common;

import com.example.mymodule.micirclefileupload.dataLayer.common.ConnectionManager;
import com.example.mymodule.micirclefileupload.dataLayer.common.Constants;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.StoreProcedures;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by kamaldua on 06/04/2016.
 */
public class GCMSender {
    public static void sendData(String  apiKey, int notificationType, ArrayList<String> toList, JSONObject data ) throws JSONException
    {
        String gcmMessage = "";
        JSONObject jGcmData = new JSONObject();
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            //Put Notification type in data
            data.put("MiType", notificationType);
            // Where to send GCM message.
            jGcmData.put("registration_ids", toList);
            // What to send in GCM message.
            jGcmData.put("data", data);

            // Create connection to send GCM/FCM Message request.
            // URL url = new URL("https://android.googleapis.com/gcm/send");
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            gcmMessage = resp;
            //System.out.println(resp);
            //System.out.println("Check your device/emulator for notification or logcat for " +
            //        "confirmation of the receipt of the GCM message.");
        } catch (Exception e) {
            /*System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();*/
            gcmMessage = e.getMessage();
        }

        try
        {
            Connection conn = ConnectionManager.getConnection();

            String log = StoreProcedures.UserProfile.GCM_LOG;
            CallableStatement cs = conn.prepareCall(log);
            cs.setString(1, jGcmData.toString());
            cs.setString(2, gcmMessage.toString());

            cs.executeQuery();

        } catch (Exception e) {
        }
    }
}
