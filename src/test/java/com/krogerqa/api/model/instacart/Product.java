package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private final String alcohol;

    public Product(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getAlcohol() {
        return alcohol;
    }
}
