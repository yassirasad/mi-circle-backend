package com.example.mymodule.micirclefileupload.controller.notification;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUploadResponse;
import com.example.mymodule.micirclefileupload.businessObject.location.LocationImage;
import com.example.mymodule.micirclefileupload.businessObject.notification.MessageFormatOfCircle;
import com.example.mymodule.micirclefileupload.businessObject.notification.MessageFormatOfFriend;
import com.example.mymodule.micirclefileupload.businessObject.notification.Notification;
import com.example.mymodule.micirclefileupload.dataLayer.common.ApiResponse;
import com.example.mymodule.micirclefileupload.dataLayer.location.DLLocation;
import com.example.mymodule.micirclefileupload.dataLayer.notification.DLNotification;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 01/16/2016.
 */
public class ImgUploadNotification extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //ImageUploadResponse res = new ImageUploadResponse();
        MessageFormatOfCircle msgCircle = new MessageFormatOfCircle();
        MessageFormatOfFriend msgFrnd = new MessageFormatOfFriend();
        String json = "";

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        Gson gson = new Gson();
        PrintWriter out = resp.getWriter();
        try {
            String deviceSession = req.getParameter("deviceSession");
            String receiverID =  req.getParameter("receiverID"); //FriendUserID or CircleID
            String messageFor = req.getParameter("messageFor"); //1 for Friend, 2 for Circle
            String messageType = req.getParameter("messageType"); //1 Text, 2 Media
            //String message = req.getParameter("message");

            /*
             V_deviceSession VARCHAR(50),
              V_ID INT UNSIGNED, -- FriendUserID or CircleID
              V_MessageFor TINYINT(4), -- 1 for Friend, 2 for Circle
              V_MessageType TINYINT(4), -- 1 Text, 2 Media
              V_Message VARCHAR(1000)
             */
            Notification msgObj  = new Notification();
            msgObj.deviceSession = deviceSession;
            msgObj.receiverID = receiverID;
            msgObj.messageFor = messageFor;
            msgObj.messageType = messageType;

            BlobKey blobKey = null;
            if(messageType.equals("1"))
            {
                msgObj.message = req.getParameter("message");
            }
            else
            {
                List<BlobKey> blobs = blobstoreService.getUploads(req).get("message");
                blobKey = blobs.get(0);
                msgObj.message = blobKey.getKeyString();

                ImagesService imagesService = ImagesServiceFactory
                        .getImagesService();
                ServingUrlOptions servingOptions = ServingUrlOptions.Builder
                        .withBlobKey(blobKey);
                String servingUrl = imagesService.getServingUrl(servingOptions);
            }

            if(messageFor.equals("1")) //for friend
            {
                msgFrnd = DLNotification.messageSaveForFriend(msgObj);
                json = gson.toJson(msgFrnd);
            }
            else if(messageFor.equals("2")) //for friend
            {
                msgCircle = DLNotification.messageSaveForCircle(msgObj);
                json = gson.toJson(msgCircle);
            }

            out.print(json.toString());
            out.flush();
            out.close();
        }
        catch (Exception ex)
        {
            ApiResponse res = new ApiResponse();
            res.status = 0;
            res.message = ex.getMessage();
            json = gson.toJson(res);
            out.print(json.toString());
            out.flush();
            out.close();

        }
    }
}
