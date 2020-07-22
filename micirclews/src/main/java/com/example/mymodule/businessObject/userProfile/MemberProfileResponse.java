package com.example.mymodule.businessObject.userProfile;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kamaldua on 04/27/2016.
 */
public class MemberProfileResponse extends ApiResponse {
    public String firstName;
    public String lastName;
    public String countryCode;
    public String imageUrl;
    public String gender;
    public Date dob;
    public String zip;
    public String statusText;
    public ArrayList<MemberProfileCircle> circles;
}
