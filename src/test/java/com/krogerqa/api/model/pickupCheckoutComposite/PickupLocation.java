package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickupLocation {
    private String storeId;

    public PickupLocation(String storeId) {
        this.storeId = storeId;
    }

    public PickupLocation() {
    }

    public String getStoreId() {
        return storeId;
    }
}
