package com.example.mymodule.micirclefileupload.controller.feed;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUploadResponse;
import com.example.mymodule.micirclefileupload.businessObject.feed.FeedImage;
import com.example.mymodule.micirclefileupload.dataLayer.feed.DLFeed;
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
 * Created by Yassir on 04-Sep-18.
 */

public class ImgUploadForFeed extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ImageUploadResponse res = new ImageUploadResponse();
        try {
            String deviceSession = req.getParameter("deviceSession");
            String feedId = req.getParameter("feedId");

            List<BlobKey> blobKeys = blobstoreService.getUploads(req).get("photo");
            BlobKey blobKey = blobKeys.get(0);

            FeedImage imgDetails = new FeedImage();
            imgDetails.deviceSession = deviceSession.trim();
            imgDetails.feedId = Integer.parseInt(feedId.trim());
            imgDetails.blobKey = blobKey.getKeyString();

            String oldBlobKey = DLFeed.imageUpload(imgDetails);

            //delete old uploaded file
            if(oldBlobKey.trim().length() > 0)
            {
                blobstoreService.delete(new BlobKey(oldBlobKey));
            }

            URL baseUrl = new URL((req.getRequestURL().toString() + "/"));
            URL url = new URL( baseUrl , ("../serve?imageUrl=" + blobKey.getKeyString()));

            res.status = 1;
            res.message = "success";
            res.imageUrl = url.toString();
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
        out.print(json);
        out.flush();
        out.close();
    }
}
