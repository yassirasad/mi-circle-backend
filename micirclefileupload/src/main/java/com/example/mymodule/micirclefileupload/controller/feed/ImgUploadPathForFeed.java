package com.example.mymodule.micirclefileupload.controller.feed;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import org.json.JSONObject;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yassir on 04-Sep-18.
 */

public class ImgUploadPathForFeed extends HttpServlet{
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        PrintWriter out;
        JSONObject json = new JSONObject();
        try {
            out = resp.getWriter();
            //"uploaded" is another servlet which will send UploadUrl and blobkey to android client
            String blobUploadUrl = blobstoreService.createUploadUrl("/uploadImgFeed");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            json.put("status", 1);
            json.put("message", "successful");
            json.put("uploadUrl", blobUploadUrl);
            out.print(json);
        }
        catch (Exception ex)
        {
            try {
                out = resp.getWriter();
                json.put("status", 0);
                json.put("message", ex.getMessage());
                out.print(json);
            }
            catch (Exception e) {
            }
        }

    }
}
