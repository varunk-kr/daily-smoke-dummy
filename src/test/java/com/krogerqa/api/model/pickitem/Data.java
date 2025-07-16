package com.krogerqa.api.model.pickitem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private String fulfillmentType;
    private String trackingId;
    private String itemId;
    private double pickedAmount;
    private AdditionalAttributes additionalAttributes;

    public Data(String fulfillmentType, String trackingId, String itemId, double pickedAmount, AdditionalAttributes additionalAttributes) {
        this.fulfillmentType = fulfillmentType;
        this.trackingId = trackingId;
        this.itemId = itemId;
        this.pickedAmount = pickedAmount;
        this.additionalAttributes = additionalAttributes;
    }

    public String getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(String fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getPickedAmount() {
        return pickedAmount;
    }

    public void setPickedAmount(double pickedAmount) {
        this.pickedAmount = pickedAmount;
    }

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
}
