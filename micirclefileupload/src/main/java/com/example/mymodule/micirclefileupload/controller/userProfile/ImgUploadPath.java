package com.example.mymodule.micirclefileupload.controller.userProfile;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 08/06/2015.
 */

public class ImgUploadPath extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        PrintWriter out;
        try {
            out = resp.getWriter();
            //"uploaded" is another servlet which will send UploadUrl and blobkey to android client
            String blobUploadUrl = blobstoreService.createUploadUrl("/upload");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            JSONObject json = new JSONObject();
            json.put("status", 1);
            json.put("message", "successful");
            json.put("uploadUrl", blobUploadUrl);
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

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            List<BlobKey> blobs = blobstoreService.getUploads(req).get("file");
            BlobKey blobKey = blobs.get(0);

            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

            String servingUrl = imagesService.getServingUrl(servingOptions);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            JSONObject json = new JSONObject();

            json.put("servingUrl", servingUrl);
            json.put("blobKey", blobKey.getKeyString());

            PrintWriter out = resp.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        catch (Exception ex)
        {

        }
    }
}
