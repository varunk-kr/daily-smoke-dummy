package com.krogerqa.api.model.dataDogXml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OrderTransaction {

    @JacksonXmlProperty(isAttribute = true, localName = "ActionCode")
    public String actionCode;

    @JacksonXmlProperty(localName = "Order", namespace = "http://www.mt.com/ComOne/namespace")
    public Order order;
}
