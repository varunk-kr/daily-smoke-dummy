package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreInformation {
    private final String storeId;

    public StoreInformation(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }
}
