package com.krogerqa.api.model.customerCheckIn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class CustomerCheckInPayload {
    private final String spotId;
    private final String vehicleType;
    private final String vehicleColor;

    public CustomerCheckInPayload(String spotId, String vehicleType, String vehicleColor) {
        this.spotId = spotId;
        this.vehicleType = vehicleType;
        this.vehicleColor = vehicleColor;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }
}
