package com.asfursov.agrocom.network;

import android.text.Editable;

import com.asfursov.agrocom.model.UserData;

import java.util.UUID;

public class APIHelper {
    public static UserData findUserByBarcode(String lastBarcode) {
        UserData user = new UserData();
        return user;
        //TODO Check user on server
    }

    public static UserData AuthorizeUser(UUID id, Editable text) {
        return new UserData();
    }
}
