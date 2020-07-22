package com.example.mymodule.controller.feed;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.BlobOperation;
import com.example.mymodule.businessObject.feed.FeedCreateRequest;
import com.example.mymodule.businessObject.feed.FeedEditRequest;
import com.example.mymodule.businessObject.feed.FeedEditResponse;
import com.example.mymodule.businessObject.feed.FeedRequest;
import com.example.mymodule.businessObject.feed.FeedResponse;
import com.example.mymodule.datalayer.feed.DLFeed;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.swagger.models.auth.In;

/**
 * Created by Yassir on 26-Dec-18.
 */

public class EditFeed extends HttpServlet{
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private final int MAX_COUNTRIES = 240;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        HttpSession session = req.getSession(false);

        try{
            String requestURI = req.getRequestURI();
            int feedID = Integer.parseInt(requestURI.substring(requestURI.lastIndexOf("/")+1));

            FeedRequest fr = new FeedRequest();
            fr.deviceSession = "2700c380-af6e-11e8-96f8-529269fb1459";
            fr.feedId = feedID;

            FeedResponse response = DLFeed.getFeed(fr);

            if(response.status == 1){
                session.setAttribute("feedId", response.feed.feedId);
                session.setAttribute("category", response.feed.categoryId);
                session.setAttribute("title", response.feed.title);
                session.setAttribute("description", response.feed.description);
            }
            else
                throw new NumberFormatException(response.message);
        }
        catch(NumberFormatException ex){
            session.setAttribute("errorMain", "Invalid Feed");
        }
        catch(Exception ex){
            session.setAttribute("errorMain", ex.getMessage());
        }

        RequestDispatcher rd = req.getRequestDispatcher("/feed_edit.jsp");
        rd.include(req, resp);

        if(session != null){
            session.removeAttribute("success");
            session.removeAttribute("error");
            session.removeAttribute("errorMain");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        HttpSession session = req.getSession(false);

        int feedId = Integer.parseInt(req.getParameter("feed-id"));
        String [] countries = req.getParameterValues("country");
        String cat = req.getParameter("category");
        String title = req.getParameter("title");
        String desc = req.getParameter("description");
        BlobKey blobKey = null;
        BlobKey blobKey2 = null;
        String blobKeyString = null;
        String blobKeyString2 = null;
        String blobContentType = null;
        String oldBlobKeyString;
        String oldBlobKeyString2;

        try{
            // Handling image/video in Blobstore.
            List<BlobKey> blobKeys = blobstoreService.getUploads(req).get("file");
            if (blobKeys == null || blobKeys.isEmpty())
                throw new Exception("file part empty");
            blobKey = blobKeys.get(0);

            //Handling video thumnail in Blobstore.
            blobKeys = blobstoreService.getUploads(req).get("t-file");
            if (blobKeys == null || blobKeys.isEmpty())
                throw new Exception("t-file part empty");
            blobKey2 = blobKeys.get(0);

            BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
            //Check the blob size, when user don't attach any file then blob size is 0, hence delete both.
            if(blobInfo.getSize() > 0){
                //Check if blob Type is image/* or video/*, else delete both blob.
                if(blobInfo.getContentType().startsWith("image/")){
                    blobContentType = "image";
                    blobKeyString = blobKey.getKeyString();
                    blobstoreService.delete(blobKey2);  //Thumbnail doesn't required for image.
                }
                else if(blobInfo.getContentType().startsWith("video/")){
                    blobContentType = "video";
                    blobKeyString = blobKey.getKeyString();
                    //Checking video thumbnail blob.
                    blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey2);
                    if(blobInfo.getSize() > 0 && blobInfo.getContentType().startsWith("image/"))
                        blobKeyString2 = blobKey2.getKeyString();
                    else
                        throw new Exception("Please attach valid video thumbnail image.");
                }
                else
                    throw new Exception("File must be image or video type");
            }
            else {
                blobstoreService.delete(blobKey);
                blobstoreService.delete(blobKey2);
            }

            if(countries == null || countries.length == 0)
                throw new Exception("Please select the country");
            if(Integer.parseInt(cat) < 1)
                throw new Exception("Please select valid category");

            FeedEditRequest fer = new FeedEditRequest();
            fer.deviceSession = "2700c380-af6e-11e8-96f8-529269fb1459";
            fer.feedId = feedId;
            fer.categoryId = Integer.parseInt(cat);
            if(title != null)
                fer.title = title.trim();
            if(desc != null)
                fer.description = desc.trim();
            fer.blobContentType = blobContentType;
            fer.blobKey = blobKeyString;
            fer.blobKey2 = blobKeyString2;
            if(countries.length >= MAX_COUNTRIES)
                fer.countryISOCodes = "all";
            else
                fer.countryISOCodes = String.join(",", countries);

            // Edit Feed
            FeedEditResponse response = DLFeed.editFeed(fer);

            if (response.status == 1){
                oldBlobKeyString = response.OldBlobKey;
                oldBlobKeyString2 = response.OldBlobKey2;

                // Delete Old Blobs, if available.
                if(!(oldBlobKeyString == null || oldBlobKeyString.equals(""))){
                    BlobOperation.deleteServingURL(oldBlobKeyString);
                    BlobOperation.deleteBlobKey(oldBlobKeyString);
                }
                if(!(oldBlobKeyString2 == null || oldBlobKeyString2.equals(""))){
                    BlobOperation.deleteServingURL(oldBlobKeyString2);
                    BlobOperation.deleteBlobKey(oldBlobKeyString2);
                }
                session.setAttribute("success", "Feed Edited");
            }
            else
                throw new Exception(response.message);
        }
        catch(Exception ex){
            if(blobKey != null){
                blobstoreService.delete(blobKey);
                blobstoreService.delete(blobKey2);
            }
            session.setAttribute("error", ex.getMessage());
            session.setAttribute("category", cat);
            session.setAttribute("title", title.trim());
            session.setAttribute("description", desc.trim());
        }
        resp.sendRedirect("/feed/edit/"+feedId);
    }
}
