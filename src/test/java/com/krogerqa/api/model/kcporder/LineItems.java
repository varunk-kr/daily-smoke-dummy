package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItems {
    private String gtin;
    private int quantity;
    private String unitOfMeasure;
    private String description;
    private String specialInstructions;
    private String inventoryReservationToken;
    private String requestedBy;
    private Boolean allowSubstitutes;
    private PriceInfo priceInfo;

    public LineItems() {
    }

    public LineItems(String gtin, int quantity, String unitOfMeasure, String description, String specialInstructions, String inventoryReservationToken, String requestedBy, Boolean allowSubstitutes, PriceInfo priceInfo) {
        this.gtin = gtin;
        this.quantity = quantity;
        this.unitOfMeasure = unitOfMeasure;
        this.description = description;
        this.specialInstructions = specialInstructions;
        this.inventoryReservationToken = inventoryReservationToken;
        this.requestedBy = requestedBy;
        this.allowSubstitutes = allowSubstitutes;
        this.priceInfo = priceInfo;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getInventoryReservationToken() {
        return inventoryReservationToken;
    }

    public void setInventoryReservationToken(String inventoryReservationToken) {
        this.inventoryReservationToken = inventoryReservationToken;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Boolean getAllowSubstitutes() {
        return allowSubstitutes;
    }

    public void setAllowSubstitutes(Boolean allowSubstitutes) {
        this.allowSubstitutes = allowSubstitutes;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }
}
