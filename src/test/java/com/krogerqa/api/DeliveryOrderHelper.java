package com.krogerqa.api;

import com.krogerqa.api.model.shipt.PIFShiptUpdate;
import com.krogerqa.api.model.shipt.PIFShiptUpdateData;
import com.krogerqa.api.model.shipt.PIFShiptUpdateDataDropOff;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for creating payloads for SHIPT scenario, Handle API calls for SHIPT status updates
 */
public class DeliveryOrderHelper {

    private static final String ORDER_RETRIEVE_MSG = "Courier Service call successful for Retrieving";
    private static DeliveryOrderHelper instance;

    private DeliveryOrderHelper() {
    }

    public synchronized static DeliveryOrderHelper getInstance() {

        if (instance != null) {
            return instance;
        }
        synchronized (DeliveryOrderHelper.class) {
            if (instance == null) {
                instance = new DeliveryOrderHelper();
            }
        }
        return instance;
    }

    private static Response getDeliveryOrderDetailsFromCourierService(String orderNumber, HashMap<String, String> testOutputData) {
        String PifEndPointForShipt = PropertyUtils.getCourierDeliveriesEndpoint();//old end point
        String userName = PropertyUtils.getCourierCoreToPifUsername();
        String password = PropertyUtils.getCourierCoreToPifPassword();
        Map<String, String> headerMap = new HashMap<>();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            PifEndPointForShipt = PropertyUtils.getCourierDeliveriesAzureEndpoint();//new end point
            userName = PropertyUtils.getShiptPifAzureUsername();
            password = PropertyUtils.getShiptPifAzurePassword();
            headerMap.put("X-Correlation-ID", "23432432t5");
        }
        ApiUtils apiUtils = new ApiUtils();
        Map<String, String> map = new HashMap<>();
        map.put("filter.orderId", orderNumber);
        Response response = apiUtils.getApi(userName, password, null, PifEndPointForShipt, headerMap, map);
        Assert.assertEquals(response.statusCode(), 200, ORDER_RETRIEVE_MSG + "delivery order details");
        return response;
    }

    private static Map<String, Object> returnDeliveryOrderDetails(Response courierServiceResponse, String status, HashMap<String, String> testOutputData) {
        JsonPath jsonPath = courierServiceResponse.jsonPath();
        Map<String, Object> orderDetails = new HashMap<>();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            orderDetails.put(Constants.PARTNER_SHIPT_ID, jsonPath.getString(Constants.PARTNER_SHIPT_ID));
            orderDetails.put(Constants.DELIVERY_SHIPT_ORDER_ID, jsonPath.getString(Constants.DELIVERY_SHIPT_ORDER_ID));
            orderDetails.put(Constants.DELIVERY_SHIPT_PARTNER_TRACKING_ID, jsonPath.getString(Constants.DELIVERY_SHIPT_PARTNER_TRACKING_ID));
            orderDetails.put(Constants.DELIVERY_SHIPT_PARTNER_TRACKING_STATUS, status);
            orderDetails.put(Constants.DELIVERY_SHIPT_WINDOW_STARTS_AT, jsonPath.getString(Constants.DELIVERY_SHIPT_WINDOW_STARTS_AT));
            orderDetails.put(Constants.DELIVERY_SHIPT_WINDOW_ENDS_AT, jsonPath.getString(Constants.DELIVERY_SHIPT_WINDOW_ENDS_AT));
            Instant instant = Instant.now();
            orderDetails.put(Constants.Shipt.DELIVERY_SHIPT_CREATED_AT, instant.toString());
            orderDetails.put(Constants.Shipt.DELIVERY_SHIPT_UPDATED_AT, instant.toString());
        } else {
            orderDetails.put(Constants.PARTNER_ID, jsonPath.getString(Constants.PARTNER_ID));
            orderDetails.put(Constants.DELIVERY_ORDER_ID, jsonPath.getString(Constants.DELIVERY_ORDER_ID));
            orderDetails.put(Constants.DELIVERY_PARTNER_TRACKING_ID, jsonPath.getString(Constants.DELIVERY_PARTNER_TRACKING_ID));
            orderDetails.put(Constants.DELIVERY_PARTNER_TRACKING_STATUS, jsonPath.getString(Constants.DELIVERY_PARTNER_TRACKING_STATUS));
            orderDetails.put(Constants.DELIVERY_WINDOW_STARTS_AT, jsonPath.getString(Constants.DELIVERY_WINDOW_STARTS_AT));
            orderDetails.put(Constants.DELIVERY_WINDOW_ENDS_AT, jsonPath.getString(Constants.DELIVERY_WINDOW_ENDS_AT));
            orderDetails.put(Constants.Shipt.DELIVERY_CREATED_AT, jsonPath.getString(Constants.Shipt.DELIVERY_CREATED_AT));
            orderDetails.put(Constants.Shipt.DELIVERY_UPDATED_AT, jsonPath.getString(Constants.Shipt.DELIVERY_UPDATED_AT));
        }
        return orderDetails;
    }

    private String returnShiptPifPayload(Response shiptResponse, String status, HashMap<String, String> testOutputData) {
        ApiUtils apiUtils = new ApiUtils();
        Map<String, Object> shiptDetails = returnDeliveryOrderDetails(shiptResponse, status, testOutputData);
        PIFShiptUpdateDataDropOff pifShiptUpdateDataDropOff = new PIFShiptUpdateDataDropOff(shiptDetails.get(Constants.DELIVERY_SHIPT_WINDOW_STARTS_AT).toString().replace("[", "").replace("]", ""), shiptDetails.get(Constants.DELIVERY_SHIPT_WINDOW_ENDS_AT).toString().replace("[", "").replace("]", ""));
        PIFShiptUpdateData pifShiptUpdateData = new PIFShiptUpdateData(shiptDetails.get(Constants.DELIVERY_SHIPT_PARTNER_TRACKING_ID).toString().replace("[", "").replace("]", ""), shiptDetails.get(Constants.DELIVERY_SHIPT_ORDER_ID).toString().replace("[", "").replace("]", ""), status, pifShiptUpdateDataDropOff, Constants.Shipt.VEHICLE_TYPE, shiptDetails.get(Constants.Shipt.DELIVERY_SHIPT_CREATED_AT).toString().replace("[", "").replace("]", ""), shiptDetails.get(Constants.Shipt.DELIVERY_SHIPT_UPDATED_AT).toString().replace("[", "").replace("]", ""));
        PIFShiptUpdate pifShiptUpdate = new PIFShiptUpdate(Constants.Shipt.DELIVERY_STATUS_CHANGED, pifShiptUpdateData);
        return apiUtils.convertObjectToString(pifShiptUpdate);
    }

    public void updateDeliveryOrderStatus(String orderNumber, String status, HashMap<String, String> testOutputData) {
        String endPointForShipt = PropertyUtils.getCourierServicePostEndpoint();//old end point
        String userName = PropertyUtils.getCourierCoreToPifUsername();
        String password = PropertyUtils.getCourierCoreToPifPassword();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            endPointForShipt = PropertyUtils.getCourierServicePostPIFEndpoint();//new end point
            userName = PropertyUtils.getShiptPifAzureUsername();
            password = PropertyUtils.getShiptPifAzurePassword();
        }
        ApiUtils apiUtils = new ApiUtils();
        String payload;
        Response responseFromDeliveryCourierService = getDeliveryOrderDetailsFromCourierService(orderNumber, testOutputData);
        Map<String, Object> orderDetails;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            payload = returnShiptPifPayload(responseFromDeliveryCourierService, status, testOutputData);
        } else {
            payload = returnShiptPayload(responseFromDeliveryCourierService, status, testOutputData);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("x-partner-id", "SHIPT");
        Response response = apiUtils.postPayload(userName, password, payload, endPointForShipt, headers, new HashMap<>());
        Assert.assertEquals(response.statusCode(), 201, "Status updated to " + status);
        responseFromDeliveryCourierService = getDeliveryOrderDetailsFromCourierService(orderNumber, testOutputData);
        orderDetails = returnDeliveryOrderDetails(responseFromDeliveryCourierService, status, testOutputData);
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            Assert.assertEquals(orderDetails.get(Constants.DELIVERY_SHIPT_PARTNER_TRACKING_STATUS).toString().replace("[", "").replace("]", ""), status, "Validating the updated status for shipt");
        } else {
            Assert.assertEquals(orderDetails.get(Constants.DELIVERY_PARTNER_TRACKING_STATUS), status, "Validating the updated status");
        }
    }

    private String returnShiptPayload(Response shiptResponse, String status, HashMap<String, String> testOutputData) {
        ApiUtils apiUtils = new ApiUtils();
        Map<String, Object> shiptDetails = returnDeliveryOrderDetails(shiptResponse, status, testOutputData);
        PIFShiptUpdateDataDropOff pifShiptUpdateDataDropOff = new PIFShiptUpdateDataDropOff(shiptDetails.get(Constants.DELIVERY_WINDOW_STARTS_AT).toString(), shiptDetails.get(Constants.DELIVERY_WINDOW_ENDS_AT).toString());
        PIFShiptUpdateData pifShiptUpdateData = new PIFShiptUpdateData(shiptDetails.get(Constants.DELIVERY_PARTNER_TRACKING_ID).toString(), shiptDetails.get(Constants.DELIVERY_ORDER_ID).toString(), status, pifShiptUpdateDataDropOff, Constants.Shipt.VEHICLE_TYPE, shiptDetails.get(Constants.Shipt.DELIVERY_CREATED_AT).toString(), shiptDetails.get(Constants.Shipt.DELIVERY_UPDATED_AT).toString());
        PIFShiptUpdate pifShiptUpdate = new PIFShiptUpdate(Constants.Shipt.DELIVERY_STATUS_CHANGED, pifShiptUpdateData);
        return apiUtils.convertObjectToString(pifShiptUpdate);
    }
}
