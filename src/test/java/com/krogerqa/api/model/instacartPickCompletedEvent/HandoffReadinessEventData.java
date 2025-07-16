package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandoffReadinessEventData {
    private final String status;
    private final VehicleInfo vehicleInfo;

    public HandoffReadinessEventData(String status, VehicleInfo vehicleInfo) {
        this.status = status;
        this.vehicleInfo = vehicleInfo;
    }

    public String getStatus() {
        return status;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }
}
