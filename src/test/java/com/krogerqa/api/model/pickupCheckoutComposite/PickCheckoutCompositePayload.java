package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
    import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickCheckoutCompositePayload {
    private String modalityType;
    private String cartId;
    private Buyer buyer;
    private Fulfillment fulfillment;
    private List<CheckoutCompositeLineItems> checkoutCompositeLineItems;
    private Properties properties;

    public PickCheckoutCompositePayload() {
    }

    public PickCheckoutCompositePayload(String modalityType, String cartId, Buyer buyer, Fulfillment fulfillment, List<CheckoutCompositeLineItems> checkoutCompositeLineItems, Properties properties) {
        this.modalityType = modalityType;
        this.cartId = cartId;
        this.buyer = buyer;
        this.fulfillment = fulfillment;
        this.checkoutCompositeLineItems = checkoutCompositeLineItems;
        this.properties = properties;
    }

    public String getModalityType() {
        return modalityType;
    }

    public String getCartId() {
        return cartId;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public Fulfillment getFulfillment() {
        return fulfillment;
    }

    public List<CheckoutCompositeLineItems> getLineItems() {
        return checkoutCompositeLineItems;
    }

    public Properties getProperties() {
        return properties;
    }
}
