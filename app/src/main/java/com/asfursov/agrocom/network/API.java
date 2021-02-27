package com.asfursov.agrocom.network;

import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {
    @POST("login")
    Call<UserData> login(@Body LoginRequest request);
}
