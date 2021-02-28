package com.asfursov.agrocom.model;

import java.util.UUID;

public class VehicleData {
    UUID id;
    String number;
    String plateNumber;
    String driver;
    String phone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
