package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutCompositeLineItems {
    private Cart cart;
    private String sourceStoreId;
    private String requestedBy;

    public CheckoutCompositeLineItems() {
    }

    public CheckoutCompositeLineItems(Cart cart, String requestedBy,String sourceStoreId) {
        this.cart = cart;
        this.requestedBy = requestedBy;
        this.sourceStoreId = sourceStoreId;
    }

    public Cart getCart() {
        return cart;
    }

    public String getSourceStoreId() {
        return sourceStoreId;
    }

    public String getRequestedBy() {
        return requestedBy;
    }
}
