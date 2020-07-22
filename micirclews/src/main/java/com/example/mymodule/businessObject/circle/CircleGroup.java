package com.example.mymodule.businessObject.circle;


import java.sql.Timestamp;
import java.util.List;

/**
 * Created by kamaldua on 08/16/2015.
 */
public class CircleGroup {
    public int circleID;
    public String circleName;
    public boolean isAdmin;
    public boolean isPublic;
    public String circleImageUrl;
    public Timestamp circleAge;
    public String xmppCrclName;
    public List<CircleMember> members;
    public List<CircleLocation> locations;
}
