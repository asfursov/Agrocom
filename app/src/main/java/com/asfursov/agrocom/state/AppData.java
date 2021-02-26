package com.asfursov.agrocom.state;

import com.asfursov.agrocom.model.UserData;

public class AppData {
    private static AppData instance;
    public static AppData getInstance(){
        if (instance==null) instance=new AppData();
        return instance;
    };

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
}
