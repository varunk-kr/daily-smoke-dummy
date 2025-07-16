package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private float miscAccount;
    private String itemTypeCode;
    private float tareValue;
    private String id;
    private boolean alcohol;
    private String averageWeightPerUnit;
    private String customerFacingSize;
    private String krogerOwnedEcommerceDescription;
    private boolean randomWeight;
    private String snapEligible;
    private FamilyTree familyTree;

    private String sellBy;

    public Product(float miscAccount, String itemTypeCode, float tareValue, String id, boolean alcohol, String averageWeightPerUnit, String customerFacingSize, String krogerOwnedEcommerceDescription, boolean randomWeight, String snapEligible, FamilyTree familyTree, String sellBy) {
        this.miscAccount = miscAccount;
        this.itemTypeCode = itemTypeCode;
        this.tareValue = tareValue;
        this.id = id;
        this.alcohol = alcohol;
        this.averageWeightPerUnit = averageWeightPerUnit;
        this.customerFacingSize = customerFacingSize;
        this.krogerOwnedEcommerceDescription = krogerOwnedEcommerceDescription;
        this.randomWeight = randomWeight;
        this.snapEligible = snapEligible;
        this.familyTree = familyTree;
        this.sellBy = sellBy;
    }

    public Product() {
    }

    public float getMiscAccount() {
        return miscAccount;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public float getTareValue() {
        return tareValue;
    }

    public String getId() {
        return id;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public String getAverageWeightPerUnit() {
        return averageWeightPerUnit;
    }

    public String getCustomerFacingSize() {
        return customerFacingSize;
    }

    public String getKrogerOwnedEcommerceDescription() {
        return krogerOwnedEcommerceDescription;
    }

    public boolean isRandomWeight() {
        return randomWeight;
    }

    public String getSnapEligible() {
        return snapEligible;
    }

    public FamilyTree getFamilyTree() {
        return familyTree;
    }

    public String getSellBy() {
        return sellBy;
    }
}
