package com.krogerqa.api.model.shipt;

public class PIFShiptUpdateData {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String external_reference_id;
    private String status;

    public String getExternal_reference_id() {
        return external_reference_id;
    }

    public void setExternal_reference_id(String external_reference_id) {
        this.external_reference_id = external_reference_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PIFShiptUpdateDataDropOff getDropoff() {
        return dropoff;
    }

    public void setDropoff(PIFShiptUpdateDataDropOff dropoff) {
        this.dropoff = dropoff;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public PIFShiptUpdateData() {
    }

    private PIFShiptUpdateDataDropOff dropoff;
    private String vehicle_type;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public PIFShiptUpdateData(String id, String external_reference_id, String status, PIFShiptUpdateDataDropOff dropoff, String vehicle_type, String created_at, String updated_at) {
        this.id = id;
        this.external_reference_id = external_reference_id;
        this.status = status;
        this.dropoff = dropoff;
        this.vehicle_type = vehicle_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    private String created_at;
    private String updated_at;
}
