package com.example.mymodule.businessObject.common;

import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by kamaldua on 09/14/2016.
 */
public class GCMSenderThread implements Runnable {

    String apiKey;
    int notificationType;
    ArrayList<String> toList;
    JSONObject data;

    public GCMSenderThread(String apiKeyParam, int notificationTypeParam, ArrayList<String> toListParam, JSONObject dataParam) {
        this.apiKey = apiKeyParam;
        this.notificationType = notificationTypeParam;
        this.toList = toListParam;
        this.data = dataParam;
    }

    public void run() {

        String gcmMessage = "";
        JSONObject jGcmData = new JSONObject();
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            jGcmData.put("registration_ids", toList);
            //Put Notification type in data
            data.put("MiType", notificationType);
            // What to send in GCM message.
            jGcmData.put("data", data);

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
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
