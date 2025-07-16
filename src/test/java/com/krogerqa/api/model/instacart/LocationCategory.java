package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationCategory {
    private final String type;
    StoreInformation storeInformationObject;

    public LocationCategory(String type, StoreInformation storeInformation) {
        this.type = type;
        this.storeInformationObject = storeInformation;
    }

    public String getType() {
        return type;
    }

    public StoreInformation getStoreInformation() {
        return storeInformationObject;
    }
}
