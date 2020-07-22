package com.example.mymodule.controller.circle;

import com.example.mymodule.businessObject.UserContactPackage.UserContactRequest;
import com.example.mymodule.businessObject.circle.CircleAddMemberRequest;
import com.example.mymodule.businessObject.circle.CircleAddMultipleMembersRequest;
import com.example.mymodule.businessObject.circle.CircleAddMultipleMembersResponse;
import com.example.mymodule.businessObject.circle.CircleAgeUpdateRequest;
import com.example.mymodule.businessObject.circle.CircleCreateRequest;
import com.example.mymodule.businessObject.circle.CircleDeleteRequest;
import com.example.mymodule.businessObject.circle.CircleGroupList;
import com.example.mymodule.businessObject.circle.CircleGroupListRequest;
import com.example.mymodule.businessObject.circle.CircleGroupResponse;
import com.example.mymodule.businessObject.circle.CircleMemberDeleteRequest;
import com.example.mymodule.businessObject.circle.CircleMemberLocation;
import com.example.mymodule.businessObject.circle.CirclesMemberLocations;
import com.example.mymodule.businessObject.circle.MemberCircleResponse;
import com.example.mymodule.businessObject.circle.MemberLocationTrackingRequest;
import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.datalayer.circle.DLCircle;
import com.example.mymodule.datalayer.common.ConnectionManager;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import org.igniterealtime.restclient.RestApiClient;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kamaldua on 08/16/2015.
 */
@Api(name = "circleApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))

public class CircleEndpoint {

    @ApiMethod(name = "getCircles", path = "getCircles", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList getCircles(DeviceSessionEntity req)
    { //CollectionResponse<Quote>
        return DLCircle.getCircles(req);
    }

    @ApiMethod(name = "createCircle", path = "createCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList createCircle(CircleCreateRequest request)
    {
        return DLCircle.createCircle(request);
    }

    @ApiMethod(name = "deleteCircle", path = "deleteCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList deleteCircle(CircleDeleteRequest request)
    {
        return DLCircle.deleteCircle(request);
    }

    @ApiMethod(name = "deleteMemberFromCircle", path = "deleteMemberFromCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList deleteMemberFromCircle(CircleMemberDeleteRequest request)
    {
        return DLCircle.deleteMemberFromCircle(request);
    }

    /** @Created by Yassir **/
    @ApiMethod(name = "leaveCircle", path = "leaveCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList leaveCircle(CircleDeleteRequest request)
    {
        return DLCircle.leaveCircle(request);
    }

    @ApiMethod(name = "addMemberToCircle", path = "addMemberToCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse addMemberToCircle(CircleAddMemberRequest req ) { //CollectionResponse<Quote>

        return DLCircle.addMemberToCircle(req);
    }

    @ApiMethod(name = "addMultipleMembersToCircle", path = "addMultipleMembersToCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleAddMultipleMembersResponse addMultipleMembersToCircle(CircleAddMultipleMembersRequest req ) { //CollectionResponse<Quote>

        return DLCircle.addMultipleMembersToCircle(req);
    }

    @ApiMethod(name = "addLocationToCircle", path = "addLocationToCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList addLocationToCircle(CircleMemberLocation req ) { //CollectionResponse<Quote>

        return DLCircle.addLocationToCircle(req);
    }

    @ApiMethod(name = "deleteLocationToCircle", path = "deleteLocationToCircle", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList deleteLocationToCircle(CircleMemberLocation req ) { //CollectionResponse<Quote>

        return DLCircle.deleteLocationToCircle(req);
    }

    @ApiMethod(name = "addLocationsToCircles", path = "addLocationsToCircles", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList addLocationsToCircles(CirclesMemberLocations req ) { //CollectionResponse<Quote>

        return DLCircle.addLocationsToCircles(req);
    }

    @ApiMethod(name = "deleteLocationsToCircles", path = "deleteLocationsToCircles", httpMethod = ApiMethod.HttpMethod.POST)
    public CircleGroupList deleteLocationsToCircles(CirclesMemberLocations req ) { //CollectionResponse<Quote>

        return DLCircle.deleteLocationsToCircles(req);
    }

    @ApiMethod(name = "circleAgeUpdate", path = "circleAgeUpdate", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse circleAgeUpdate(CircleAgeUpdateRequest req ) { //CollectionResponse<Quote>

        return DLCircle.circleAgeUpdate(req);
    }

    /*
    @ApiMethod(name = "getMemberCircles", path = "getMemberCircles", httpMethod = ApiMethod.HttpMethod.POST)
    public MemberCircleResponse getMemberCircles(UserContactRequest req ) { //CollectionResponse<Quote>

        return DLCircle.getMemberCircles( req );
    }
    */

}
