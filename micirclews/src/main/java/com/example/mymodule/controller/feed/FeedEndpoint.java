package com.example.mymodule.controller.feed;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.feed.Countries;
import com.example.mymodule.businessObject.feed.FeedCategories;
import com.example.mymodule.businessObject.feed.FeedLikeDislikeRequest;
import com.example.mymodule.businessObject.feed.FeedRequest;
import com.example.mymodule.businessObject.feed.FeedResponse;
import com.example.mymodule.businessObject.feed.FeedsRequest;
import com.example.mymodule.businessObject.feed.FeedsResponse;
import com.example.mymodule.datalayer.feed.DLFeed;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yassir on 29-Aug-18.
 */

@Api(name = "feedApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class FeedEndpoint {
    @ApiMethod(name = "getCountryCodes", path = "getCountryCodes", httpMethod = ApiMethod.HttpMethod.GET)
    public Countries getCountryCodes(){
        return DLFeed.getCountryCodes();
    }

    @ApiMethod(name = "getFeedCategories", path = "getFeedCategories", httpMethod = ApiMethod.HttpMethod.GET)
    public FeedCategories getFeedCategories(){
        return DLFeed.getFeedCategories();
    }

    @ApiMethod(name = "getFeed", path = "getFeed", httpMethod = ApiMethod.HttpMethod.POST)
    public FeedResponse getFeed(FeedRequest req){
        return DLFeed.getFeed(req);
    }

    @ApiMethod(name = "getFeeds", path = "getFeeds", httpMethod = ApiMethod.HttpMethod.POST)
    public FeedsResponse getFeeds(FeedsRequest req){
        return DLFeed.getFeeds(req);
    }

    @ApiMethod(name = "deleteFeed", path = "deleteFeed", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse deleteFeed(FeedRequest req){
        return DLFeed.deleteFeed(req);
    }

    @ApiMethod(name = "likeDislikeFeed", path = "likeDislikeFeed", httpMethod = ApiMethod.HttpMethod.POST)
    public FeedResponse likeDislikeFeed(FeedLikeDislikeRequest req){
        return DLFeed.likeDislikeFeed(req);
    }

    @ApiMethod(name = "reportFeed", path = "reportFeed", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse reportFeed(FeedRequest req){
        return DLFeed.reportFeed(req);
    }
}
