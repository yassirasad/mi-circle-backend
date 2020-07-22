package com.example.mymodule.controller.panic;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.panic.AllActivePanicRequest;
import com.example.mymodule.businessObject.panic.PanicActiveDataResponse;
import com.example.mymodule.businessObject.panic.PanicDataRequest;
import com.example.mymodule.businessObject.panic.PanicDataResponse;
import com.example.mymodule.businessObject.panic.PanicFollowRequest;
import com.example.mymodule.businessObject.panic.PanicReleaseRequest;
import com.example.mymodule.datalayer.panic.DLPanic;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * Created by kamaldua on 02/03/2016.
 */
@Api(name = "panicApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class PanicEndpoint {

    @ApiMethod(name = "panicRespond", path = "panicRespond", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse panicRespond(PanicFollowRequest req) {
        return DLPanic.panicRespond(req);
    }

    @ApiMethod(name = "getPanicData", path = "getPanicData", httpMethod = ApiMethod.HttpMethod.POST)
    public PanicDataResponse getPanicData(PanicDataRequest req) {
        return DLPanic.getPanicData(req);
    }

    @ApiMethod(name = "panicRelease", path = "panicRelease", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse panicRelease(PanicReleaseRequest req) {
        return DLPanic.panicRelease(req);
    }

    @ApiMethod(name = "getAllActivePanic", path = "getAllActivePanic", httpMethod = ApiMethod.HttpMethod.POST)
    public PanicActiveDataResponse getAllActivePanic(DeviceSessionEntity req) {
        return DLPanic.getAllActivePanic(req);
    }
}
