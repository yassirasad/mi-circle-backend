package com.example.mymodule.businessObject.feed;

/**
 * Created by Yassir on 27-Sep-18.
 */

public class FeedLikeDislikeRequest{
    public String deviceSession;
    public int feedId;
    public byte likeDislikeAction;     // 0-None, 1-Like, 2-Dislike
}
