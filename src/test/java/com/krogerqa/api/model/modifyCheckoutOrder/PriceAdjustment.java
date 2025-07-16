package com.krogerqa.api.model.modifyCheckoutOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceAdjustment {
    private List<Waivers> waivers;

    public PriceAdjustment() {
    }

    public PriceAdjustment(List<Waivers> waivers) {
        this.waivers = waivers;
    }

    public List<Waivers> getWaivers() {
        return waivers;
    }
}
