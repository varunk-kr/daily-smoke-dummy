package com.krogerqa.api;

import com.krogerqa.api.model.customerCheckIn.CustomerCheckInPayload;
import com.krogerqa.api.model.itempickevent.ItemPick;
import com.krogerqa.api.model.pickitem.Data;
import com.krogerqa.api.model.pickitem.PickItem;
import com.krogerqa.api.model.trolleyrunevent.AdditionalAttributes;
import com.krogerqa.api.model.trolleyrunevent.TrolleyRun;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.*;
import com.krogerqa.web.ui.maps.cue.CueHomeMap;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for creating payloads for PICKING services and Handling API calls for various PICKING services
 */
public class PickingServicesHelper {
    static String END_TROLLEY_RUN_UNSUCCESSFUL = "END_TROLLEY_RUN is not successful";
    static String START_TROLLEY_RUN_UNSUCCESSFUL = "START_TROLLEY_RUN is not successful";
    static String PICK_ITEM_NOT_SUCCESS = "Pick Item is not successful";
    static String TROLLEY_NAME = "trolleyName";
    static String containerPath = "data.pick-lists.item-containers";
    static String BATCH_RESPONSE_NOT_SUCCESS = "batching response is not successful";
    static String SUBS_RESPONSE_FAILED = "Substitute customer response failed";
    static String SUBS_COMMUNICATION_FAILED = "Triggering substitute communication failed";
    static String ITEM_PICK_NOT_SUCCESS = "Publish ITEM_PICK event is not successful";
    static String PICK_FETCH_NOT_SUCCESS = "Pick-List fulfilled-item fetch is not successful";
    static String ADDITIONAL_ATTRIBUTES = "additionalAttributes";
    static String PICK_LIST_FULFILL_ITEMS = "data.pick-lists.fulfilledItems[";
    static String PICK_LIST_ITEMS = "data.pick-lists.item-containers[";
    static String SUBS_SUCCESS_MSG = "Successfully validated details for all substituted items";
    static String TROLLEY_NOT_GENERATED_MESSAGE = "No trolley generated for the last request trolley click.";
    private static PickingServicesHelper instance;
    ApiUtils apiUtils = new ApiUtils();
    CueHomeMap cueHomeMap = CueHomeMap.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();
    WebUtils webUtils = WebUtils.getInstance();

    private PickingServicesHelper() {
    }

    public synchronized static PickingServicesHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (PickingServicesHelper.class) {
            if (instance == null) {
                instance = new PickingServicesHelper();
            }
        }
        return instance;
    }

    private Response getFulfilledItemDetails(String orderId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.id", "pick_creation_dsw_" + orderId);
        queryParams.put("projection", "fulfilled-items");
        Response response = new ApiUtils().getApi(null, null, null, PropertyUtils.getPickListsDataEndpoint(), new HashMap<>(), queryParams);
        Assert.assertEquals(response.getStatusCode(), 200, PICK_FETCH_NOT_SUCCESS);
        return response;
    }

    private String generateTrolleyEventPayload(String eventType, String trolleyId, String location, int daysFromToday, String pickDate) {
        TrolleyRun trolleyRun = new TrolleyRun(PropertyUtils.getCueUsername(), eventType, DateUtils.getRequiredDateMilliseconds(daysFromToday), trolleyId, location, new AdditionalAttributes(location, pickDate));
        return new ApiUtils().convertObjectToString(trolleyRun);
    }

    private void endTrolleyRun(String storeId, String pickDate, String trolleyId, int daysFromToday) {
        Response response = new ApiUtils().postPayload(null, null, generateTrolleyEventPayload(Constants.PickCreation.END_TROLLEY_EVENT, trolleyId, storeId, daysFromToday, pickDate), PropertyUtils.getPickingEventsEndpoint(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, END_TROLLEY_RUN_UNSUCCESSFUL);
    }

    private void startTrolleyRun(String storeId, String pickDate, String trolleyId, int daysFromToday) {
        Response response = new ApiUtils().postPayload(null, null, generateTrolleyEventPayload(Constants.PickCreation.START_TROLLEY_EVENT, trolleyId, storeId, daysFromToday, pickDate), PropertyUtils.getPickingEventsEndpoint(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, START_TROLLEY_RUN_UNSUCCESSFUL);
    }

    private void pickItem(String orderNumber, String storeId, String pickDate, String fulfillmentType, String itemId, String itemUpc, String pickedAmount, String orderedQuantity, String containerNumber, String temperature, String containerType, String trolleyId) {
        Response response = new ApiUtils().postPayload(null, null, generatePickItemPayload(orderNumber, fulfillmentType, itemId, itemUpc, Double.parseDouble(pickedAmount), orderedQuantity, containerNumber, temperature.substring(0, 2), containerType, trolleyId, storeId, pickDate), PropertyUtils.getFulfillItemEndpoint(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 201, PICK_ITEM_NOT_SUCCESS);
    }

    /**
     * API to perform batching of orders from required date
     *
     * @param storeId       store location id with division
     * @param daysFromToday integer to indicate number of days from current date for determining order batching date. Example, -1 for previous day, 0 for current day, 1 for next day.
     */
    public void pickRunBatchPostApi(String storeId, int daysFromToday) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.locationId", storeId);
        queryParams.put("filter.executionDateTime", DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday) + Constants.PickCreation.ORDER_BATCHING_TIME);
        Response response = new ApiUtils().postPayload(null, null, "", PropertyUtils.getOrderBatchingEndpoint(), new HashMap<>(), queryParams);
        Assert.assertEquals(response.getStatusCode(), 200, BATCH_RESPONSE_NOT_SUCCESS);
    }

    public String queryItemContainers(String orderId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.id", "pick_creation_dsw_" + orderId);
        queryParams.put("projection", "item-containers");
        Response response = new ApiUtils().getApi(null, null, null, PropertyUtils.getPickListsDataEndpoint(), new HashMap<>(), queryParams);
        Assert.assertEquals(response.getStatusCode(), 200, "Pick-List item-container fetch is not successful");
        return response.asString();
    }

    public List<HashMap<String, String>> getContainerDetails(String orderId, List<HashMap<String, String>> itemsList) {
        List<HashMap<String, String>> pickRunList = new ArrayList<>();
        JsonPath jsonPath = new JsonPath(queryItemContainers(orderId));
        for (Object containerDetails : jsonPath.getList("data.pick-lists.item-containers")) {
            HashMap<String, String> pickRunMap = new HashMap<>();
            JsonPath jsonPathChild = new JsonPath(new ApiUtils().convertObjectToString(containerDetails));
            for (HashMap<String, String> itemDetails : itemsList) {
                if (!(itemDetails.containsValue(Constants.PickCreation.CARRYOVER))) {
                    Map<String, String> apiItemDetails = jsonPathChild.getMap(ADDITIONAL_ATTRIBUTES);
                    if (apiItemDetails.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC).equals(itemDetails.get(ExcelUtils.ITEM_UPC))) {
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_ITEM_UPC, apiItemDetails.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER, apiItemDetails.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_TROLLEY_ID, apiItemDetails.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE, apiItemDetails.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_CONTAINER_TYPE, jsonPathChild.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TYPE));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_ITEM_ID, jsonPathChild.get(Constants.PickCreation.PICK_LISTS_ITEM_ID));
                        pickRunMap.put(Constants.PickCreation.PICK_LISTS_CONTAINER_UUID, jsonPathChild.get(Constants.PickCreation.PICK_LISTS_CONTAINER_UUID));
                        pickRunMap.put(Constants.PickCreation.ITEM_ORDERED_QUANTITY, itemDetails.get(ExcelUtils.ITEM_QUANTITY));
                        pickRunMap.put(Constants.PickCreation.NEED_BY_TIME, apiItemDetails.get(Constants.PickCreation.NEED_BY_TIME));
                        if (itemDetails.containsValue(Constants.PickCreation.OUT_OF_STOCK)) {
                            pickRunMap.put(Constants.PickCreation.ITEM_FULFILLMENT_TYPE, Constants.PickCreation.FULFILLMENT_OUT_OF_STOCK);
                            pickRunMap.put(Constants.PickCreation.ITEM_STATUS, Constants.PickCreation.OUT_OF_STOCK);
                            pickRunMap.put(Constants.PickCreation.ITEM_PICKED_AMOUNT, "0");
                        } else {
                            pickRunMap.put(Constants.PickCreation.ITEM_FULFILLMENT_TYPE, Constants.PickCreation.FULFILLMENT_AS_ORDERED);
                            pickRunMap.put(Constants.PickCreation.ITEM_STATUS, Constants.PickCreation.PICKED_STATUS);
                            pickRunMap.put(Constants.PickCreation.ITEM_PICKED_AMOUNT, itemDetails.get(ExcelUtils.ITEM_QUANTITY));
                        }
                        if (itemDetails.containsKey(Constants.PickCreation.SELL_BY_WEIGHT)) {
                            pickRunMap.put(Constants.PickCreation.SELL_BY_WEIGHT, "true");
                        } else {
                            pickRunMap.put(Constants.PickCreation.SELL_BY_WEIGHT, "false");
                        }
                        pickRunList.add(pickRunMap);
                    }
                }
            }
        }
        return pickRunList;
    }

    private String generatePickItemPayload(String orderNumber, String fulfillmentType, String itemId, String itemUpc, Double pickedAmount, String orderedQuantity, String containerNumber, String temperature, String containerType, String trolleyId, String location, String pickDate) {
        PickItem pickItem = new PickItem();
        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data(fulfillmentType, itemId, itemUpc, pickedAmount, new com.krogerqa.api.model.pickitem.AdditionalAttributes(containerNumber, String.valueOf(DateUtils.getRequiredDateMilliseconds(Constants.Date.TODAY)), location, orderNumber, orderedQuantity, itemUpc, pickDate, PropertyUtils.getCueUsername(), PropertyUtils.getCueUsername(), temperature, itemId, trolleyId, containerType)));
        pickItem.setData(dataList);
        return new ApiUtils().convertObjectToString(pickItem);
    }

    private String generateItemPickEventPayload(String fulfillmentType, String itemId, String itemUpc, String isRandomWeight, String pickedAmount, String orderedQuantity, String containerUuid, String containerNumber, String temperature, String itemStatus, String trolleyId, String location, String pickDate, String orderNumber) {
        long notedTime = DateUtils.getRequiredDateMilliseconds(Constants.Date.TODAY);
        ItemPick itemPick = new ItemPick(new com.krogerqa.api.model.itempickevent.AdditionalAttributes(containerUuid, containerNumber, trolleyId, pickDate, fulfillmentType, itemUpc, orderedQuantity, "POSTMAN", pickedAmount, String.valueOf(notedTime), location, isRandomWeight, itemStatus, itemUpc, String.valueOf(notedTime), temperature, itemId, itemId), PropertyUtils.getCueUsername(), Constants.PickCreation.ITEM_PICK_EVENT, notedTime, orderNumber, notedTime, location);
        return new ApiUtils().convertObjectToString(itemPick);
    }

    private void publishItemPickEvent(String fulfillmentType, String itemId, String itemUpc, String isRandomWeight, String pickedAmount, String orderedQuantity, String containerUuid, String containerNumber, String temperature, String itemStatus, String trolleyId, String storeId, String pickDate, String orderNumber) {
        Response response = new ApiUtils().postPayload(null, null, generateItemPickEventPayload(fulfillmentType, itemId, itemUpc, isRandomWeight, pickedAmount, orderedQuantity, containerUuid, containerNumber, temperature, itemStatus, trolleyId, storeId, pickDate, orderNumber), PropertyUtils.getPickingEventsEndpoint(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, ITEM_PICK_NOT_SUCCESS);
    }

    public void pickTrolleysWithApi(String orderId, String storeId, int daysFromToday, List<HashMap<String, String>> itemsList) {
        String pickDate = DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday);
        for (HashMap<String, String> pickRun : getContainerDetails(orderId, itemsList)) {
            startTrolleyRun(storeId, pickDate, pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID), daysFromToday);
            pickItem(orderId, storeId, pickDate, pickRun.get(Constants.PickCreation.ITEM_FULFILLMENT_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_ID), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC), pickRun.get(Constants.PickCreation.ITEM_PICKED_AMOUNT), pickRun.get(Constants.PickCreation.ITEM_ORDERED_QUANTITY), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID));
            publishItemPickEvent(pickRun.get(Constants.PickCreation.ITEM_FULFILLMENT_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_ID), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC), pickRun.get(Constants.PickCreation.SELL_BY_WEIGHT), pickRun.get(Constants.PickCreation.ITEM_PICKED_AMOUNT), pickRun.get(Constants.PickCreation.ITEM_ORDERED_QUANTITY), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_UUID), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE), pickRun.get(Constants.PickCreation.ITEM_STATUS), pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID), storeId, pickDate, orderId);
            endTrolleyRun(storeId, pickDate, pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID), daysFromToday);
        }
        triggerCarryoverLoadTrolley();
    }

    public void triggerCarryoverLoadTrolley() {
        new ApiUtils().getApi(null, null, null, PropertyUtils.getCarryoverJobRunnerEndpoint(), new HashMap<>(), new HashMap<>());
    }

    public HashMap<String, String> getContainersNotPicked(String orderId, HashMap<String, String> containerMap) {
        JsonPath jsonPath = new JsonPath(getFulfilledItemDetails(orderId).asString());
        HashMap<String, String> notPickedContainerMap = SerializationUtils.clone(containerMap);
        for (int i = 0; i < jsonPath.getInt("data.pick-lists.fulfilledItems.size()"); i++) {
            notPickedContainerMap.remove(jsonPath.getMap(PICK_LIST_FULFILL_ITEMS + i + "].additionalAttributes").get("containerNo"));
        }
        return notPickedContainerMap;
    }

    public void triggerSubCommunication(String orderId) {
        int statusCode = apiUtils.postPayload(null, null, "", PropertyUtils.getTriggerSubstituteCommunicationEndpoint(orderId), new HashMap<>(), new HashMap<>()).getStatusCode();
        Assert.assertEquals(statusCode, 200, SUBS_COMMUNICATION_FAILED);
    }

    private String generatePayloadForCustomerResponse(List<HashMap<String, String>> itemsList) {
        JSONObject payload = new JSONObject();
        try {
            JSONArray choicesArray = new JSONArray();
            for (HashMap<String, String> itemDetails : itemsList) {
                JSONObject item = new JSONObject();
                if (itemDetails.get(ExcelUtils.FULFILLMENT_TYPE) != null && itemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_SHORT) || itemDetails.get(ExcelUtils.FULFILLMENT_TYPE) != null && itemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_ACCEPT)) {
                    item.put("orderedUpc", itemDetails.get(ExcelUtils.ITEM_UPC));
                    item.put("isAccepted", true);
                    choicesArray.add(item);
                } else if (itemDetails.get(ExcelUtils.FULFILLMENT_TYPE) != null && itemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_REJECT)) {
                    item.put("orderedUpc", itemDetails.get(ExcelUtils.ITEM_UPC));
                    item.put("isAccepted", false);
                    choicesArray.add(item);
                }
            }
            payload.put("choices", choicesArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(payload);
    }

    public void sendSubstitutionCustomerResponse(String orderId, List<HashMap<String, String>> itemsList) {
        int statusCode = apiUtils.postPayload(PropertyUtils.getSubstituteCustomerResponseApiUsername(), PropertyUtils.getSubstituteCustomerResponseApiPassword(), generatePayloadForCustomerResponse(itemsList), PropertyUtils.getSubstituteCustomerResponseEndpoint(orderId), new HashMap<>(), new HashMap<>()).getStatusCode();
        Assert.assertEquals(statusCode, 200, SUBS_RESPONSE_FAILED);
    }

    private String verifySubstitutedItemResponse(String orderId, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        StringBuilder rejectedItemContainer = new StringBuilder();
        int actualSubstitutedCount = 0;
        JsonPath jsonPath = new JsonPath(getFulfilledItemDetails(orderId).asString());
        itemsList.removeIf(itemDetails -> (!itemDetails.get(ExcelUtils.FULFILLMENT_TYPE).contains(Constants.PickCreation.SUBSTITUTE)));
        for (int i = 0; i < jsonPath.getInt("data.pick-lists.fulfilledItems.size()"); i++) {
            baseCommands.getWebDriverWait();
            for (HashMap<String, String> substituteItemDetails : itemsList) {
                baseCommands.getWebDriverWait();
                Map<String, String> apiItemDetails = jsonPath.getMap(PICK_LIST_FULFILL_ITEMS + i + "]." + ADDITIONAL_ATTRIBUTES);
                if (apiItemDetails.get("orderedUpc").equals(substituteItemDetails.get(ExcelUtils.ITEM_UPC))) {
                    Assert.assertEquals(jsonPath.get(PICK_LIST_FULFILL_ITEMS + i + "]." + Constants.PickCreation.ITEM_FULFILLMENT_TYPE), "substitute", "Incorrect fulfillment type");
                    Assert.assertEquals(jsonPath.get(PICK_LIST_FULFILL_ITEMS + i + "]." + Constants.PickCreation.PICK_LISTS_ITEM_ID), substituteItemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC), "Incorrect substitute upc");
                    if (!substituteItemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_SHORT)) {
                        if (substituteItemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_REJECT)) {
                            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER))) {
                                rejectedItemContainer.append(apiItemDetails.get("containerNo")).append(",");
                            } else {
                                rejectedItemContainer = new StringBuilder(apiItemDetails.get("containerNo"));
                            }
                        }
                    }
                    actualSubstitutedCount++;
                    break;
                }
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER))) {
            rejectedItemContainer = new StringBuilder(rejectedItemContainer.substring(0, rejectedItemContainer.length() - 1));
        }
        Assert.assertEquals(actualSubstitutedCount, itemsList.size(), SUBS_SUCCESS_MSG);
        return rejectedItemContainer.toString();
    }

    public String validateTwoWayCommunication(String orderId, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        try {
            triggerSubCommunication(orderId);
            sendSubstitutionCustomerResponse(orderId, itemsList);
            return verifySubstitutedItemResponse(orderId, itemsList, testOutputData);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> pickAnItemFromTrolley(HashMap<String, String> testOutputData, int daysFromToday, List<HashMap<String, String>> itemsList) {
        String pickDate = DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday);
        String orderId = testOutputData.get(ExcelUtils.ORDER_NUMBER);
        String storeId = testOutputData.get(ExcelUtils.STORE_ID);
        String takeOverTrolley = "";
        String takeOverContainerNumber = "";
        for (HashMap<String, String> pickRun : getContainerDetails(orderId, itemsList)) {
            String temp = pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER)) && (temp.equals(Constants.PickCreation.REFRIGERATED) || temp.equals(Constants.PickCreation.FROZEN_TEMP))) {
                continue;
            }
            takeOverTrolley = pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID);
            takeOverContainerNumber = pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER);
            startTrolleyRun(storeId, pickDate, takeOverTrolley, daysFromToday);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.CARRY_OVER_TAKE_OVER))) {
                pickItem(orderId, storeId, pickDate, pickRun.get(Constants.PickCreation.ITEM_FULFILLMENT_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_ID), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC), pickRun.get(Constants.PickCreation.ITEM_PICKED_AMOUNT), pickRun.get(Constants.PickCreation.ITEM_ORDERED_QUANTITY), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID));
                publishItemPickEvent(pickRun.get(Constants.PickCreation.ITEM_FULFILLMENT_TYPE), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_ID), pickRun.get(Constants.PickCreation.PICK_LISTS_ITEM_UPC), pickRun.get(Constants.PickCreation.SELL_BY_WEIGHT), pickRun.get(Constants.PickCreation.ITEM_PICKED_AMOUNT), pickRun.get(Constants.PickCreation.ITEM_ORDERED_QUANTITY), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_UUID), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE), pickRun.get(Constants.PickCreation.ITEM_STATUS), pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID), storeId, pickDate, orderId);
            }
            break;
        }
        testOutputData.put(ExcelUtils.TAKE_OVER_TROLLEY, takeOverTrolley);
        testOutputData.put(ExcelUtils.TAKE_OVER_CONTAINER_NO, takeOverContainerNumber);
        return testOutputData;
    }

    public void requestTrolley() {
        baseCommands.waitForElementVisibility(cueHomeMap.clickRequestTrolley());
        baseCommands.waitForElementClickability(cueHomeMap.clickRequestTrolley());
        baseCommands.click(cueHomeMap.clickRequestTrolley());
        baseCommands.waitForElementVisibility(cueHomeMap.trolleyConfirmButton());
        baseCommands.click(cueHomeMap.trolleyConfirmButton());
        baseCommands.waitForElementInvisible(cueHomeMap.trolleyRequestNotification());
    }

    public Boolean getAllContainersBatched(String orderNumber) {
        boolean batched = true;
        JsonPath jsonPath = new JsonPath(queryItemContainers(orderNumber));
        List<Object> listOfTrolleys = jsonPath.getList(containerPath);
        for (int j = 0; j < listOfTrolleys.size(); j++) {
            HashMap<String, String> containerMap = new HashMap<>(jsonPath.getMap(PICK_LIST_ITEMS + j + "]." + ADDITIONAL_ATTRIBUTES));
            batched = !containerMap.containsKey(TROLLEY_NAME);
            if (batched) {
                break;
            }
            baseCommands.wait(1);
        }
        return batched;
    }

    public void verifyAllContainerGeneratedForOrder(String orderNumber) {
        Boolean batched = getAllContainersBatched(orderNumber);
        while (batched) {
            baseCommands.webpageRefresh();
            int numberOfTrolleysBefore = cueHomePage.getNumberOfTrolleys();
            requestTrolley();
            baseCommands.webpageRefresh();
            int numberOfTrolleysAfter = cueHomePage.getNumberOfTrolleys();
            int i = 0;
            while (numberOfTrolleysBefore == numberOfTrolleysAfter) {
                numberOfTrolleysAfter = getNumberOfTrolleysAfterClickingRequestTrolley(orderNumber);
                i++;
                if (i == 30) {
                    Assert.fail(TROLLEY_NOT_GENERATED_MESSAGE);
                    break;
                }
            }
            batched = getAllContainersBatched(orderNumber);
        }
    }

    private int getNumberOfTrolleysAfterClickingRequestTrolley(String orderNumber) {
        int numberOfTrolleysAfter;
        baseCommands.webpageRefresh();
        numberOfTrolleysAfter = cueHomePage.getNumberOfTrolleys();
        baseCommands.webpageRefresh();
        cueHomePage.searchOrderId(orderNumber);
        cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
        baseCommands.waitForElementVisibility(cueHomeMap.orderDetailsHeader());
        baseCommands.browserBack();
        return numberOfTrolleysAfter;
    }

    public void partialBatching(String orderNumber) {
        int i = 0;
        int numberOfTrolleysBefore = cueHomePage.getNumberOfTrolleys();
        requestTrolley();
        baseCommands.webpageRefresh();
        int numberOfTrolleysAfter = cueHomePage.getNumberOfTrolleys();
        while (numberOfTrolleysBefore == numberOfTrolleysAfter) {
            numberOfTrolleysAfter = getNumberOfTrolleysAfterClickingRequestTrolley(orderNumber);
            i++;
            if (i == 20) {
                Assert.fail(TROLLEY_NOT_GENERATED_MESSAGE);
                break;
            }
        }
    }

    public void verifyTrolleyNotGenerated(String orderNumber) {
        JsonPath jsonPath = new JsonPath(queryItemContainers(orderNumber));
        List<Object> list = jsonPath.getList(containerPath);
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> containerMap = new HashMap<>(jsonPath.getMap(PICK_LIST_ITEMS + i + "]." + ADDITIONAL_ATTRIBUTES));
            Assert.assertFalse((containerMap.containsKey(TROLLEY_NAME)));
        }
    }

    public void verifyTrolleyGenerated(String orderNumber) {
        baseCommands.waitForElementClickability(cueHomeMap.clickRequestTrolley());
        baseCommands.webpageRefresh();
        JsonPath jsonPath = new JsonPath(queryItemContainers(orderNumber));
        List<Object> list = jsonPath.getList(containerPath);
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> containerMap = new HashMap<>(jsonPath.getMap(PICK_LIST_ITEMS + i + "]." + ADDITIONAL_ATTRIBUTES));
            Assert.assertTrue((containerMap.containsKey(TROLLEY_NAME)));
        }
    }

    public void triggerCustomerCheckIn(HashMap<String, String> testOutputData) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Kroger-Associate-Euid", PropertyUtils.getCustomerCheckInUsername());
        headers.put("X-Kroger-Receiving-Location-Id", testOutputData.get(ExcelUtils.STORE_ID));
        headers.put("X-Correlation-ID", "3dcd379e-1a62-4e66-bf91-d5040c07230a");
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("locationId", testOutputData.get(ExcelUtils.STORE_ID));
        CustomerCheckInPayload customerCheckInPayload = new CustomerCheckInPayload("12", "SEDAN", "WHITE");
        apiUtils.postPayload(PropertyUtils.getCustomerCheckInUsername(), PropertyUtils.getCustomerCheckInPassword(), new ApiUtils().convertObjectToString(customerCheckInPayload), PropertyUtils.getCustomerCheckInEndpoint().replace("orderNumber", testOutputData.get(ExcelUtils.ORDER_NUMBER)), headers, queryParams);
    }
}
