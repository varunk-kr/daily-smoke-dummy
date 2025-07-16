package com.krogerqa.api.model.flexJob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTime {
    private final String value;
    private final String timezone;

    public DateTime(String value, String timezone) {
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
