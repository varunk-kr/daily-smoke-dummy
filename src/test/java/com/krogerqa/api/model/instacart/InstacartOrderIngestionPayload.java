package com.krogerqa.api.model.instacart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstacartOrderIngestionPayload {
    private final String id;
    private final String partnerOrderId;
    private final String visualOrderId;
    TimeStamp timeStampObject;
    private final String eventType;
    CreateOrderEventData createOrderEventDataObject;

    public InstacartOrderIngestionPayload(String id, String partnerOrderId, String visualOrderId, TimeStamp timestampObject, String eventType, CreateOrderEventData createOrderEventDataObject) {
        this.id = id;
        this.partnerOrderId = partnerOrderId;
        this.visualOrderId = visualOrderId;
        this.timeStampObject = timestampObject;
        this.eventType = eventType;
        this.createOrderEventDataObject = createOrderEventDataObject;
    }

    public String getId() {
        return id;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public String getVisualOrderId() {
        return visualOrderId;
    }

    public TimeStamp getTimestamp() {
        return timeStampObject;
    }

    public String getEventType() {
        return eventType;
    }

    public CreateOrderEventData getCreateOrderEventData() {
        return createOrderEventDataObject;
    }
}
