package com.example.mymodule.businessObject.userProfile;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.Date;

/**
 * Created by kamaldua on 11/17/2015.
 */
public class UserProfileResponse extends ApiResponse {
    public int userId;
    public String firstName;
    public String lastName;
    public String likes;
    public String dislikes;
    public String workplace;
    public boolean isInActive;
    public String imageThumbnail;
    public String image;
    public String countryCode;
    public String contactNo;
    public String emailID;
    public Date dob;
    public String zip;
    public String statusText;
    public String gender;
    public String passwordChangedOn;
    public boolean isEmailVerified;
    public String referralCode;
}
