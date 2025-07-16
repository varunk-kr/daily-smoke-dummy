package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickingCompletedEventData {
    private final String storeId;
    private final ArrayList<Items> items;
    private final ArrayList<Containers> containers;

    public PickingCompletedEventData(String storeId, ArrayList<Items> items, ArrayList<Containers> container) {
        this.storeId = storeId;
        this.items = items;
        this.containers = container;
    }

    public String getStoreId() {
        return storeId;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public ArrayList<Containers> getContainers() {
        return containers;
    }
}
