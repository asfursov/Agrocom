package com.asfursov.agrocom.model;

public class OperationAllowedResponse extends OperationResponse {
    public VehicleData getVehicle() {
        return vehicle;
    }

    VehicleData vehicle;

    float humidity;
    float trash;

    public float getHumidity() {
        return humidity;
    }

    public float getTrash() {
        return trash;
    }
}
