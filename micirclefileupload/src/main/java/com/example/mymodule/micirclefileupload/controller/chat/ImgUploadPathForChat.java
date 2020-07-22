package com.example.mymodule.micirclefileupload.controller.chat;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUploadResponse;
import com.example.mymodule.micirclefileupload.businessObject.chat.MessageResponse;
import com.example.mymodule.micirclefileupload.businessObject.location.LocationImage;
import com.example.mymodule.micirclefileupload.dataLayer.location.DLLocation;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;
import com.google.appengine.repackaged.com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 01/27/2017.
 */
public class ImgUploadPathForChat extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        PrintWriter out;
        try {
            out = resp.getWriter();
            //"uploaded" is another servlet which will send UploadUrl and blobkey to android client
            String blobUploadUrl = blobstoreService.createUploadUrl("/uploadImgChat");
            String blobUploadThumbUrl = blobstoreService.createUploadUrl("/uploadImgChat");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            JSONObject json = new JSONObject();
            json.put("status", 1);
            json.put("message", "successful");
            json.put("url", blobUploadUrl);
            json.put("urlThumb", blobUploadThumbUrl);
            out.print(json.toString());
        }
        catch (Exception ex)
        {
            try {
                out = resp.getWriter();
                JSONObject json = new JSONObject();
                json.put("status", 0);
                json.put("message", ex.getMessage());
                out.print(json.toString());
            }
            catch (Exception e) {
            }
        }

    }
}
