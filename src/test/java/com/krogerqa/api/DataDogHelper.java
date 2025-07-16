package com.krogerqa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.krogerqa.api.model.dataDogXml.ARTSCommonHeader;
import com.krogerqa.api.model.dataDogXml.Message;
import com.krogerqa.api.model.dataDogXml.Order;
import com.krogerqa.api.model.dataDogXml.OrderTransaction;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.response.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;


public class DataDogHelper {

    private static DataDogHelper instance;
    ApiUtils apiUtils = new ApiUtils();
    Response dataDogResponse;
    BaseCommands baseCommands = new BaseCommands();

    private DataDogHelper() {
    }

    public synchronized static DataDogHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DataDogHelper.class) {
            if (instance == null) {
                instance = new DataDogHelper();
            }
        }
        return instance;
    }

    public void getDataDogXmlResponse(HashMap<String, String> testOutputData, String status) {
        if (status.equalsIgnoreCase(Constants.ScDataDog.IN_PROCESS)) {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            Message message = new Message();
            message.schemaLocation = Constants.ScDataDog.SCHEMA_LOCATION;
            ARTSCommonHeader header = new ARTSCommonHeader();
            header.messageType = Constants.ScDataDog.REQUEST;
            header.messageID = Constants.ScDataDog.MESSAGE_ID;
            header.dateTime = Constants.ScDataDog.DATE_TIME;
            Order order = new Order();
            order.uuid = testOutputData.get(ExcelUtils.ORDER_NUMBER) + testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
            OrderTransaction transaction = new OrderTransaction();
            transaction.actionCode = "Read";
            transaction.order = order;
            message.artsCommonHeader = header;
            message.orderTransaction = transaction;
            String rawXml;
            try {
                rawXml = xmlMapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String cleaned = rawXml.replaceFirst("<Message[^>]*>", Constants.ScDataDog.MESSAGE_TAG_DATA_DOG_XML);
            dataDogResponse = apiUtils.postApiWithXmlPayload("", "", cleaned, PropertyUtils.getDataDogXmlEndpoint(), new HashMap<>(), new HashMap<>());
            sendServiceCounterEventToCue(dataDogResponse.asPrettyString(), Constants.ScDataDog.IN_PROCESS);
        } else {
            sendServiceCounterEventToCue(dataDogResponse.asPrettyString(), Constants.ScDataDog.FINISHED);
        }
        baseCommands.webpageRefresh();
    }

    public void sendServiceCounterEventToCue(String dataDogResponse, String event) {
        String updatedPayload;
        if (event.equalsIgnoreCase(Constants.ScDataDog.IN_PROCESS)) {
            updatedPayload = dataDogResponse.replace(Constants.ScDataDog.READ, Constants.ScDataDog.UPDATE).replaceAll(Constants.ScDataDog.SALE, Constants.ScDataDog.IN_PROCESS);
        } else {
            updatedPayload = dataDogResponse.replace(Constants.ScDataDog.READ, Constants.ScDataDog.UPDATE).replaceAll(Constants.ScDataDog.SALE, Constants.ScDataDog.FINISHED);
        }
        String inputXml = updatedPayload;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc;
        try {
            doc = builder.parse(new ByteArrayInputStream(inputXml.getBytes()));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        NodeList lineItems = doc.getElementsByTagNameNS("*", "LineItem");
        for (int i = 0; i < lineItems.getLength(); i++) {
            Node lineItem = lineItems.item(i);
            Document newDoc = builder.newDocument();
            Node importedRoot = newDoc.importNode(doc.getDocumentElement(), true);
            newDoc.appendChild(importedRoot);
            NodeList allLineItems = newDoc.getElementsByTagNameNS("*", "LineItem");
            for (int j = allLineItems.getLength() - 1; j >= 0; j--) {
                allLineItems.item(j).getParentNode().removeChild(allLineItems.item(j));
            }
            Node importedLineItem = newDoc.importNode(lineItem, true);
            Node orderNode = newDoc.getElementsByTagNameNS("*", "Order").item(0);
            orderNode.appendChild(importedLineItem);
            StringWriter writer = new StringWriter();
            Transformer transformer;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException e) {
                throw new RuntimeException(e);
            }
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            try {
                transformer.transform(new DOMSource(newDoc), new StreamResult(writer));
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
            String resultXml = writer.toString();
            apiUtils.postApiWithXmlPayload("", "", resultXml, PropertyUtils.getDataDogXmlEndpoint(), new HashMap<>(), new HashMap<>());
        }
    }
}

