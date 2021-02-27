package com.asfursov.agrocom.model;

public class LoginRequest {
    public String userid;
    public String password;

    public LoginRequest() {
    }

    public LoginRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

}
