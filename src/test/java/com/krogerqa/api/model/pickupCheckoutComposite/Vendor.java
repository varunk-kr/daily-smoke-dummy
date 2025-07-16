package com.krogerqa.api.model.pickupCheckoutComposite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vendor {
    private String id;
    private String name;
    private String reservationId;

    public Vendor(){

    }

    public Vendor(String id, String name, String reservationId){
        this.id = id;
        this.name=name;
        this.reservationId = reservationId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReservationId() {
        return reservationId;
    }
}
