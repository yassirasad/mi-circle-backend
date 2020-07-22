package com.example.mymodule.controller.feed;

import com.example.mymodule.datalayer.feed.DLFeed;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yassir on 26-Oct-18.
 */

public class ServeVideo extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String feedId = req.getParameter("fid");
        try {
            String blobKeyString = DLFeed.getFeedBlobKey(Integer.parseInt(feedId));
            if(blobKeyString == null)
                throw new Exception("Invalid blobKey");
            BlobKey blobKey = new BlobKey(blobKeyString);

            resp.setContentType("video/mp4");

            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            blobstoreService.serve(blobKey, resp);
        }
        catch(Exception ex){
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.write(ex.getMessage());
        }
    }
}
