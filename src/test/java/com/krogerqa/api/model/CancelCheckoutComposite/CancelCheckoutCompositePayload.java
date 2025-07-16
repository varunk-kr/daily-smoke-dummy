package com.krogerqa.api.model.CancelCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelCheckoutCompositePayload {
    private String versionKey;
    private String note;

    public CancelCheckoutCompositePayload() {
    }

    public CancelCheckoutCompositePayload(String versionKey, String note) {
        this.versionKey = versionKey;
        this.note = note;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public String getNote() {
        return note;
    }
}
