package com.krogerqa.api.model.activateNonEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivateNonEbtCheckoutCompositePayload {
    private String versionKey;
    private PaymentDetails paymentDetails;

    public ActivateNonEbtCheckoutCompositePayload() {
    }

    public ActivateNonEbtCheckoutCompositePayload(String versionKey, PaymentDetails paymentDetails) {
        this.versionKey = versionKey;
        this.paymentDetails = paymentDetails;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }
}
