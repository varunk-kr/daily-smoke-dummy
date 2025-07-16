package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subcommodity {
    private String code;
    private String name;

    public Subcommodity(String code, String name){
        this.code=code;
        this.name=name;
    }

    public Subcommodity() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
