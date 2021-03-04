package com.asfursov.agrocom.model;

public class OperationAllowedResponse {
    public VehicleData getVehicle() {
        return vehicle;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }

    VehicleData vehicle;
    boolean allowed;
    String message;
}
