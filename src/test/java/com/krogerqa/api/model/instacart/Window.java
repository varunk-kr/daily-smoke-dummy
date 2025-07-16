package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Window {
    private final String value;
    private final String timezone;

    public Window(String value, String timezone) {
        this.value = value;
        this.timezone = timezone;
    }

    public String getValue() {
        return value;
    }

    public String getTimezone() {
        return timezone;
    }
}
