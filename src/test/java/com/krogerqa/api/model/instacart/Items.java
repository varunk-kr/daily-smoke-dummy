package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {

    private final String id;
    private final String gtin;
    private final int quantity;
    private final String description;
    Product product;

    public Items(String id, String gtin, int quantity, String description, Product product) {
        this.id = id;
        this.gtin = gtin;
        this.quantity = quantity;
        this.description = description;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public String getGtin() {
        return gtin;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }
}
