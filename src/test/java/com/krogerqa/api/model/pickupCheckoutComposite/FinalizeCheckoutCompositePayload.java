package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinalizeCheckoutCompositePayload {
    private String versionKey;

    public FinalizeCheckoutCompositePayload() {
    }

    public FinalizeCheckoutCompositePayload(String versionKey) {
        this.versionKey = versionKey;
    }

    public String getVersionKey() {
        return versionKey;
    }
}
