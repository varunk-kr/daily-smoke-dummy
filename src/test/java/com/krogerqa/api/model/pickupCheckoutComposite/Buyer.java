package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Buyer {
    private String profileId;
    private String loyaltyId;
    private String experience;
    private String email;
    private String dateOfBirth;
    private Address address;

    public Buyer() {
    }
    public Buyer(String profileId, String loyaltyId, String experience, String email, String dateOfBirth, Address address) {
        this.profileId = profileId;
        this.loyaltyId = loyaltyId;
        this.experience = experience;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Buyer(Address address) {
        this.address = address;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getLoyaltyId() {
        return loyaltyId;
    }

    public String getExperience() {
        return experience;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public Buyer(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
