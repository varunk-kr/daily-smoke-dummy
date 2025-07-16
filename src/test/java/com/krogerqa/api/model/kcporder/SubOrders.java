package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubOrders {
    private String instanceOf;
    private CostSummary costSummary;
    private FulfillmentChannelInfo fulfillmentChannelInfo;
    private PickupInfo pickupInfo;
    private String specialInstructions;
    private List<PriceAdjustments> priceAdjustments;
    private List<LineItems> lineItems;
    public SubOrders() {
    }
    public SubOrders(String instanceOf, CostSummary costSummary, FulfillmentChannelInfo fulfillmentChannelInfo, PickupInfo pickupInfo, String specialInstructions, List<PriceAdjustments> priceAdjustments, List<LineItems> lineItems) {
        this.instanceOf = instanceOf;
        this.costSummary = costSummary;
        this.fulfillmentChannelInfo = fulfillmentChannelInfo;
        this.pickupInfo = pickupInfo;
        this.specialInstructions = specialInstructions;
        this.priceAdjustments = priceAdjustments;
        this.lineItems = lineItems;
    }

    public List<LineItems> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItems> lineItems) {
        this.lineItems = lineItems;
    }

    public PickupInfo getPickupInfo() {
        return pickupInfo;
    }

    public void setPickupInfo(PickupInfo pickupInfo) {
        this.pickupInfo = pickupInfo;
    }

    public FulfillmentChannelInfo getFulfillmentChannelInfo() {
        return fulfillmentChannelInfo;
    }

    public void setFulfillmentChannelInfo(FulfillmentChannelInfo fulfillmentChannelInfo) {
        this.fulfillmentChannelInfo = fulfillmentChannelInfo;
    }

    public String getInstanceOf() {
        return instanceOf;
    }

    public void setInstanceOf(String instanceOf) {
        this.instanceOf = instanceOf;
    }

    public CostSummary getCostSummary() {
        return costSummary;
    }

    public void setCostSummary(CostSummary costSummary) {
        this.costSummary = costSummary;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public List<PriceAdjustments> getPriceAdjustments() {
        return priceAdjustments;
    }

    public void setPriceAdjustments(List<PriceAdjustments> priceAdjustments) {
        this.priceAdjustments = priceAdjustments;
    }
}
