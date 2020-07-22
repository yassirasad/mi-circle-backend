package com.example.mymodule.businessObject.feed;

/**
 * Created by Yassir on 25-Sep-18.
 */

public class Feed extends FeedCategory{
    public int feedId;
    public String title;
    public String description;
    public String imageUrl;
    public String videoUrl;
    public String thumbnailUrl;
    public int creatorUserId;
    public String creatorUserName;
    public String createdOn;
    public byte likeDislikeStatus;
    public int likeCount;
    public int dislikeCount;
    public int reportCount;
}
