package com.example.mymodule.businessObject.panic;

import com.example.mymodule.businessObject.common.ApiResponse;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by kamaldua on 02/03/2016.
 */
public class PanicDataResponse extends ApiResponse{
   public List<PanicData> data;
    public List<PanicMember> members;
    public boolean isPanicReleased;
    public String imgUrl;
}
