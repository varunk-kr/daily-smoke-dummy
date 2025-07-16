package com.krogerqa.api.model.modifyCheckoutOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.krogerqa.api.model.activateNonEbtCheckoutComposite.PaymentDetails;
import com.krogerqa.api.model.pickupCheckoutComposite.Buyer;
import com.krogerqa.api.model.pickupCheckoutComposite.CheckoutCompositeLineItems;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyCheckoutPayload {
    private String versionKey;
    private Buyer buyer;
    private List<CheckoutCompositeLineItems> checkoutCompositeLineItems;
    private PriceAdjustment priceAdjustment;
    private PaymentDetails paymentDetails;

    public ModifyCheckoutPayload() {
    }

    public ModifyCheckoutPayload(String versionKey, Buyer buyer, List<CheckoutCompositeLineItems> checkoutCompositeLineItems, PriceAdjustment priceAdjustment, PaymentDetails paymentDetails) {
        this.versionKey = versionKey;
        this.buyer = buyer;
        this.checkoutCompositeLineItems = checkoutCompositeLineItems;
        this.priceAdjustment = priceAdjustment;
        this.paymentDetails = paymentDetails;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public List<CheckoutCompositeLineItems> getLineItems() {
        return checkoutCompositeLineItems;
    }

    public PriceAdjustment getPriceAdjustment() {
        return priceAdjustment;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }
}
