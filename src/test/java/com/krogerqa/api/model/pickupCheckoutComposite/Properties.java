package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {
    private String checkoutVersion;

    public Properties() {
    }

    public Properties(String checkoutVersion) {
        this.checkoutVersion = checkoutVersion;
    }

    public String getCheckoutVersion() {
        return checkoutVersion;
    }
}
