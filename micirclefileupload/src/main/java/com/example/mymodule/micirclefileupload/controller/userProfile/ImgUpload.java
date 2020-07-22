package com.example.mymodule.micirclefileupload.controller.userProfile;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUpload;
import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUploadResponse;
import com.example.mymodule.micirclefileupload.dataLayer.userProfile.DLUser;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
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
 * Created by kamaldua on 08/06/2015.
 */

public class ImgUpload extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ImageUploadResponse res = new ImageUploadResponse();
        try {
            String deviceSession = req.getParameter("deviceSession");
            ImageUpload imgDetails  = new ImageUpload();
            imgDetails.deviceSession = deviceSession;
            List<BlobKey> blobs = blobstoreService.getUploads(req).get("file");
            BlobKey blobKey = blobs.get(0);

            imgDetails.blobKey = blobKey.getKeyString();

            String oldBlobKey = DLUser.imageUpload(imgDetails);

            //delete old uploaded file
            if(oldBlobKey.trim().length() > 0)
            {
                blobstoreService.delete(new BlobKey(oldBlobKey));
            }

            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String servingUrl = imagesService.getServingUrl(servingOptions);

            URL baseUrl = new URL((req.getRequestURL().toString() + "/"));
            URL url = new URL( baseUrl , ("../serve?imageUrl=" + blobKey.getKeyString()));

            res.status = 1;
            res.message = "success";
            res.imageUrl = url.toString();

            //JSONObject json = new JSONObject();
            //json.put("imageUrl", url);

            //Added by Yassir
            DLUser.notifyImgChangeToFriends(imgDetails.deviceSession, res.imageUrl);

        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();

        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        Gson gson = new Gson();
        String json = gson.toJson(res);

        PrintWriter out = resp.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }
}
