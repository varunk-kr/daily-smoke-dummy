package com.krogerqa.api.model.pclAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assignments {

    private String containerId;
    private String permanentLabel;
    private String batchedNo;
    private String locationId;
    private String orderId;
    private int itemCount;
    private String needBy;
    private String trolleyName;
    private String pickupDate;
    private long eventTime;

    public Assignments() {
    }

    public Assignments(String containerId, String permanentLabel, String batchedNo, String locationId, String orderId, int itemCount, String needBy, String trolleyName, String pickupDate, long eventTime) {
        this.containerId = containerId;
        this.permanentLabel = permanentLabel;
        this.batchedNo = batchedNo;
        this.locationId = locationId;
        this.orderId = orderId;
        this.itemCount = itemCount;
        this.needBy = needBy;
        this.trolleyName = trolleyName;
        this.pickupDate = pickupDate;
        this.eventTime = eventTime;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getBatchedNo() {
        return batchedNo;
    }

    public String getPermanentLabel() {
        return permanentLabel;
    }

    public long getEventTime() {
        return eventTime;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getNeedBy() {
        return needBy;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getTrolleyName() {
        return trolleyName;
    }
}
