package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandoffReadinessEventArrivedData {
    private final String status;
    private final String pickupSpot;
    private final VehicleInfo vehicleInfo;

    public HandoffReadinessEventArrivedData(String status, String pickupSpot, VehicleInfo vehicleInfo) {
        this.status = status;
        this.pickupSpot = pickupSpot;
        this.vehicleInfo = vehicleInfo;
    }

    public String getStatus() {
        return status;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public String getPickupSpot() {
        return pickupSpot;
    }
}
