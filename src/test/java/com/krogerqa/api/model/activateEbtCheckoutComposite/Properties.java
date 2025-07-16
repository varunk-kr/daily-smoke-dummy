package com.krogerqa.api.model.activateEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {
    private String checkoutVersion;

    public Properties() {
    }

    public String getCheckoutVersion() {
        return checkoutVersion;
    }

    public Properties(String checkoutVersion) {
        this.checkoutVersion = checkoutVersion;
    }
}
