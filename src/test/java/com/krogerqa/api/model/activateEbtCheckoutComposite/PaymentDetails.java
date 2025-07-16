package com.krogerqa.api.model.activateEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetails {
    private boolean ebt;

    public PaymentDetails() {
    }

    public PaymentDetails(Boolean ebt) {
        this.ebt = ebt;
    }

    public boolean getEbt() {
        return ebt;
    }
}
