package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickupInfo {
    private ContactInfo contactInfo;
    private BeginDateTime beginDateTime;
    private EndDateTime endDateTime;
    private String locationId;
    private String reservationId;

    public PickupInfo() {
    }

    public PickupInfo(ContactInfo contactInfo, BeginDateTime beginDateTime, EndDateTime endDateTime, String locationId, String reservationId) {
        this.contactInfo = contactInfo;
        this.beginDateTime = beginDateTime;
        this.endDateTime = endDateTime;
        this.locationId = locationId;
        this.reservationId = reservationId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public BeginDateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(BeginDateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public EndDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(EndDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
