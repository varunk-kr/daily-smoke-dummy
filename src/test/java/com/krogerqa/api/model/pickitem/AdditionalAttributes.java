package com.krogerqa.api.model.pickitem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalAttributes {
    private String containerNo;
    private String lastActionTimestamp;
    private String locationId;
    private String orderNumber;
    private String orderedItemQty;
    private String orderedUpc;
    private String pickDate;
    private String picker;
    private String pickerId;
    private String tempType;
    private String trackingId;
    private String trolleyName;
    private String type;

    public AdditionalAttributes(String containerNo, String lastActionTimestamp, String locationId, String orderNumber, String orderedItemQty, String orderedUpc, String pickDate, String picker, String pickerId, String tempType, String trackingId, String trolleyName, String type) {
        this.containerNo = containerNo;
        this.lastActionTimestamp = lastActionTimestamp;
        this.locationId = locationId;
        this.orderNumber = orderNumber;
        this.orderedItemQty = orderedItemQty;
        this.orderedUpc = orderedUpc;
        this.pickDate = pickDate;
        this.picker = picker;
        this.pickerId = pickerId;
        this.tempType = tempType;
        this.trackingId = trackingId;
        this.trolleyName = trolleyName;
        this.type = type;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLastActionTimestamp() {
        return lastActionTimestamp;
    }

    public void setLastActionTimestamp(String lastActionTimestamp) {
        this.lastActionTimestamp = lastActionTimestamp;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderedItemQty() {
        return orderedItemQty;
    }

    public void setOrderedItemQty(String orderedItemQty) {
        this.orderedItemQty = orderedItemQty;
    }

    public String getOrderedUpc() {
        return orderedUpc;
    }

    public void setOrderedUpc(String orderedUpc) {
        this.orderedUpc = orderedUpc;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getPicker() {
        return picker;
    }

    public void setPicker(String picker) {
        this.picker = picker;
    }

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrolleyName() {
        return trolleyName;
    }

    public void setTrolleyName(String trolleyName) {
        this.trolleyName = trolleyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
