package com.asfursov.agrocom.model;

public class EnterLeaveRequest {
    String vehicleId;
    String userId;
    String plateNumber;
    String operationId;

    public EnterLeaveRequest(VehicleData vehicle, UserData operator, String plateNumber, OperationId operationId) {
        this.vehicleId = vehicle.getId().toString();
        this.userId = operator.getId().toString();
        this.plateNumber = plateNumber;
        this.operationId = operationId.getName();
    }

}
