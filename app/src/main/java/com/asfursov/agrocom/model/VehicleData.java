package com.asfursov.agrocom.model;

import java.util.UUID;

public class VehicleData {
    UUID id;
    String number;
    String plateNumber;
    String driver;
    String phone;
    Boolean isTrailer;
    Boolean trailerSeparately;

    public Boolean getTrailer() {
        return isTrailer;
    }

    public Boolean getTrailerSeparately() {
        return trailerSeparately;
    }

    public String getNumber() {
        return number;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getDriver() {
        return driver.
                replace("(", "\n(")
                .replace(";", ";\n");
    }

    public String getPhone() {
        return phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
