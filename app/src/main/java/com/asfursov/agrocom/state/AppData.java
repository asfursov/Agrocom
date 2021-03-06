package com.asfursov.agrocom.state;

import android.content.Context;

import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.model.Platform;
import com.asfursov.agrocom.model.Role;
import com.asfursov.agrocom.model.UserData;

public class AppData {
    private static AppData instance;
    private OperationId operationId;
    private Context context;
    private Platform platform;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private String lastBarcode;

    public int getBarcodeScannerReturnAction() {
        return barcodeScannerReturnAction;
    }

    public void setBarcodeScannerReturnAction(int barcodeScannerReturnAction) {
        this.barcodeScannerReturnAction = barcodeScannerReturnAction;
    }

    private int barcodeScannerReturnAction;


    public String getLastBarcode() {
        return lastBarcode;
    }

    public void setLastBarcode(String lastBarcode) {
        this.lastBarcode = lastBarcode;
    }

    private  AppData() {
    }

    private UserData user;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    private Role role;

    public static AppData getInstance() {
        if (instance == null) instance = new AppData();
        return instance;
    }

    public OperationId getOperationId() {
        return operationId;
    }

    public void setOperationId(OperationId operationId) {
        this.operationId = operationId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

}
