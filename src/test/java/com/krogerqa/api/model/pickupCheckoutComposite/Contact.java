package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean smsOptin;
    private Address address;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String phone, Boolean smsOptin,Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.smsOptin = smsOptin;
        this.address=address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getSmsOptin() {
        return smsOptin;
    }

    public Address getAddress() {
        return address;
    }
}
