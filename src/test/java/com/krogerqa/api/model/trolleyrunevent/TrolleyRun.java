package com.krogerqa.api.model.trolleyrunevent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrolleyRun {
    private String euid;
    private String eventType;
    private long notedTime;
    private String orderNumber;
    private String storeNumber;
    private AdditionalAttributes additionalAttributes;

    public TrolleyRun(String euid, String eventType, long notedTime, String orderNumber, String storeNumber, AdditionalAttributes additionalAttributes) {
        this.euid = euid;
        this.eventType = eventType;
        this.notedTime = notedTime;
        this.orderNumber = orderNumber;
        this.storeNumber = storeNumber;
        this.additionalAttributes = additionalAttributes;
    }

    public String getEuid() {
        return euid;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEuid(String euid) {
        this.euid = euid;
    }

    public long getNotedTime() {
        return notedTime;
    }

    public void setNotedTime(long notedTime) {
        this.notedTime = notedTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
}
