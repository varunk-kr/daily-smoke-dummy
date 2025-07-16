package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecapDepartment {
    private Department department;

    public RecapDepartment() {
    }

    public RecapDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }
}
