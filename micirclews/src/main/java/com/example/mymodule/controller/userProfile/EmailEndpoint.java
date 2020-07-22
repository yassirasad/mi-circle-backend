package com.example.mymodule.controller.userProfile;

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.userProfile.EmailChangeRequest;
import com.example.mymodule.businessObject.userProfile.EmailVerifyOTPRequest;
import com.example.mymodule.datalayer.cronJobs.DLCronJobs;
import com.example.mymodule.datalayer.userProfile.DLEmail;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * Created by yassir on 5/5/17.
 */

@Api(name = "emailApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com",  ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class EmailEndpoint {

    @ApiMethod(name = "changeEmail", path = "changeEmail", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse changeEmail(EmailChangeRequest req)
    {
        return DLEmail.changeEmail(req);
    }

    @ApiMethod(name = "verifyEmail", path = "verifyEmail", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse verifyEmail(DeviceSessionEntity req)
    {
        return DLEmail.verifyEmail(req);
    }

    @ApiMethod(name = "verifyEmailOTP", path = "verifyEmailOTP", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse verifyEmailOTP(EmailVerifyOTPRequest req)
    {
        return DLEmail.verifyEmailOTP(req);
    }

    /*
    @ApiMethod(name = "testCron", path = "testCron", httpMethod = ApiMethod.HttpMethod.GET)
    public void testCron()
    {
        DLCronJobs.emailToInactiveUsers();
    }   */
}
