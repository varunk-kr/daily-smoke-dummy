package com.krogerqa.api.model.itempickevent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPick {

    private AdditionalAttributes additionalAttributes;
    private String euid;
    private String eventType;
    private long notedTime;
    private String orderNumber;
    private long receivedTime;
    private String storeNumber;

    public ItemPick(AdditionalAttributes additionalAttributes, String euid, String eventType, long notedTime, String orderNumber, long receivedTime, String storeNumber) {
        this.additionalAttributes = additionalAttributes;
        this.euid = euid;
        this.eventType = eventType;
        this.notedTime = notedTime;
        this.orderNumber = orderNumber;
        this.receivedTime = receivedTime;
        this.storeNumber = storeNumber;
    }

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public String getEuid() {
        return euid;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }
}
