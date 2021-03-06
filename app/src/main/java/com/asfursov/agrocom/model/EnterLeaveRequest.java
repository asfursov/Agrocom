package com.asfursov.agrocom.model;

public class EnterLeaveRequest {
    String vehicleId;
    String userId;
    String plateNumber;
    String operationId;
    String platformId;

    public EnterLeaveRequest(VehicleData vehicle, UserData operator, String plateNumber, OperationId operationId) {
        this(vehicle, operator, plateNumber, operationId, null);
    }

    public EnterLeaveRequest(VehicleData vehicle, UserData operator, String plateNumber, OperationId operationId, Platform platform) {
        this.vehicleId = vehicle.getId().toString();
        this.userId = operator.getId().toString();
        this.plateNumber = plateNumber;
        this.operationId = operationId.getAPIName();
        this.platformId = platform == null ? null : platform.getId().toString();
    }

}
