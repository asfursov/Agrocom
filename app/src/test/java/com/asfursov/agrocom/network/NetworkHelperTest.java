package com.asfursov.agrocom.network;

import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.OperationAllowedRequest;
import com.asfursov.agrocom.model.OperationAllowedResponse;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.model.UserData;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkHelperTest extends TestCase {

    public static final String USERID = "c778259e-78cd-11eb-9aab-d01f30b1f1a7";
    public static final String PASSWORD = "MTIzNDU=";

    public void testGetAPI() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID, PASSWORD));

        Response<UserData> response = call.execute();
        assertEquals(200, response.code());
        assertNotNull("Response", response);
        assertNotNull("Response data", response.body());
        assertTrue("User ID matched", USERID.equalsIgnoreCase(response.body().getId().toString()));
        assertTrue("Roles not empty", response.body().getRoles().size() > 0);


    }

    public void testNoPassword() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID, null));

        Response<UserData> response = call.execute();
        assertEquals(403, response.code());


    }

    @Test
    public void testNotFound() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest("DUMMY", null));

        Response<UserData> response = call.execute();
        assertEquals("Response code", 401, response.code());


    }

    public void testWrongPassword() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest(USERID, "dummy"));

        Response<UserData> response = call.execute();
        assertEquals(403, response.code());


    }

    public void testSupporting() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<UserData> call = api.login(new LoginRequest("3E895B4B-3F09-11EB-9C88-70C94EDD47E0", null));

        Response<UserData> response = call.execute();
        assertEquals("Response code", 401, response.code());


    }

    public void testIsAllowed() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<OperationAllowedResponse> call = api.isAllowed(
                new OperationAllowedRequest(
                        "4be635ec-71ce-11eb-9c93-70c94edd47e0",
                        "0C9769A4-5026-11EB-9C8A-70C94EDD47E0",
                        OperationId.LEAVE
                ));

        Response<OperationAllowedResponse> response = call.execute();
        assertEquals("Response code", 200, response.code());


    }

    public void testIsAllowedVehicleNotFound() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<OperationAllowedResponse> call = api.isAllowed(
                new OperationAllowedRequest(
                        "//4be635ec-71ce-11eb-9c93-70c94edd47e0",
                        "0C9769A4-5026-11EB-9C8A-70C94EDD47E0",
                        OperationId.LEAVE
                ));

        Response<OperationAllowedResponse> response = call.execute();
        assertEquals("Response code", 404, response.code());


    }

    public void testIsAllowedUserNotFound() throws IOException {
        API api = NetworkHelper.getInstance().getAPI();
        Call<OperationAllowedResponse> call = api.isAllowed(
                new OperationAllowedRequest(
                        "4be635ec-71ce-11eb-9c93-70c94edd47e0",
                        "//0C9769A4-5026-11EB-9C8A-70C94EDD47E0",
                        OperationId.LEAVE
                ));

        Response<OperationAllowedResponse> response = call.execute();
        assertEquals("Response code", 403, response.code());


    }
}
