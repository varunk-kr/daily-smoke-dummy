package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleInfo {
    private final String make;
    private final String model;
    private final String vehicleType;
    private final String color;

    public VehicleInfo(String make, String model, String vehicleType, String color) {
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.color = color;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getColor() {
        return color;
    }
}
