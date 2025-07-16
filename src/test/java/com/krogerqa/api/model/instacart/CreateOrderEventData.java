package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderEventData {

    private final String specialInstructions;
    Customer customerObject;
    Handoff handoffObject;
    ArrayList<Items> items;

    public CreateOrderEventData(String specialInstructions, Customer customerObject, Handoff handoffObject, ArrayList<Items> items) {
        this.specialInstructions = specialInstructions;
        this.customerObject = customerObject;
        this.handoffObject = handoffObject;
        this.items = items;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public Customer getCustomer() {
        return customerObject;
    }

    public Handoff getHandoff() {
        return handoffObject;
    }

    public ArrayList<Items> getItems() {
        return items;
    }
}
