package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Window {
    private String value;
    private String timezone;

    public Window(String value, String timezone) {
        this.timezone = timezone;
        this.value = value;
    }

    public Window() {
    }

    public String getValue() {
        return value;
    }

    public String getTimezone() {
        return timezone;
    }
}
