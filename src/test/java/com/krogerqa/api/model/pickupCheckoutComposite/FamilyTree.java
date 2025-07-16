package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FamilyTree {
    private PrimaryDepartment primaryDepartment;
    private Commodity commodity;
    private Subcommodity subCommodity;

    public FamilyTree() {
    }

    public FamilyTree(PrimaryDepartment primaryDepartment, Commodity commodity, Subcommodity subCommodity) {
        this.primaryDepartment = primaryDepartment;
        this.commodity = commodity;
        this.subCommodity = subCommodity;
    }

    public PrimaryDepartment getPrimaryDepartment() {
        return primaryDepartment;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public Subcommodity getSubCommodity() {
        return subCommodity;
    }
}
