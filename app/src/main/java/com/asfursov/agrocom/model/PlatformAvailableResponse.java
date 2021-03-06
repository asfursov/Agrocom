package com.asfursov.agrocom.model;

import java.time.LocalDateTime;
import java.util.Date;

public class PlatformAvailableResponse extends OperationResponse {
    private Platform platform;
    private String occupiedBy;
    private String occupiedFrom;


    public PlatformAvailableResponse() {
    }

    public PlatformAvailableResponse(Platform platform, boolean allowed, String occupiedBy, Date occupiedFrom) {
        this.platform = platform;
        this.allowed = allowed;
        this.occupiedBy = occupiedBy;
        this.occupiedFrom = occupiedFrom.toString();
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(String occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public LocalDateTime getOccupiedFrom() {
        return LocalDateTime.parse(occupiedFrom);
    }
}
