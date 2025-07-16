package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Regular {
    private String price;

    public Regular() {
    }

    public Regular(String price){
        this.price=price;
    }

    public String getPrice() {
        return price;
    }
}
