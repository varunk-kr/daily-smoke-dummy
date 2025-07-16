package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstacartPickCompletedEventPayload {
    private final String id;
    private final String partnerOrderId;
    private final String orderId;
    private final String visualOrderId;
    private final TimeStampPickCompleted timestamp;
    private final String eventType;
    private final PickingCompletedEventData pickingCompletedEventData;

    public InstacartPickCompletedEventPayload(String id, String partnerOrderId, String orderId, String visualOrderId, TimeStampPickCompleted timestamp, String eventType, PickingCompletedEventData pickingCompletedEventData) {
        this.id = id;
        this.partnerOrderId = partnerOrderId;
        this.orderId = orderId;
        this.visualOrderId = visualOrderId;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.pickingCompletedEventData = pickingCompletedEventData;
    }

    public String getId() {
        return id;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVisualOrderId() {
        return visualOrderId;
    }

    public TimeStampPickCompleted getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public PickingCompletedEventData getPickingCompletedEventData() {
        return pickingCompletedEventData;
    }
}
