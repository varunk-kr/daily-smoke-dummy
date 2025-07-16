package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {
    private String nodeId;
    private String slotId;
    private String storeId;
    private Vendor vendor;
    private String capabilities;

    public Network(String nodeId, String slotId, String storeId, Vendor vendor, String capabilities) {
        this.nodeId = nodeId;
        this.slotId = slotId;
        this.storeId = storeId;
        this.vendor = vendor;
        this.capabilities = capabilities;
    }

    public Network() {
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getSlotId() {
        return slotId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getCapabilities() {
        return capabilities;
    }
}
