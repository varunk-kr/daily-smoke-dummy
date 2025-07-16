package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fulfillment {
    private Contact contact;
    private PickupLocation pickupLocation;
    private PricingLocation pricingLocation;
    private String instructions;
    private Promise promise;

    public Fulfillment() {
    }

    public Fulfillment(Contact contact) {
        this.contact = contact;
    }

    public Fulfillment(Contact contact, PickupLocation pickupLocation, PricingLocation pricingLocation, String instructions, Promise promise) {
        this.contact = contact;
        this.pickupLocation = pickupLocation;
        this.pricingLocation = pricingLocation;
        this.instructions = instructions;
        this.promise = promise;
    }

    public Contact getContact() {
        return contact;
    }

    public PickupLocation getPickupLocation() {
        return pickupLocation;
    }

    public PricingLocation getPricingLocation() {
        return pricingLocation;
    }

    public String getInstructions() {
        return instructions;
    }

    public Promise getPromise() {
        return promise;
    }
}
