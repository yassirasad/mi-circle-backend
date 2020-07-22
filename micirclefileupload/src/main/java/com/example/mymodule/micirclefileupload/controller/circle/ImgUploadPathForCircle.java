package com.example.mymodule.micirclefileupload.controller.circle;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import org.json.JSONObject;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kamaldua on 11/20/2015.
 */
public class ImgUploadPathForCircle extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        PrintWriter out;
        try {
            out = resp.getWriter();
            //"uploaded" is another servlet which will send UploadUrl and blobkey to android client
            String blobUploadUrl = blobstoreService.createUploadUrl("/uploadImgCircle");

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

}
