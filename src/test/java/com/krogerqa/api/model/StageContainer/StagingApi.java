package com.krogerqa.api.model.StageContainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StagingApi {
    private Containers containers;

    public StagingApi(Containers containers) {
        this.containers = containers;
    }

    public StagingApi() {
    }

    public Containers getContainers() {
        return containers;
    }
}
