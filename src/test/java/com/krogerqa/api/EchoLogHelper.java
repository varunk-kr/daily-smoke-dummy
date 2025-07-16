package com.krogerqa.api;

import com.kroger.commons.krypt.aes.AESMasterEncryptor;
import com.kroger.commons.krypt.core.StringEncryptor;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class EchoLogHelper {

    private  static EchoLogHelper instance;
    private EchoLogHelper (){}
    public synchronized static EchoLogHelper getInstance() {
        if (instance == null) {
            synchronized (EchoLogHelper.class)
            {
                if (instance == null) {
                    instance = new EchoLogHelper();
                }
            }
        }
        return instance;
    }

    private static final String COR_MESSAGE = "COR - processEndOrderReleased";
    private static final String COR_MISMATCH_MESSAGE = "Customer Order Released message did not match";
    private static final String KLOG_MESSAGE = "klogSent_Success";
    private static final String KLOG_UNITID = "KLog.Transaction.BusinessUnit.UnitID";
    private static final String KLOG_ORDERID = "KLog.Transaction.OrderFulfillmentApplication.OrderNumber";
    private static final String KLOG_CUSTOMERID = "KLog.Transaction.RetailTransaction.LoyaltyAccount.CustomerID";
    private static final String KLOG_GROSSPOSITIVE = "KLog.Transaction.GrossPositive";
    ApiUtils apiUtils = new ApiUtils();

    public void controlTowerApiResponseVerification(String orderId) {
        JsonPath jsonPath = new JsonPath(controlTowerGetApi(orderId).body().asPrettyString());
        String controlTowerResponse = (jsonPath.getString("hits.hits[0]._source.message"));
        Assert.assertTrue(controlTowerResponse.equalsIgnoreCase(COR_MESSAGE), COR_MISMATCH_MESSAGE);
    }

    public void verifyKLogResponse(String orderNumber, String storeId, String customerPlusCardId, String amountProcessed) {
        Response responseBody = kLogGetApi(orderNumber);
        JsonPath jsonPath = new JsonPath(responseBody.asString());
        String kLogResponse = (jsonPath.getString("hits.hits[0]._source.messageDetail.kLogMessage"));
        String xmlFile = decryptKLog(kLogResponse);
        Assert.assertTrue(xmlFile.length() > 0, "Did not get Decrypted xml file");
        String unitId = XmlPath.with(xmlFile).get(KLOG_UNITID);
        String orderId = XmlPath.with(xmlFile).get(KLOG_ORDERID);
        String customerId = XmlPath.with(xmlFile).get(KLOG_CUSTOMERID);
        String grossPositive = XmlPath.with(xmlFile).get(KLOG_GROSSPOSITIVE);
        String orderAmount = amountProcessed.substring(1) + "00";
        Assert.assertEquals(unitId, storeId, "store id validation is not successful");
        Assert.assertEquals(orderId, orderNumber, "order id validation is not successful");
        Assert.assertEquals(grossPositive, orderAmount, "gross positive validation is not successful");
        if (!(customerPlusCardId.startsWith("2"))) {
            Assert.assertEquals(customerId, customerPlusCardId.substring(0, customerPlusCardId.length() - 1), "customer plus card validation is not successful");
        } else {
            Assert.assertEquals(customerId, customerPlusCardId, "customer plus card validation is not successful");
        }
    }

    private String getEchoPayload(String message, String orderId) {
        JSONObject root = new JSONObject();
        Map<String, String> matchMessage = new HashMap<>();
        matchMessage.put("message", message);
        Map<String, String> matchOrder = new HashMap<>();
        if (message.equalsIgnoreCase(COR_MESSAGE)) {
            matchOrder.put("messageDetail.OrderNo", orderId);
        } else if (message.equalsIgnoreCase(KLOG_MESSAGE)) {
            matchOrder.put("messageDetail.orderNumber", orderId);
        }
        JSONArray mustArray = new JSONArray();
        mustArray.put(new JSONObject().put("match", matchMessage));
        mustArray.put(new JSONObject().put("match", matchOrder));
        root.put("query", new JSONObject().put("bool", new JSONObject().put("must", mustArray)));
        return String.valueOf(root);
    }

    private Response controlTowerGetApi(String orderId) {
        return apiUtils.getApi(PropertyUtils.getSpmUserId(), PropertyUtils.getSpmPassword(), getEchoPayload(COR_MESSAGE, orderId), PropertyUtils.getControlTowerEventEndpoint(), new HashMap<>(), new HashMap<>());
    }

    private InputStream unzipText(String text) throws IOException {
        byte[] messageBytes = Base64.getDecoder().decode(text);
        return new GZIPInputStream(new ByteArrayInputStream(messageBytes));
    }

    private String unzipTextToString(String text) throws IOException {
        InputStream is = unzipText(text);
        String result;
        try (Scanner scanner = new Scanner(is)) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    private String decryptKLog(String kLog) {
        String stageKey = PropertyUtils.getKlogDecryptionStageKey();
        String plainValueStage = null;
        try {
            StringEncryptor stringEncryptor = new AESMasterEncryptor(stageKey).getStringEncryptor();
            plainValueStage = stringEncryptor.decrypt(kLog);
            plainValueStage = unzipTextToString(plainValueStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainValueStage;
    }

    private Response kLogGetApi(String orderId) {
        return apiUtils.getApi(PropertyUtils.getSpmUserId(), PropertyUtils.getSpmPassword(), getEchoPayload(KLOG_MESSAGE, orderId), PropertyUtils.getKlogEndpoint(), new HashMap<>(), new HashMap<>());
    }
}
