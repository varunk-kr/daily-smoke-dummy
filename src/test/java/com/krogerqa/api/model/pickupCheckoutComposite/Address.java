package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private boolean residential;
    private ArrayList<String> addressLines;
    private String cityTown;
    private String stateProvince;
    private String postalCode;
    private String countryCode;
    private String phone;

    public Address(boolean residential, ArrayList<String> addressLines, String cityTown, String stateProvince, String postalCode, String countryCode,String phone){
         this.residential = residential;
         this.addressLines=addressLines;
         this.cityTown = cityTown;
         this.stateProvince = stateProvince;
         this.postalCode = postalCode;
         this.countryCode= countryCode;
         this.phone=phone;
    }

    public boolean isResidential() {
        return residential;
    }

    public String getCityTown() {
        return cityTown;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public String getCountryCode() {
        return countryCode;
    }

    public ArrayList<String> getAddressLines() {
        return addressLines;
    }

    public String getPhone() {
        return phone;
    }
}
