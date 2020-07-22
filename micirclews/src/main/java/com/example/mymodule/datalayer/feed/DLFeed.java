package com.example.mymodule.datalayer.feed;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.BlobOperation;
import com.example.mymodule.businessObject.feed.Countries;
import com.example.mymodule.businessObject.feed.Country;
import com.example.mymodule.businessObject.feed.Feed;
import com.example.mymodule.businessObject.feed.FeedCategories;
import com.example.mymodule.businessObject.feed.FeedCategory;
import com.example.mymodule.businessObject.feed.FeedCreateRequest;
import com.example.mymodule.businessObject.feed.FeedEditRequest;
import com.example.mymodule.businessObject.feed.FeedEditResponse;
import com.example.mymodule.businessObject.feed.FeedLikeDislikeRequest;
import com.example.mymodule.businessObject.feed.FeedRequest;
import com.example.mymodule.businessObject.feed.FeedResponse;
import com.example.mymodule.businessObject.feed.FeedsRequest;
import com.example.mymodule.businessObject.feed.FeedsResponse;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.example.mymodule.datalayer.common.StoreProcedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Exception;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by Yassir on 24-Aug-18.
 */

public class DLFeed {
    public static Boolean loginFeed(String username, String password) {
        boolean isCredentialValid = false;
        try {
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_LOGIN);
            cs.setString(1, username.trim());
            cs.setString(2, password);
            cs.registerOutParameter(3, Types.BOOLEAN);
            cs.executeQuery();

            isCredentialValid = cs.getBoolean(3);

            cs.close();
            conn.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
        return isCredentialValid;
    }

    public static Countries getCountryCodes(){
        Countries res = new Countries();
        res.countries = new ArrayList<>();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.COUNTRIES);
            ResultSet rs = cs.executeQuery();

            while(rs.next()){
                Country country = new Country();
                country.code = rs.getString(1);
                country.name = rs.getString(2);
                res.countries.add(country);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FeedCategories getFeedCategories(){
        FeedCategories res = new FeedCategories();
        res.categories = new ArrayList<>();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_CATEGORIES);
            ResultSet rs = cs.executeQuery();

            while(rs.next()){
                FeedCategory feedCategory = new FeedCategory();
                feedCategory.categoryId = rs.getInt(1);
                feedCategory.category = rs.getString(2);
                res.categories.add(feedCategory);
            }
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse createFeed(FeedCreateRequest req){
        ApiResponse res = new ApiResponse();
        res.status = 0;
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_CREATE);
            cs.setString(1, req.deviceSession.trim());
            cs.setString(2, req.countryISOCodes);
            cs.setInt(3, req.categoryId);
            cs.setString(4, req.title);
            cs.setString(5, req.description);
            cs.setString(6, req.blobContentType);
            cs.setString(7, req.blobKey);
            cs.setString(8, req.blobKey2);
            cs.executeQuery();
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch (Exception ex){
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FeedEditResponse editFeed(FeedEditRequest req){
        FeedEditResponse res = new FeedEditResponse();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_EDIT);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.feedId);
            cs.setString(3, req.countryISOCodes);
            cs.setInt(4, req.categoryId);
            cs.setString(5, req.title);
            cs.setString(6, req.description);
            cs.setString(7, req.blobContentType);
            cs.setString(8, req.blobKey);
            cs.setString(9, req.blobKey2);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.registerOutParameter(11, Types.VARCHAR);
            cs.execute();

            res.OldBlobKey = cs.getString(10);
            res.OldBlobKey2 = cs.getString(11);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse deleteFeed(FeedRequest req){
        ApiResponse res = new ApiResponse();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_DELETE);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.feedId);
            cs.execute();
            cs.close();
            conn.close();

            res.status = 1;
            res.message = "success";
        }
        catch (Exception ex){
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static String getFeedBlobKey(int feedId) throws Exception{
        Connection conn = ConnectionManager.getConnection();
        CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_BLOBKEY);
        cs.setInt(1, feedId);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.execute();
        return cs.getString(2);
    }

    public static FeedResponse getFeed(FeedRequest req){
        FeedResponse res = new FeedResponse();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, -1);   //Need valid UserID when directly called from another Stored Procedure.
            cs.setInt(3, req.feedId);
            ResultSet rs = cs.executeQuery();

            if(rs.next())
                res.feed = getSingleFeedFromResultSet(rs);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FeedsResponse getFeeds(FeedsRequest req){
        FeedsResponse res = new FeedsResponse();
        res.feeds = new ArrayList<>();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEEDS);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.lastFeedId);
            cs.setInt(3, req.feedsLimit);
            ResultSet rs = cs.executeQuery();

            while(rs.next())
                res.feeds.add(getSingleFeedFromResultSet(rs));

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static FeedResponse likeDislikeFeed(FeedLikeDislikeRequest req){
        FeedResponse res = new FeedResponse();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_LIKE_DISLIKE);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.feedId);
            cs.setByte(3, req.likeDislikeAction);
            ResultSet rs = cs.executeQuery();

            if(rs.next())
                res.feed = getSingleFeedFromResultSet(rs);

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    public static ApiResponse reportFeed(FeedRequest req){
        ApiResponse res = new ApiResponse();
        try{
            Connection conn = ConnectionManager.getConnection();
            CallableStatement cs = conn.prepareCall(StoreProcedures.UserProfile.FEED_REPORT);
            cs.setString(1, req.deviceSession.trim());
            cs.setInt(2, req.feedId);
            cs.execute();

            cs.close();
            conn.close();

            res.status = 1;
            res.message = "successful";

        } catch (Exception ex) {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }

    private static Feed getSingleFeedFromResultSet(ResultSet rs) throws SQLException{
        Feed feed = new Feed();
        feed.feedId = rs.getInt(1);
        feed.categoryId = rs.getInt(2);
        feed.category = rs.getString(3);
        feed.title = rs.getString(4);
        feed.description = rs.getString(5);
        String blobContentType = rs.getString(6);
        String blobKeyString = rs.getString(7);
        String blobKeyString2 = rs.getString(8);
        if(blobContentType!=null && blobContentType.equals("image"))
            feed.imageUrl = BlobOperation.getServingURL(blobKeyString);
        else if(blobContentType!=null && blobContentType.equals("video")){
            feed.videoUrl = "https://mi-circle.appspot.com/feed/video?fid="+rs.getInt(1);
            feed.thumbnailUrl = BlobOperation.getServingURL(blobKeyString2);
        }
        feed.creatorUserId = rs.getInt(9);
        feed.creatorUserName = rs.getString(10);
        feed.createdOn = rs.getString(11);
        feed.likeDislikeStatus = rs.getByte(12);
        feed.likeCount = rs.getInt(13);
        feed.dislikeCount = rs.getInt(14);
        feed.reportCount = rs.getInt(15);
        return feed;
    }
}
