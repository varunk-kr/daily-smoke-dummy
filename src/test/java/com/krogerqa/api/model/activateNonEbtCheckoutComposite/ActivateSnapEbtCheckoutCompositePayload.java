package com.krogerqa.api.model.activateNonEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.krogerqa.api.model.pickupCheckoutComposite.Buyer;
import com.krogerqa.api.model.pickupCheckoutComposite.Fulfillment;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivateSnapEbtCheckoutCompositePayload {

    private String versionKey;
    private PaymentDetails paymentDetails;
    private Buyer buyer;
    private Fulfillment fulfillment;

    public ActivateSnapEbtCheckoutCompositePayload() {
    }

    public ActivateSnapEbtCheckoutCompositePayload(String versionKey, PaymentDetails paymentDetails, Buyer buyer, Fulfillment fulfillment) {
        this.versionKey = versionKey;
        this.paymentDetails = paymentDetails;
        this.buyer = buyer;
        this.fulfillment = fulfillment;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public Fulfillment getFulfillment() {
        return fulfillment;
    }
}
