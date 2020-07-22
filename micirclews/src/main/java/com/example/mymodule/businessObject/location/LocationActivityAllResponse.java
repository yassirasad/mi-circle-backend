package com.example.mymodule.businessObject.location;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.ArrayList;

/**
 * Created by kamaldua on 06/02/2016.
 */
public class LocationActivityAllResponse extends ApiResponse {
    public ArrayList<LocationFriendActivity> locationActivities;
    public ArrayList<LocationCircleActivity> circleActivities;

}
