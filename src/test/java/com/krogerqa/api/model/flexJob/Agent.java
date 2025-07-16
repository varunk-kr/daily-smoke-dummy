package com.krogerqa.api.model.flexJob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agent {
    private final String id;

    public Agent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
