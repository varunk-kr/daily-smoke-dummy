package com.krogerqa.api.model.pclAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssigningPcl {

    private Data data;

    public AssigningPcl() {
    }

    public AssigningPcl(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
