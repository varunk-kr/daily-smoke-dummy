package com.krogerqa.api.model.dataDogXml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Message", namespace = "http://www.mt.com/ComOne/namespace")
public class Message {

    @JacksonXmlProperty(isAttribute = true, localName = "schemaLocation", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    public String schemaLocation;

    @JacksonXmlProperty(localName = "ARTSCommonHeader", namespace = "http://www.mt.com/ComOne/namespace")
    public ARTSCommonHeader artsCommonHeader;

    @JacksonXmlProperty(localName = "OrderTransaction", namespace = "http://www.mt.com/ComOne/namespace")
    public OrderTransaction orderTransaction;
}