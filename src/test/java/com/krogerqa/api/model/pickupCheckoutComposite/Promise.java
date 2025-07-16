package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Promise {
    private String id;
    private ArrayList<Selections> selections;

    public Promise() {
    }

    public Promise(String id, ArrayList<Selections> selections) {
        this.id = id;
        this.selections = selections;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Selections> getSelections() {
        return selections;
    }
}
