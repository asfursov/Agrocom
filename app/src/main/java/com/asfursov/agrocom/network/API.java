package com.asfursov.agrocom.network;

import com.asfursov.agrocom.model.EnterLeaveRequest;
import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.OperationAllowedRequest;
import com.asfursov.agrocom.model.OperationAllowedResponse;
import com.asfursov.agrocom.model.PlatformAvailableRequest;
import com.asfursov.agrocom.model.PlatformAvailableResponse;
import com.asfursov.agrocom.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {
    @POST("login")
    Call<UserData> login(@Body LoginRequest request);

    @POST("isAllowed")
    Call<OperationAllowedResponse> isAllowed(@Body OperationAllowedRequest request);

    @POST("enter")
    Call<OperationAllowedResponse> operate(@Body EnterLeaveRequest request);

    @POST("isAvailable")
    Call<PlatformAvailableResponse> isAvailable(@Body PlatformAvailableRequest request);

}
