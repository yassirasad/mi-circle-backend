package com.example.mymodule.micirclefileupload.controller.chat;

import com.example.mymodule.micirclefileupload.businessObject.chat.MessageResponse;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 01/27/2017.
 */
public class ImageUploadForChat extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        MessageResponse response = new MessageResponse();
        try {
            String fileType = req.getParameter("type");
            /*String deviceSession = req.getParameter("deviceSession");
            String locationID = req.getParameter("locationId");

            LocationImage imgDetails  = new LocationImage();
            imgDetails.deviceSession = deviceSession;
            imgDetails.locationID = locationID;*/

            List<BlobKey> blobs = blobstoreService.getUploads(req).get("file");
            BlobKey blobKey = blobs.get(0);

            String imageUrl = blobKey.getKeyString();
            ImagesService imagesService = ImagesServiceFactory.getImagesService();

            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
            String servingUrl = imagesService.getServingUrl(servingOptions);

            BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
            BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(new BlobKey(servingUrl));
            Long blobSize = blobInfo.getSize();

            String thumbUrl = "";
            if(fileType == "jpg" || fileType == "jpeg" || fileType == "png" || fileType == "gif")
            {
                thumbUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey).crop(true).imageSize(300));
            }
            else if(fileType == "video")
            {

            }
            response.url = servingUrl;
            response.type = fileType;
            response.thumb = thumbUrl;
            response.file_size = blobSize.toString();
        }
        catch (Exception ex)
        {

        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        Gson gson = new Gson();
        String json = gson.toJson(response);

        PrintWriter out = resp.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }
}
