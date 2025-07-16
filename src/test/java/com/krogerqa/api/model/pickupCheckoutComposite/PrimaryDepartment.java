package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryDepartment {
    private RecapDepartment recapDepartment;

    public PrimaryDepartment() {
    }

    public PrimaryDepartment(RecapDepartment recapDepartment) {
        this.recapDepartment = recapDepartment;
    }

    public RecapDepartment getRecapDepartment() {
        return recapDepartment;
    }
}
