package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Containers {
    private final String id;
    private final String temp;
    private final String label;
    private final Type type;

    public Containers(String id, String temp, String label, Type type) {
        this.id = id;
        this.temp = temp;
        this.label = label;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTemp() {
        return temp;
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }
}
