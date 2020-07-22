package com.example.mymodule.controller.location;

import com.example.mymodule.businessObject.circle.MemberCircleResponse;
import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.location.LocationActivityAllRequest;
import com.example.mymodule.businessObject.location.LocationActivityAllResponse;
import com.example.mymodule.businessObject.location.LocationActivityRequest;
import com.example.mymodule.businessObject.location.LocationActivityResponse;
import com.example.mymodule.businessObject.location.LocationActivitySetRequest;
import com.example.mymodule.businessObject.location.LocationAddRequest;
import com.example.mymodule.businessObject.location.LocationDeleteRequest;
import com.example.mymodule.businessObject.location.LocationFriendRequest;
import com.example.mymodule.businessObject.location.LocationGetResponse;
import com.example.mymodule.businessObject.location.LocationsFriendsRequest;
import com.example.mymodule.businessObject.location.UnshareLocationFromCirclesRequest;
import com.example.mymodule.datalayer.location.DLLocation;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

/**
 * Created by kamaldua on 09/21/2015.
 */
@Api(name = "locationApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))

public class LocationEndpoint {

    @ApiMethod(name = "addLocation", path = "addLocation", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse addLocation(LocationAddRequest req) {

        return DLLocation.addLocation(req);
    }

    @ApiMethod(name = "getLocations", path = "getLocations", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse getLocations(DeviceSessionEntity req) {

        return DLLocation.getLocations(req.deviceSession);
    }

    @ApiMethod(name = "deleteLocation", path = "deleteLocation", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse deleteLocation(LocationDeleteRequest req) {

        return DLLocation.deleteLocation(req);
    }

    /*@ApiMethod(name = "shareLocationWithFriend", path = "shareLocationWithFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse shareLocationWithFriend(LocationFriendRequest req) {

        return DLLocation.shareLocationWithFriend(req);
    }

    @ApiMethod(name = "unShareLocationWithFriend", path = "unShareLocationWithFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse unShareLocationWithFriend(LocationFriendRequest req) {

        return DLLocation.unShareLocationWithFriend(req);
    }*/

    @ApiMethod(name = "addLocationsWithFriends", path = "addLocationsWithFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse addLocationsWithFriends(LocationsFriendsRequest req) {

        return DLLocation.addLocationsWithFriends(req);
    }

    @ApiMethod(name = "deleteLocationsWithFriends", path = "deleteLocationsWithFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse deleteLocationsWithFriends(LocationsFriendsRequest req) {

        return DLLocation.deleteLocationsWithFriends(req);
    }

    @ApiMethod(name="leaveLocation" , path = "leaveLocation", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationGetResponse leaveLocation(LocationDeleteRequest req) {
        return DLLocation.leaveLocationOfFriend(req);
    }

    @ApiMethod(name = "getLocationActivities", path = "getLocationActivities", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationActivityResponse getLocationActivities(LocationActivityRequest req) {

        return DLLocation.getLocationActivities(req);
    }

    @ApiMethod(name = "setLocationActivities", path = "setLocationActivities", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse setLocationActivities(LocationActivitySetRequest req) {

        return DLLocation.setLocationActivities(req);
    }

    @ApiMethod(name = "getAllLocationActivities", path = "getAllLocationActivities", httpMethod = ApiMethod.HttpMethod.POST)
    public LocationActivityAllResponse getAllLocationActivities(LocationActivityAllRequest req) {

        return DLLocation.getAllLocationActivities(req);
    }

    @ApiMethod(name = "enableDisableLocationsFromCircles", path = "enableDisableLocationsFromCircles", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse enableDisableLocationsFromCircles(UnshareLocationFromCirclesRequest req ) { //CollectionResponse<Quote>

        return DLLocation.unShareLocationsFromCircles(req);
    }
}
