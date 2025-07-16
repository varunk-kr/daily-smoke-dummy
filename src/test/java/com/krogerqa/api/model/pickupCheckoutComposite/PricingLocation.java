package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingLocation {
    private String facilityId;
    private String storeId;

    public PricingLocation() {
    }

    public PricingLocation(String facilityId, String storeId) {
        this.facilityId = facilityId;
        this.storeId = storeId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getStoreId() {
        return storeId;
    }
}
