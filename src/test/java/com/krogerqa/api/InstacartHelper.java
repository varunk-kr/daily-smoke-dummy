package com.krogerqa.api;

import com.krogerqa.api.model.instacart.*;
import com.krogerqa.api.model.instacart.Items;
import com.krogerqa.api.model.instacart.Product;
import com.krogerqa.api.model.instacart.Window;
import com.krogerqa.api.model.instacartPickCompletedEvent.*;
import com.krogerqa.api.model.instacartPickCompletedEvent.TimeStampPickCompleted;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.DateUtils;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class InstacartHelper {

    private static InstacartHelper instance;
    public String bearerToken;
    public final String CREATE_ORDER = "CREATE_ORDER";
    public final String FETCH_ORDER_DETAILS = "FETCH_ORDER_DETAILS";
    public final String RECEIVER_ENROUTE = "RECEIVER_ENROUTE";
    public final String RECEIVER_ARRIVED = "RECEIVER_ARRIVED";
    public final String ENROUTE = "ENROUTE";
    public final String ARRIVED = "ARRIVED";
    public static final String PICK_INSTACART_ORDER = "PICK_INSTACART_ORDER";
    String clientId = "order-ingestion-platform-4e4ddc3dc2a6e61b2118f45094dbd5c78499921113992496966";
    String clientKeyPassword = "LO7NdFJQCvrK3SeP3AzGD5KV7uaET3bLg5BKLq5J";
    String writeScope = "urn:com:kroger:kr:order:create-partner-orders:write";
    String readScope = "urn:com:kroger:kr:order:partner-orders:read";
    String pickScope = "urn:com:kroger:status:icmp:write";
    private String partnerOrderId;
    private String id;
    private String visualOrderId;

    private InstacartHelper() {
    }

    public synchronized static InstacartHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (InstacartHelper.class) {
            if (instance == null) {
                instance = new InstacartHelper();
            }
        }
        return instance;
    }

    public HashMap<String, String> getParamsForInstacartAccessToken(String scope) {
        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        switch (scope) {
            case CREATE_ORDER:
                formParams.put("scope", writeScope);
                break;
            case FETCH_ORDER_DETAILS:
                formParams.put("scope", readScope);
                break;
            case PICK_INSTACART_ORDER:
                formParams.put("scope", pickScope);
                break;
        }
        return formParams;
    }

    public void getInstacartAccessToken(String scope) {
        HashMap<String, String> formParams = getParamsForInstacartAccessToken(scope);
        formParams.put("client_id", clientId);
        formParams.put("client_secret", clientKeyPassword);
        if (scope.equals(PICK_INSTACART_ORDER)) {
            formParams.put("client_id", "instacartcallbackcert-030e0a023cf0d5ea55b693246211cbc25633934244753808680");
            formParams.put("client_secret", "oA9CQxC27OzQy6QK1gG2zDkWbxN8q_8tiVBKYHSF");
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Response res = new ApiUtils().postPayloadForFormUrlencodedWithHeaders(formParams, headers, PropertyUtils.getInstacartAuthenticationTokenEndpoint());
        bearerToken = res.jsonPath().getString("access_token");
    }

    public HashMap<String, String> createAndVerifyOrderDetails(HashMap<String, String> testOutputData) {
        testOutputData.put(ExcelUtils.IS_INSTACART_ORDER, String.valueOf(true));
        getInstacartAccessToken(CREATE_ORDER);
        ingestOrder(testOutputData);
        getInstacartAccessToken(FETCH_ORDER_DETAILS);
        getOrderIdAndPartnerId();
        return writeInstacartOrderDetails(testOutputData);
    }

    public HashMap<String, String> writeInstacartOrderDetails(HashMap<String, String> testOutputData) {
        testOutputData.put(ExcelUtils.INSTACART_ID, id);
        testOutputData.put(ExcelUtils.INSTACART_VISUAL_ORDER_ID, visualOrderId);
        testOutputData.put(ExcelUtils.INSTACART_PARTNER_ORDER_ID, partnerOrderId);
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        return testOutputData;
    }

    public String generateRandomVisualOrderId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public void ingestOrder(HashMap<String, String> testData) {
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(testData.get(ExcelUtils.SCENARIO));
        partnerOrderId = String.valueOf(generateRandomInstacartContainer());
        Customer customer = new Customer(testData.get(ExcelUtils.FIRST_NAME), testData.get(ExcelUtils.LAST_NAME), testData.get(ExcelUtils.PHONE_NUMBER), testData.get(ExcelUtils.USER_EMAIL), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR) + "-" + testData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH) + "-" + testData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY));
        TimeStamp timeStamp = new TimeStamp(DateUtils.getCurrentTimeInUTCTimeFormat(), "UTC");
        StoreInformation storeInformation = new StoreInformation(testData.get(ExcelUtils.STORE_ID));
        LocationCategory locationCategory = new LocationCategory("store", storeInformation);
        int hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(4));
        String startTime = "T" + String.format("%02d", hoursSpan) + ":00:00Z/";
        String endTime = "T" + String.format("%02d", hoursSpan + 1) + ":00:00Z";
        int daysFromToday = Integer.parseInt(testData.get(ExcelUtils.DAYS_FROM_TODAY));
        Window window = new Window(DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday) + startTime + DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday) + endTime, testData.get(Constants.CheckoutComposite.TIMEZONE));
        Handoff handoff = new Handoff("pickup", locationCategory, window);
        Product product = new Product(String.valueOf(false));
        ArrayList<Items> itemsList = new ArrayList<>();
        int i = 0;
        for (HashMap<String, String> item : itemData) {
            Items items = new Items("408366763229007-" + i, item.get(ExcelUtils.ITEM_UPC), Integer.parseInt(item.get(ExcelUtils.ITEM_QUANTITY)), item.get(Constants.CheckoutComposite.ITEM_DESCRIPTION), product);
            itemsList.add(items);
            i++;
        }
        CreateOrderEventData createOrderEventData = new CreateOrderEventData(testData.get(Constants.CheckoutComposite.SPECIAL_INSTRUCTION), customer, handoff, itemsList);
        InstacartOrderIngestionPayload instacartOrderIngestionPayload = new InstacartOrderIngestionPayload("8f14bd05-756b-415d-be35-3d4c79cbac7b", partnerOrderId, generateRandomVisualOrderId(), timeStamp, Constants.Instacart.CREATE_ORDER, createOrderEventData);
        Response response = new ApiUtils().postPayloadWithBearerToken(new ApiUtils().convertObjectToString(instacartOrderIngestionPayload), PropertyUtils.getInstacartOrderIngestionEndpoint(), getInstacartApiHeaders(), new HashMap<>(), bearerToken);
        JsonPath jsonPath = new JsonPath(response.asString());
        id = jsonPath.get("data.id");
    }

    public HashMap<String, String> getInstacartApiHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Correlation-ID", "stage-220");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + bearerToken);
        return headers;
    }

    public void getOrderIdAndPartnerId() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.partnerOrderId", partnerOrderId);
        Response response = new ApiUtils().getApiWithBearerToken(PropertyUtils.getInstacartOrderDetailsEndpoint(), getInstacartApiHeaders(), queryParams, bearerToken);
        JsonPath jsonPath = new JsonPath(response.asString());
        visualOrderId = jsonPath.get("data.orderId");
        partnerOrderId = jsonPath.get("data.partnerOrderId");
    }

    public long generateRandomInstacartContainer() {
        Random random = new Random();
        long firstPart = 1000 + random.nextInt(9000);
        long secondPart = (long) (random.nextDouble() * 1_000_000_000_0000L);
        return Long.parseLong(String.format("%04d%012d", firstPart, secondPart));
    }

    public HashMap<String, String> getHeadersForPickCompletedApi() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Kroger-Checkout-Version", "2");
        headers.put("X-Correlation-ID", "ICC_1234");
        return headers;
    }

    public HashMap<String, String> pickInstacartOrder(HashMap<String, String> testOutputData) {
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(testOutputData.get(ExcelUtils.SCENARIO));
        HashMap<String, String> instacartContainerMap = new HashMap<>();
        int i = 1;
        getInstacartAccessToken(PICK_INSTACART_ORDER);
        TimeStampPickCompleted timestamp = new TimeStampPickCompleted(DateUtils.getCurrentTimeInUTCTimeFormat(), "UTC");
        ArrayList<com.krogerqa.api.model.instacartPickCompletedEvent.Items> itemsList = new ArrayList<>();
        ArrayList<Containers> containerList = new ArrayList<>();
        for (HashMap<String, String> item : itemData) {
            ArrayList<Details> detailsList = new ArrayList<>();
            Details details = new Details(Integer.parseInt(item.get(ExcelUtils.ITEM_QUANTITY)), item.get(ExcelUtils.ITEM_UPC), Constants.Instacart.FULFILLED, Constants.PickCreation.FULFILLMENT_AS_ORDERED, null);
            detailsList.add(details);
            com.krogerqa.api.model.instacartPickCompletedEvent.Items items = new com.krogerqa.api.model.instacartPickCompletedEvent.Items(Constants.Instacart.ITEM_ID + i, detailsList);
            itemsList.add(items);
            i++;
        }
        for (int j = 1; j <= 3; j++) {
            Containers containers = null;
            Type type = new Type(Constants.Instacart.BAG);
            switch (j) {
                case 1:
                    String ambientContainer = generateRandomInstacartContainer() + "-1-" + j;
                    containers = new Containers(Constants.Instacart.CONTAINER_ID + j, HarvesterServicesHelper.AMBIENT_TEMPERATURE, ambientContainer, type);
                    instacartContainerMap.put(ambientContainer, HarvesterServicesHelper.AMBIENT_TEMPERATURE);
                    break;
                case 2:
                    String refrigeratedContainer = generateRandomInstacartContainer() + "-1-" + j;
                    containers = new Containers(Constants.Instacart.CONTAINER_ID + j, Constants.Instacart.REFRIGERATED_TEMPERATURE, refrigeratedContainer, type);
                    instacartContainerMap.put(refrigeratedContainer, HarvesterServicesHelper.REFRIGERATED_TEMPERATURE);
                    break;
                case 3:
                    String frozenContainer = generateRandomInstacartContainer() + "-1-" + j;
                    containers = new Containers(Constants.Instacart.CONTAINER_ID + j, HarvesterServicesHelper.FROZEN_TEMPERATURE, frozenContainer, type);
                    instacartContainerMap.put(frozenContainer, HarvesterServicesHelper.FROZEN_TEMPERATURE);
                    break;
            }
            containerList.add(containers);
        }
        PickingCompletedEventData pickingCompletedEventData = new PickingCompletedEventData(testOutputData.get(ExcelUtils.STORE_ID), itemsList, containerList);
        InstacartPickCompletedEventPayload instacartPickCompletedEventPayload = new InstacartPickCompletedEventPayload(testOutputData.get(ExcelUtils.INSTACART_ID), testOutputData.get(ExcelUtils.INSTACART_PARTNER_ORDER_ID), testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID), timestamp, Constants.Instacart.PICKING_COMPLETED, pickingCompletedEventData);
        HashMap<String, String> headers = getHeadersForPickCompletedApi();
        Response response = new ApiUtils().postPayloadWithBearerToken(new ApiUtils().convertObjectToString(instacartPickCompletedEventPayload), PropertyUtils.getInstacartPickCompletedEndpoint(), headers, new HashMap<>(), bearerToken);
        Assert.assertEquals(response.getStatusCode(), 200);
        testOutputData.put(ExcelUtils.INSTACART_CONTAINER_MAP, String.valueOf(instacartContainerMap));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        return testOutputData;
    }

    public void triggerOnMyWayForInstacartOrder(HashMap<String, String> testOutputData) {
        getInstacartAccessToken(PICK_INSTACART_ORDER);
        HashMap<String, String> headers = getHeadersForPickCompletedApi();
        TimeStampPickCompleted timestamp = new TimeStampPickCompleted(DateUtils.getCurrentTimeInUTCTimeFormat(), Constants.Instacart.UTC);
        VehicleInfo vehicleInfo = new VehicleInfo(Constants.Instacart.MAKE, Constants.Instacart.MODEL, Constants.Instacart.VEHICLE_TYPE, Constants.Instacart.VEHICLE_COLOR);
        HandoffReadinessEventData handoffReadinessEventData = new HandoffReadinessEventData(ENROUTE, vehicleInfo);
        HandoffReadinessEventArrivedData handoffReadinessEventArrivedDataData = new HandoffReadinessEventArrivedData(ARRIVED, testOutputData.get(ExcelUtils.SPOT), vehicleInfo);
        InstacartCustomerEnRoute instacartCustomerEnRoute = new InstacartCustomerEnRoute(testOutputData.get(ExcelUtils.INSTACART_ID), testOutputData.get(ExcelUtils.INSTACART_PARTNER_ORDER_ID), testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID), timestamp, RECEIVER_ENROUTE, handoffReadinessEventData);
        InstacartCustomerArrived instacartCustomerArrived = new InstacartCustomerArrived(testOutputData.get(ExcelUtils.INSTACART_ID), testOutputData.get(ExcelUtils.INSTACART_PARTNER_ORDER_ID), testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID), timestamp, RECEIVER_ARRIVED, handoffReadinessEventArrivedDataData);
        new ApiUtils().postPayloadWithBearerToken(new ApiUtils().convertObjectToString(instacartCustomerEnRoute), PropertyUtils.getInstacartPickCompletedEndpoint(), headers, new HashMap<>(), bearerToken);
        new ApiUtils().postPayloadWithBearerToken(new ApiUtils().convertObjectToString(instacartCustomerArrived), PropertyUtils.getInstacartPickCompletedEndpoint(), headers, new HashMap<>(), bearerToken);
    }
}
