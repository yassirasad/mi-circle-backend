package com.example.mymodule.businessObject.common;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

/**
 * Created by Yassir on 25-Sep-18.
 */

public class BlobOperation {
    private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public static String getServingURL(String blobKeyString){
        try {
            if(blobKeyString == null)
                return null;
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(new BlobKey(blobKeyString));
            return imagesService.getServingUrl(servingUrlOptions);
        }
        catch(Exception ex){
            return null;
        }
    }

    public static void deleteServingURL(String blobKeyString){
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        imagesService.deleteServingUrl(new BlobKey(blobKeyString));
    }

    public static void deleteBlobKey(String blobKeyString){
        blobstoreService.delete(new BlobKey(blobKeyString));
    }
}
