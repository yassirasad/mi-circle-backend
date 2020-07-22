package com.example.mymodule.businessObject.circle;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.util.List;

/**
 * Created by kamaldua on 08/17/2015.
 */
public class CircleGroupResponse extends ApiResponse {
    public String circleID;
    public String circleName;
    public boolean isAdmin;
    public boolean isPublic;
    public String location;

    public List<CircleMember> members;
}
