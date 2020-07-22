package com.example.mymodule.businessObject.location;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.List;

/**
 * Created by kamaldua on 11/27/2015.
 */
public class LocationActivityResponse extends ApiResponse {
    public List<LocationFriendActivity> frnds;
    public List<LocationCircleActivity> crcls;

}
