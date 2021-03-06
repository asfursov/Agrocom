package com.asfursov.agrocom.model;

public class OperationAllowedRequest {
    String userId;

    String vehicleId;
    String operationId;
    String platformId;

    public OperationAllowedRequest(String vehicleId, String userId, OperationId operationId, Platform platform) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.operationId = operationId.getAPIName();
        this.platformId = platform == null ? null : platform.getId().toString();
    }

    public OperationAllowedRequest(String vehicleId, String userId, OperationId operationId) {
        this(vehicleId, userId, operationId, null);
    }
}
