package com.example.mymodule.micirclefileupload.controller.panic;

import com.example.mymodule.micirclefileupload.businessObject.UserProfile.ImageUploadResponse;
import com.example.mymodule.micirclefileupload.businessObject.circle.CircleImage;
import com.example.mymodule.micirclefileupload.businessObject.panic.PanicInitiateRequest;
import com.example.mymodule.micirclefileupload.businessObject.panic.PanicResponse;
import com.example.mymodule.micirclefileupload.dataLayer.circle.DLCircle;
import com.example.mymodule.micirclefileupload.dataLayer.panic.DLPanic;
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
 * Created by kamaldua on 02/03/2016.
 */
public class PanicInitiate extends HttpServlet {
    BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PanicResponse res  = new PanicResponse();
        try {

            String deviceSession = req.getParameter("deviceSession");
            String latitude = req.getParameter("latitude");
            String longitude = req.getParameter("longitude");
            String address = req.getParameter("address");

            PanicInitiateRequest panicDetails  = new PanicInitiateRequest();
            panicDetails.deviceSession = deviceSession;
            panicDetails.latitude = Double.parseDouble(latitude);
            panicDetails.longitude = Double.parseDouble(longitude);
            panicDetails.address = address;

            List<BlobKey> blobs = blobstoreService.getUploads(req).get("file");
            BlobKey blobKey = blobs.get(0);

            panicDetails.blobKey = blobKey.getKeyString();

            ImagesService imagesService = ImagesServiceFactory
                    .getImagesService();
            ServingUrlOptions servingOptions = ServingUrlOptions.Builder
                    .withBlobKey(blobKey);
            String servingUrl = imagesService.getServingUrl(servingOptions);

            res = DLPanic.panicInitiate(panicDetails);

            res.status = 1;
            res.message = "success";
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
