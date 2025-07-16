package com.krogerqa.api.model.kcporder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KcpPayload {
    private String entity;
    private String id;
    private TimeStamp timestamp;
    private String source;
    private String orderId;
    private CustomerInfo customerInfo;
    private String paymentId;
    private String paymentMethod;
    private ShoppingContext shoppingContext;
    private CostSummary costSummary;
    private List<SubOrders> subOrders;

    public KcpPayload() {
    }

    public KcpPayload(String entity, String id, TimeStamp timestamp, String source, String orderId, CustomerInfo customerInfo, String paymentId, String paymentMethod, ShoppingContext shoppingContext, CostSummary costSummary, List<SubOrders> subOrders) {
        this.entity = entity;
        this.id = id;
        this.timestamp = timestamp;
        this.source = source;
        this.orderId = orderId;
        this.customerInfo = customerInfo;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.shoppingContext = shoppingContext;
        this.costSummary = costSummary;
        this.subOrders = subOrders;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public TimeStamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(TimeStamp timestamp) {
        this.timestamp = timestamp;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ShoppingContext getShoppingContext() {
        return shoppingContext;
    }

    public void setShoppingContext(ShoppingContext shoppingContext) {
        this.shoppingContext = shoppingContext;
    }

    public CostSummary getCostSummary() {
        return costSummary;
    }

    public void setCostSummary(CostSummary costSummary) {
        this.costSummary = costSummary;
    }

    public List<SubOrders> getSubOrders() {
        return subOrders;
    }

    public void setSubOrder(List<SubOrders> subOrders) {
        this.subOrders = subOrders;
    }
}
