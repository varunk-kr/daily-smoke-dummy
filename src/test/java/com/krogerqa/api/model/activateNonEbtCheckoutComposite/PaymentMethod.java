package com.krogerqa.api.model.activateNonEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod {
    private String type;
    private String vaultId;
    private String subType;
    private String lastFourOfCard;

    public PaymentMethod() {
    }

    public PaymentMethod(String type, String vaultId, String subType, String lastFourOfCard) {
        this.type = type;
        this.vaultId = vaultId;
        this.subType = subType;
        this.lastFourOfCard = lastFourOfCard;
    }

    public PaymentMethod(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getVaultId() {
        return vaultId;
    }

    public String getSubType() {
        return subType;
    }

    public String getLastFourOfCard() {
        return lastFourOfCard;
    }
}
