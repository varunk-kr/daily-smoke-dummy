package com.krogerqa.api.model.dataDogXml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Order {

    @JacksonXmlProperty(localName = "UUID", namespace = "http://www.mt.com/ComOne/namespace")
    public String uuid;
}