package com.krogerqa.api.model.modifyCheckoutOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Waivers {
    private String id;
    private String amount;
    private String name;

    public Waivers() {
    }

    public Waivers(String id, String amount, String name) {
        this.id = id;
        this.amount = amount;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
