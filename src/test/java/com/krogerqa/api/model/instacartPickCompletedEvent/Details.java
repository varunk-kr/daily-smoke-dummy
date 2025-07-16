package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Details {
    private final int quantity;
    private final String gtin;
    private final String status;
    private final String fulfillmentType;
    private final String note;

    public Details(int quantity, String gtin, String status, String fulfillmentType, String note) {
        this.quantity = quantity;
        this.gtin = gtin;
        this.status = status;
        this.fulfillmentType = fulfillmentType;
        this.note = note;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getGtin() {
        return gtin;
    }

    public String getStatus() {
        return status;
    }

    public String getFulfillmentType() {
        return fulfillmentType;
    }

    public String getNote() {
        return note;
    }
}
