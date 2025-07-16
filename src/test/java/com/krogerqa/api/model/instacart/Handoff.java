package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Handoff {
    private final String modality;
    LocationCategory locationCategoryObject;
    Window windowObject;

    public Handoff(String modality, LocationCategory locationCategoryObject, Window windowObject) {
        this.modality = modality;
        this.locationCategoryObject = locationCategoryObject;
        this.windowObject = windowObject;
    }

    public String getModality() {
        return modality;
    }

    public LocationCategory getLocationCategory() {
        return locationCategoryObject;
    }

    public Window getWindow() {
        return windowObject;
    }
}

