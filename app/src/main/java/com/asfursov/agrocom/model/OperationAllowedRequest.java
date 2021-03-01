package com.asfursov.agrocom.model;

public class OperationAllowedRequest {
    String userId;

    String vehicleId;
    String operationId;

    public OperationAllowedRequest(String vehicleId, String userId, OperationId operationId) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.operationId = operationId.getAPIName();
    }
}
