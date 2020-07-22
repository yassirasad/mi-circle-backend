package com.example.mymodule.controller.userProfile;

/**
 * Created by kamaldua on 07/24/2015.
 */

import com.example.mymodule.businessObject.common.ApiResponse;
import com.example.mymodule.businessObject.common.Constants;
import com.example.mymodule.businessObject.common.DeviceSessionEntity;
import com.example.mymodule.businessObject.common.ImageURLRequest;
import com.example.mymodule.businessObject.common.ImageURLResponse;
import com.example.mymodule.businessObject.userProfile.ContactNoRequest;
import com.example.mymodule.businessObject.userProfile.CircleImageDeleteRequest;
import com.example.mymodule.businessObject.userProfile.FCMGetRequest;
import com.example.mymodule.businessObject.userProfile.FCMInfo;
import com.example.mymodule.businessObject.userProfile.FCMSetRequest;
import com.example.mymodule.businessObject.userProfile.ImageDeleteRequest;
import com.example.mymodule.businessObject.userProfile.LocationImageDeleteRequest;
import com.example.mymodule.businessObject.userProfile.LoginUser;
import com.example.mymodule.businessObject.userProfile.LoginUserResponse;
import com.example.mymodule.businessObject.userProfile.MemberProfileRequest;
import com.example.mymodule.businessObject.userProfile.MemberProfileResponse;
import com.example.mymodule.businessObject.userProfile.PassResetOTPVerificationRequest;
import com.example.mymodule.businessObject.userProfile.PasswordChangeRequest;
import com.example.mymodule.businessObject.userProfile.PasswordForgetRequest;
import com.example.mymodule.businessObject.userProfile.PasswordForgetRequestResponse;
import com.example.mymodule.businessObject.userProfile.PasswordResetRequest;
import com.example.mymodule.businessObject.userProfile.ResetPasswordRequest;
import com.example.mymodule.businessObject.userProfile.SignUp;
import com.example.mymodule.businessObject.userProfile.SignUpResponse;
import com.example.mymodule.businessObject.userProfile.StatusTextResponse;
import com.example.mymodule.businessObject.userProfile.StatusTextSetRequest;
import com.example.mymodule.businessObject.userProfile.UpdateUserProfileRequest;
import com.example.mymodule.businessObject.userProfile.UserProfileResponse;
import com.example.mymodule.businessObject.userProfile.ValidateReferral;
import com.example.mymodule.businessObject.userProfile.ValidateReferralResponse;
import com.example.mymodule.businessObject.userProfile.VerifyContactNoRequest;
import com.example.mymodule.businessObject.userProfile.VerifyUser;
import com.example.mymodule.businessObject.userProfile.VerifyUserResponse;
import com.example.mymodule.datalayer.userProfile.DLUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;


@Api(name = "userApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "micirclews.mymodule.example.com", ownerName = "micirclews.mymodule.example.com", packagePath=""))
public class UserEndpoint {

    @ApiMethod(name = "checkContactNo", path = "checkContactNo", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse checkContactNo(ContactNoRequest req)
    {
        return DLUser.checkContactNo(req);
    }

    @ApiMethod(name = "verifyContactNo", path = "verifyContactNo", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse verifyContactNo(VerifyContactNoRequest req)
    {
        return DLUser.verifyContactNo(req);
    }

    @ApiMethod(name = "signUp", httpMethod = ApiMethod.HttpMethod.POST)
    public SignUpResponse signUp(SignUp signUpDetails) { //CollectionResponse<Quote>
        return DLUser.signUp(signUpDetails);
    }

    @ApiMethod(name = "validateReferralCode", httpMethod = ApiMethod.HttpMethod.POST)
    public ValidateReferralResponse validateReferralCode(ValidateReferral referral){
        return DLUser.validateReferralCode(referral);
    }

    @ApiMethod(name = "verifyUser", httpMethod = ApiMethod.HttpMethod.POST)
    public VerifyUserResponse verifyUser(VerifyUser userDetails) {
        return DLUser.verifyUser(userDetails);
    }

    @ApiMethod(name = "loginUser", httpMethod = ApiMethod.HttpMethod.POST)
    public LoginUserResponse loginUser(LoginUser loginDetails) {
        return DLUser.loginUser(loginDetails);
    }

    @ApiMethod(name = "forgotPassword", path = "forgotPassword", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse forgotPassword(ContactNoRequest req)
    {
        return DLUser.forgotPassword(req);
    }

    @ApiMethod(name = "resetPassword", path = "resetPassword", httpMethod = ApiMethod.HttpMethod.POST)
    public ApiResponse resetPassword(ResetPasswordRequest req)
    {
        return DLUser.resetPassword(req);
    }

    @ApiMethod(
            name = "setFCMId",
            path = "setFCMId",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse setFCMId(FCMSetRequest req)
    {
        return DLUser.setFCMId(req);
    }

    @ApiMethod(
            name = "getFCMId",
            path = "getFCMId",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public FCMInfo getFCMId(FCMGetRequest req)
    {
        return DLUser.getFCMId(req);
    }

    @ApiMethod(
            name = "getProfileInfo",
            path = "getProfileInfo",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public UserProfileResponse getProfileInfo(DeviceSessionEntity req)
    {
        return DLUser.getProfileInfo(req);
    }

    @ApiMethod(
            name = "updateProfileInfo",
            path = "updateProfileInfo",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse updateProfileInfo(UpdateUserProfileRequest req)
    {
        return DLUser.updateProfileInfo(req);
    }

    @ApiMethod(
            name = "passwordForget",
            path = "passwordForget",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public PasswordForgetRequestResponse passwordForget(PasswordForgetRequest req)
    {
        return DLUser.passwordForget(req);
    }

    @ApiMethod(
            name = "passwordResetOTPVerification",
            path = "passwordResetOTPVerification",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse passwordResetOTPVerification(PassResetOTPVerificationRequest req)
    {
        return DLUser.passwordResetOTPVerification(req);
    }

    @ApiMethod(
            name = "passwordReset",
            path = "passwordReset",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse passwordReset(PasswordResetRequest req)
    {
        return DLUser.passwordReset(req);
    }

    @ApiMethod(
            name = "getMemberProfile",
            path = "getMemberProfile",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public MemberProfileResponse getMemberProfile(MemberProfileRequest req)
    {
        return DLUser.getMemberProfile(req);
    }

    @ApiMethod(
            name = "setNewPassword",
            path = "setNewPassword",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse setNewPassword(PasswordChangeRequest req)
    {
        return DLUser.setNewPassword(req);
    }

    @ApiMethod(
            name = "getImage",
            path = "getImage",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ImageURLResponse getImage(ImageURLRequest req)
    {
        return DLUser.getImage(req);
    }


    @ApiMethod(
            name = "deleteUserImage",
            path = "deleteUserImage",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse deleteUserImage(DeviceSessionEntity req)
    {
        ImageDeleteRequest newReq = new ImageDeleteRequest();
        newReq.deviceSession = req.deviceSession.trim();
        newReq.requestId = 0;
        newReq.paramId = 0;
        return DLUser.deleteUserImage(newReq);
    }

    @ApiMethod(
            name = "deleteLocationImage",
            path = "deleteLocationImage",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse deleteLocationImage(LocationImageDeleteRequest req)
    {
        ImageDeleteRequest newReq = new ImageDeleteRequest();
        newReq.deviceSession = req.deviceSession.trim();
        newReq.requestId = 1;
        newReq.paramId = req.locId;
        return DLUser.deleteUserImage(newReq);
    }

    @ApiMethod(
            name = "deleteCircleImage",
            path = "deleteCircleImage",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse deleteCircleImage(CircleImageDeleteRequest req)
    {
        ImageDeleteRequest newReq = new ImageDeleteRequest();
        newReq.deviceSession = req.deviceSession.trim();
        newReq.requestId = 2;
        newReq.paramId = req.circleId;
        return DLUser.deleteUserImage(newReq);
    }

    @ApiMethod(
            name = "getUserTextStatus",
            path = "getUserTextStatus",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public StatusTextResponse getUserTextStatus(DeviceSessionEntity req)
    {
        return DLUser.getUserTextStatus(req);
    }

    @ApiMethod(
            name = "setUserTextStatus",
            path = "setUserTextStatus",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse setUserTextStatus(StatusTextSetRequest req)
    {
        return DLUser.setUserTextStatus(req);
    }

    @ApiMethod(
            name = "testMethod",
            path = "testMethod",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public ApiResponse testMethod()
    {
        ApiResponse res = new ApiResponse();
        try {
            GeoApiContext context = new GeoApiContext(new GaeRequestHandler()).setApiKey(Constants.GOOGLE_API_KEY);
            DistanceMatrix r = DistanceMatrixApi.newRequest(context)
                    .origins(new LatLng(-31.9522, 115.8589))
                    .destinations(new LatLng(-25.344677, 131.036692))
                    .mode(TravelMode.DRIVING)
                    .awaitIgnoreError();
            String distance = "";
            String time = "";
            if (r.rows.length > 0) {
                if (r.rows[0].elements.length > 0) {
                    distance = r.rows[0].elements[0].distance.humanReadable;
                    time = r.rows[0].elements[0].duration.humanReadable;
                }
            }

            res.status = 1;
            res.message = distance.toString() + " " + time.toString();
        }
        catch (Exception ex)
        {
            res.status = 0;
            res.message = ex.getMessage();
        }
        return res;
    }


}

