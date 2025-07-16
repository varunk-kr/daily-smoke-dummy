package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactInfo {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean smsOptIn;
    private String smsPhoneNumber;

    public ContactInfo() {
    }

    public ContactInfo(String firstName, String lastName, String phoneNumber, Boolean smsOptIn, String smsPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.smsOptIn = smsOptIn;
        this.smsPhoneNumber = smsPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getSmsOptIn() {
        return smsOptIn;
    }

    public void setSmsOptIn(Boolean smsOptIn) {
        this.smsOptIn = smsOptIn;
    }

    public String getSmsPhoneNumber() {
        return smsPhoneNumber;
    }

    public void setSmsPhoneNumber(String smsPhoneNumber) {
        this.smsPhoneNumber = smsPhoneNumber;
    }

}
