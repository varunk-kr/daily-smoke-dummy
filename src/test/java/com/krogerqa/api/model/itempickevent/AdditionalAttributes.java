package com.krogerqa.api.model.itempickevent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalAttributes {
    private String containerUuid;
    private String containerNo;
    private String trolleyId;
    private String pickDate;
    private String fulfillmentType;
    private String itemId;
    private String orderedItemQty;
    private String fulfillmentApp;
    private String pickedAmount;
    private String itemPickBeginTime;
    private String locationId;
    private String randomWeight;
    private String itemStatus;
    private String orderedUpc;
    private String itemPickEndTime;
    private String tempType;
    private String itemUuid;
    private String trackingId;

    public AdditionalAttributes(String containerUuid, String containerNo, String trolleyId, String pickDate, String fulfillmentType, String itemId, String orderedItemQty, String fulfillmentApp, String pickedAmount, String itemPickBeginTime, String locationId, String randomWeight, String itemStatus, String orderedUpc, String itemPickEndTime, String tempType, String itemUuid, String trackingId) {
        this.containerUuid = containerUuid;
        this.containerNo = containerNo;
        this.trolleyId = trolleyId;
        this.pickDate = pickDate;
        this.fulfillmentType = fulfillmentType;
        this.itemId = itemId;
        this.orderedItemQty = orderedItemQty;
        this.fulfillmentApp = fulfillmentApp;
        this.pickedAmount = pickedAmount;
        this.itemPickBeginTime = itemPickBeginTime;
        this.locationId = locationId;
        this.randomWeight = randomWeight;
        this.itemStatus = itemStatus;
        this.orderedUpc = orderedUpc;
        this.itemPickEndTime = itemPickEndTime;
        this.tempType = tempType;
        this.itemUuid = itemUuid;
        this.trackingId = trackingId;
    }

    public String getContainerUuid() {
        return containerUuid;
    }

    public void setContainerUuid(String containerUuid) {
        this.containerUuid = containerUuid;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getTrolleyId() {
        return trolleyId;
    }

    public void setTrolleyId(String trolleyId) {
        this.trolleyId = trolleyId;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(String fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderedItemQty() {
        return orderedItemQty;
    }

    public void setOrderedItemQty(String orderedItemQty) {
        this.orderedItemQty = orderedItemQty;
    }

    public String getFulfillmentApp() {
        return fulfillmentApp;
    }

    public void setFulfillmentApp(String fulfillmentApp) {
        this.fulfillmentApp = fulfillmentApp;
    }

    public String getPickedAmount() {
        return pickedAmount;
    }

    public void setPickedAmount(String pickedAmount) {
        this.pickedAmount = pickedAmount;
    }

    public String getItemPickBeginTime() {
        return itemPickBeginTime;
    }

    public void setItemPickBeginTime(String itemPickBeginTime) {
        this.itemPickBeginTime = itemPickBeginTime;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getRandomWeight() {
        return randomWeight;
    }

    public void setRandomWeight(String randomWeight) {
        this.randomWeight = randomWeight;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getOrderedUpc() {
        return orderedUpc;
    }

    public void setOrderedUpc(String orderedUpc) {
        this.orderedUpc = orderedUpc;
    }

    public String getItemPickEndTime() {
        return itemPickEndTime;
    }

    public void setItemPickEndTime(String itemPickEndTime) {
        this.itemPickEndTime = itemPickEndTime;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
