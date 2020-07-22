package com.example.mymodule.micirclefileupload.businessObject.panic;

import com.example.mymodule.micirclefileupload.dataLayer.common.ApiResponse;

import java.sql.Timestamp;

/**
 * Created by kamaldua on 02/03/2016.
 */
public class PanicResponse extends ApiResponse {
    public int panicID;
    public int panicLocationID;
    public int userID;
    public double latitude;
    public double longitude;
    public Timestamp createdOn;
    public boolean isPanicUser;
    public String imageUrl;
    public String userName;
}
