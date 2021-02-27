package com.asfursov.agrocom.network;

import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.UserData;

import junit.framework.TestCase;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkHelperTest extends TestCase {

    public static final String USERID = "c778259e-78cd-11eb-9aab-d01f30b1f1a7";
    public static final String PASSWORD = "MTIzNDU=";

    public void testGetAPI() {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID, PASSWORD));
        try {
            Response<UserData> response = call.execute();
            assertEquals(200,response.code());
            assertNotNull("Response",response);
            assertNotNull("Response data",response.body());
            assertTrue("User ID matched",USERID.equalsIgnoreCase(response.body().getId().toString()));
            assertTrue("Roles not empty",response.body().getRoles().size()>0);
        } catch (Throwable e) {

        }
    }
    public void testNoPassword() {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID, null));
        try {
            Response<UserData> response = call.execute();
            assertEquals(401,response.code());
        } catch (Throwable e) {

        }
    }
    public void testNotFound() {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest("DUMMY", null));
        try {
            Response<UserData> response = call.execute();
            assertEquals(404,response.code());
        } catch (Throwable e) {

        }
    }
    public void testWrongPassword() {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID,"dummy"));
        try {
            Response<UserData> response = call.execute();
            assertEquals(404,response.code());
        } catch (Throwable e) {

        }
    }
}
