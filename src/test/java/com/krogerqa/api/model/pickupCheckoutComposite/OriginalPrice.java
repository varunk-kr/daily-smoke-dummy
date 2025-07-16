package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginalPrice {
    private Regular regular;

    public OriginalPrice() {
    }

    public OriginalPrice(Regular regular) {
        this.regular = regular;
    }

    public Regular getRegular() {
        return regular;
    }
}
