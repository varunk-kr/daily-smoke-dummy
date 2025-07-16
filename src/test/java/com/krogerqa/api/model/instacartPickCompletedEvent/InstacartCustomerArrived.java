package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstacartCustomerArrived {
    private final String id;
    private final String partnerOrderId;
    private final String orderId;
    private final TimeStampPickCompleted timestamp;
    private final String eventType;
    private final HandoffReadinessEventArrivedData handoffReadinessEventData;

    public InstacartCustomerArrived(String id, String partnerOrderId, String orderId, TimeStampPickCompleted timestamp, String eventType, HandoffReadinessEventArrivedData handoffReadinessEventData) {
        this.id = id;
        this.partnerOrderId = partnerOrderId;
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.handoffReadinessEventData = handoffReadinessEventData;
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

    public TimeStampPickCompleted getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public HandoffReadinessEventArrivedData getHandoffReadinessEventData() {
        return handoffReadinessEventData;
    }
}
