package com.asfursov.agrocom.model;

import java.util.UUID;

public class PlatformAvailableRequest {
    private final UUID userId;
    private final UUID platformId;

    public PlatformAvailableRequest(String userId, String platformId) {
        this.userId = UUID.fromString(userId);
        this.platformId = UUID.fromString(platformId);
    }

    public PlatformAvailableRequest(UserData user, String platformId) {
        this.userId = user.getId();
        this.platformId = UUID.fromString(platformId);
    }


    public PlatformAvailableRequest(UserData user, Platform platform) {
        this.userId = user.getId();
        this.platformId = platform.getId();
    }
}
