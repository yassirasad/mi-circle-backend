package com.example.mymodule.businessObject.location;

import java.util.List;

/**
 * Created by kamaldua on 09/21/2015.
 */
public class Location {
    public int locationID;
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public int radius;
    public String imageURL;
    public boolean isAdmin;

    public List<LocationFriend> sharedWithFriends;
}
