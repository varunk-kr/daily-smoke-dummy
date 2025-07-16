package com.krogerqa.api.model.instacartPickCompletedEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstacartCustomerEnRoute {
    private final String id;
    private final String partnerOrderId;
    private final String orderId;
    private final TimeStampPickCompleted timestamp;
    private final String eventType;
    private final HandoffReadinessEventData handoffReadinessEventData;

    public InstacartCustomerEnRoute(String id, String partnerOrderId, String orderId, TimeStampPickCompleted timestamp, String eventType, HandoffReadinessEventData handoffReadinessEventData) {
        this.id = id;
        this.partnerOrderId = partnerOrderId;
        this.timestamp = timestamp;
        this.orderId = orderId;
        this.handoffReadinessEventData = handoffReadinessEventData;
        this.eventType = eventType;
    }

    public String getId() {
        return id;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public TimeStampPickCompleted getTimestamp() {
        return timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public HandoffReadinessEventData getHandoffReadinessEventData() {
        return handoffReadinessEventData;
    }

    public String getEventType() {
        return eventType;
    }
}
