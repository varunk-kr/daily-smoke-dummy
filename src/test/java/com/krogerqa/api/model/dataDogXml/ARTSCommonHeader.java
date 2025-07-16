package com.krogerqa.api.model.dataDogXml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ARTSCommonHeader {

    @JacksonXmlProperty(isAttribute = true, localName = "MessageType")
    public String messageType;

    @JacksonXmlProperty(localName = "MessageID", namespace = "http://www.mt.com/ComOne/namespace")
    public String messageID;

    @JacksonXmlProperty(localName = "DateTime", namespace = "http://www.mt.com/ComOne/namespace")
    public String dateTime;
}