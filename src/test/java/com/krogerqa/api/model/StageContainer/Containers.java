package com.krogerqa.api.model.StageContainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Containers {
    private String id;

    public Containers() {
    }

    public Containers(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
