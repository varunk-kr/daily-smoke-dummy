package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {
    private String id;
    private String gtin13;
    private int quantity;
    private Product product;
    private OriginalPrice originalPrice;

    public Cart() {
    }

    public Cart(String id, String gtin13, int quantity, Product product, OriginalPrice originalPrice) {
        this.id = id;
        this.gtin13 = gtin13;
        this.quantity = quantity;
        this.product = product;
        this.originalPrice = originalPrice;
    }

    public String getId() {
        return id;
    }

    public String getGtin13() {
        return gtin13;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public OriginalPrice getOriginalPrice() {
        return originalPrice;
    }
}
