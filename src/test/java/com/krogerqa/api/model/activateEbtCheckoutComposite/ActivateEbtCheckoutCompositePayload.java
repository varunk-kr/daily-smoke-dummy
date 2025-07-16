package com.krogerqa.api.model.activateEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivateEbtCheckoutCompositePayload {
private PaymentDetails paymentDetails;
private Properties properties;
private String versionKey;

    public ActivateEbtCheckoutCompositePayload() {
    }

    public ActivateEbtCheckoutCompositePayload(PaymentDetails paymentDetails, Properties properties, String versionKey) {
        this.paymentDetails = paymentDetails;
        this.properties = properties;
        this.versionKey = versionKey;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getVersionKey() {
        return versionKey;
    }
}
