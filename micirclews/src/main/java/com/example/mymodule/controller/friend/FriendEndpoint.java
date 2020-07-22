package com.example.mymodule.controller.friend;


import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.friend.FriendIgnoreRequest;
import com.example.mymodule.businessObject.friend.FriendRemoveBlockRequest;
import com.example.mymodule.businessObject.friend.FriendSearchResponse;
import com.example.mymodule.businessObject.friend.FriendSerachRequest;
import com.example.mymodule.businessObject.friend.FriendSuggestionResponse;
import com.example.mymodule.businessObject.friend.GetFriendResponse;
import com.example.mymodule.businessObject.friend.NewFriendRequest;
import com.example.mymodule.businessObject.friend.NewFriendWithIDRequest;
import com.example.mymodule.datalayer.friend.DLFriend;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * Created by kamaldua on 09/22/2015.
 */
@Api(name = "friendApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class FriendEndpoint {

    @ApiMethod(name = "sendFriendRequest", path = "sendFriendRequest", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse sendFriendRequest(NewFriendRequest req) {
        return DLFriend.sendFriendRequest(req);
    }

    @ApiMethod(name = "getFriends", path = "getFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public GetFriendResponse getFriends(DeviceSessionEntity req) {
        return DLFriend.getFriends(req);
    }

    @ApiMethod(name = "searchFriends", path = "searchFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public FriendSearchResponse searchFriends(FriendSerachRequest req) {
        return DLFriend.searchFriends(req);
    }

    @ApiMethod(name = "suggestFriends", path = "suggestFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public FriendSuggestionResponse suggestFriends(DeviceSessionEntity req) {
        return DLFriend.suggestFriends(req);
    }

    @ApiMethod(name = "ignoreFriend", path = "ignoreFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse ignoreFriend(FriendIgnoreRequest req) {
        return DLFriend.ignoreFriend(req);
    }

    @ApiMethod(name = "sendFriendRequestWithID", path = "sendFriendRequestWithID", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse sendFriendRequestWithID(NewFriendWithIDRequest req) {
        return DLFriend.sendFriendRequestWithID(req);
    }

    @ApiMethod(name = "blockFriend", path = "blockFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public GetFriendResponse blockFriend(FriendRemoveBlockRequest req) {
        return DLFriend.blockFriend(req);
    }

    @ApiMethod(name = "unBlockFriend", path = "unBlockFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public GetFriendResponse unblockFriend(FriendRemoveBlockRequest req) {
        return DLFriend.unBlockFriend(req);
    }

    @ApiMethod(name = "unFriend", path = "unFriend", httpMethod = ApiMethod.HttpMethod.POST)
    public GetFriendResponse unFriend(FriendRemoveBlockRequest req) {
        return DLFriend.unFriend(req);
    }
}
