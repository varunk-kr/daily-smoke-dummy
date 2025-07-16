package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Selections {
    private String id;
    private Vendor vendor;
    private String code;
    private String cost;
    private String costId;
    private Window window;
    private List<Network> network;

    public Selections() {
    }

    public Selections(String id, Vendor vendor, String code, String cost, String costId, Window window, List<Network> network) {
        this.id = id;
        this.vendor = vendor;
        this.code = code;
        this.cost = cost;
        this.costId = costId;
        this.window = window;
        this.network = network;
    }

    public String getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public String getCode() {
        return code;
    }

    public String getCost() {
        return cost;
    }

    public String getCostId() {
        return costId;
    }

    public Window getWindow() {
        return window;
    }

    public List<Network> getNetwork() {
        return network;
    }
}
