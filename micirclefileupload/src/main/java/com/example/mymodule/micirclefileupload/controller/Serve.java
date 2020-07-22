package com.example.mymodule.micirclefileupload.controller;

/**
 * Created by kamaldua on 08/06/2015.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class Serve extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BlobKey blobKey = new BlobKey(req.getParameter("imageUrl"));
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
        String servingUrl = imagesService.getServingUrl(servingOptions);

        resp.sendRedirect((servingUrl));
        //blobstoreService.serve(blobKey, resp);
    }
}
