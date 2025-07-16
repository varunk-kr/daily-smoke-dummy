package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillmentChannelInfo {
    private String name;
    private KcpProperties kcpProperties;

    public FulfillmentChannelInfo(String name, KcpProperties kcpProperties) {
        this.name = name;
        this.kcpProperties = kcpProperties;
    }

    public FulfillmentChannelInfo() {
    }

    public KcpProperties getProperties() {
        return kcpProperties;
    }

    public void setProperties(KcpProperties kcpProperties) {
        this.kcpProperties = kcpProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
