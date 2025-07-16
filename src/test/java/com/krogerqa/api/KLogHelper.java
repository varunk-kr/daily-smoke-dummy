package com.krogerqa.api;

import com.krogerqa.utils.Constants;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;


public class KLogHelper {

    private static KLogHelper instance;
    private static final String clientId = "e2eklog-243261243034242f734c656a32612e485070564e59647463516d3847753146353362345a6c34356533484e374142636f47632e4575545853754f53614654157022536226984";
    private static final String clientKeyPassword = "i5VIKlcI4cKcSVCwCXpF9aXUR_8mrNKaeovoVYEC";
    private static final String readScope = "urn:com:kroger:ecommpos:klog:read";
    private static final String KLOG_UNIT_ID = "KLog.Transaction.BusinessUnit.UnitID";
    private static final String KLOG_ORDER_ID = "KLog.Transaction.OrderFulfillmentApplication.OrderNumber";
    private static final String KLOG_CUSTOMER_ID = "KLog.Transaction.RetailTransaction.LoyaltyAccount.CustomerID";
    private static final String KLOG_GROSS_POSITIVE = "KLog.Transaction.GrossPositive";


    private KLogHelper() {
    }

    public synchronized static KLogHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (KLogHelper.class) {
            if (instance == null) {
                instance = new KLogHelper();
            }
        }
        return instance;
    }

    public String getKLogAccessToken() {
        HashMap<String, String> formParams = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        formParams.put("client_id", clientId);
        formParams.put("client_secret", clientKeyPassword);
        formParams.put("scope", readScope);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response res = new ApiUtils().postPayloadForFormUrlencodedWithHeaders(formParams, headers, PropertyUtils.getKlogAuthenticationTokenEndpoint());
        return res.jsonPath().getString("access_token");
    }

    public Response getKLogXml(String orderNumber) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.tranType", "sale");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/xml");
        return new ApiUtils().getApiWithBearerToken(PropertyUtils.getKlogByOrderNumber(orderNumber), headers, queryParams, getKLogAccessToken());
    }

    public void verifyKLogResponse(String orderNumber, String storeId, String customerPlusCardId, String amountProcessed) {
        Response responseBody = getKLogXml(orderNumber);
        if (responseBody.getStatusCode() != 200) {
            Assert.assertEquals(responseBody.getStatusCode(), 200, Constants.Klog.KLOG_RESPONSE_CODE_FAILED_MSG);
            return;
        }
        String kLogResponse = responseBody.asString();
        Assert.assertFalse(kLogResponse.isEmpty(), Constants.Klog.KLOG_NOT_RECEIVED_MSG);
        String unitId = XmlPath.with(kLogResponse).get(KLOG_UNIT_ID);
        String orderId = XmlPath.with(kLogResponse).get(KLOG_ORDER_ID);
        String customerId = XmlPath.with(kLogResponse).get(KLOG_CUSTOMER_ID);
        String grossPositive = XmlPath.with(kLogResponse).get(KLOG_GROSS_POSITIVE);
        String orderAmount = amountProcessed.substring(1) + "00";
        Assert.assertEquals(unitId, storeId, Constants.Klog.STORE_ID_VALIDATION_MSG);
        Assert.assertEquals(orderId, orderNumber, Constants.Klog.ORDER_ID_VALIDATION_MSG);
        Assert.assertEquals(grossPositive, orderAmount, Constants.Klog.GROSS_VALUE_VALIDATION_MSG);
        if (!(customerPlusCardId.startsWith("2"))) {
            Assert.assertEquals(customerId, customerPlusCardId.substring(0, customerPlusCardId.length() - 1), Constants.Klog.CARD_VALIDATION_MSG);
        } else {
            Assert.assertEquals(customerId, customerPlusCardId, Constants.Klog.CARD_VALIDATION_MSG);
        }
    }
}
