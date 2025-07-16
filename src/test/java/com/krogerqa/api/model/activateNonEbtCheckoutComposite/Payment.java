package com.krogerqa.api.model.activateNonEbtCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {
    private String id;
    private List<Transactions> transactions;

    public Payment() {
    }

    public Payment(String id, List<Transactions> transactions) {
        this.id = id;
        this.transactions = transactions;
    }

    public String getId() {
        return id;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }
}
