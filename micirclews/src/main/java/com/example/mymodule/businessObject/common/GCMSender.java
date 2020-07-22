package com.example.mymodule.businessObject.common;

import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by kamaldua on 06/07/2016.
 */

public class GCMSender {

    public static void sendData(String  apiKey, int notificationType, ArrayList<String> toList, JSONObject data ) throws JSONException
    {
        //Runnable r = new GCMSenderThread(apiKey, notificationType, toList, data);
        //new Thread(r).start();

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
            jGcmData.put("content_available", true);
            jGcmData.put("priority", "high");

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
            gcmMessage = IOUtils.toString(inputStream);
        } catch (Exception e) {
            gcmMessage = e.getMessage();
        }

        try
        {
            Connection conn = ConnectionManager.getConnection();

            String log = StoreProcedures.UserProfile.GCM_LOG;
            CallableStatement cs = conn.prepareCall(log);
            cs.setString(1, jGcmData.toString());
            cs.setString(2, gcmMessage);

            cs.executeQuery();

        } catch (Exception e) {
        }
    }
}
