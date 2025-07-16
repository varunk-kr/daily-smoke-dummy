package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
    private final String id;
    private final ArrayList<Details> details;

    public Items(String id, ArrayList<Details> details){
        this.id = id;
        this.details=details;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Details> getDetails() {
        return details;
    }
}
