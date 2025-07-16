package com.krogerqa.web.apps;

import com.krogerqa.api.*;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeSelectingPage;
import com.krogerqa.web.ui.maps.cue.CueHomeMap;
import org.testng.Assert;
import utils.E2eListeners;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStagingPage;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.utils.WebUtils;
import com.krogerqa.web.ui.maps.cue.CueOrderDetailsMap;
import com.krogerqa.web.ui.pages.cue.CueContainerDetailsPage;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import com.krogerqa.web.ui.pages.cue.CueTrolleyDetailsPage;
import com.krogerqa.web.ui.pages.dash.DashPage;
import com.krogerqa.web.ui.pages.login.FederationSignInPage;
import com.krogerqa.web.ui.pages.seamlessportal.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.kroger.commons.krypt.core.LogUtil.log;

public class CueOrderValidation {
    public static final Logger LOGGER = LogManager.getLogger(CueOrderValidation.class);
    private static final String TEMP_TYPE_COLUMN = "Temp Type";
    static HashMap<String, String> updatedPclIdTemperatureMap = new HashMap<>();
    static HashMap<String, String> updatedPclLabelTemperatureMap = new HashMap<>();
    static String pclLabelGenerated;
    String instacartCueVisualOrderID = "";
    private static CueOrderValidation instance;
    public HashMap<String, String> orderSummaryMap = new HashMap<>();
    HashMap<String, String> pclLabelMap = new HashMap<>();
    HashMap<String, String> pclIdMap = new HashMap<>();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    CueHomeMap cueHomeMap = CueHomeMap.getInstance();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();
    CueTrolleyDetailsPage cueTrolleyDetailsPage = CueTrolleyDetailsPage.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    AssignPclHelper assignPclHelper = AssignPclHelper.getInstance();
    DashPage dashPage = DashPage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    DeliveryOrderHelper deliveryOrderHelper = DeliveryOrderHelper.getInstance();
    CueContainerDetailsPage cueContainerDetailsPage = CueContainerDetailsPage.getInstance();
    SeamlessMyPurchasesPage seamlessMyPurchasesPage = SeamlessMyPurchasesPage.getInstance();
    HarvesterNativeStagingPage harvesterNativeStagingPage = HarvesterNativeStagingPage.getInstance();
    SeamlessCheckoutCompletePage seamlessCheckoutCompletePage = SeamlessCheckoutCompletePage.getInstance();
    SeamlessUpcSearchPage seamlessUpcSearchPage = SeamlessUpcSearchPage.getInstance();
    SeamlessModifyOrderPage seamlessModifyOrderPage = SeamlessModifyOrderPage.getInstance();
    SeamlessPaymentPage seamlessPaymentPage = SeamlessPaymentPage.getInstance();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    HashMap<String, String> containerMapPcl;
    HashMap<String, String> containerMap;
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    CueOrderDetailsMap cueOrderDetailsMap = CueOrderDetailsMap.getInstance();
    FederationSignInPage federationSignInPage = FederationSignInPage.getInstance();
    WebUtils webUtils = WebUtils.getInstance();
    public static final String orderStatusNotPicked = "Order status is not getting updated to Picked in Cue";
    public static final String orderStatusNotStaged = "Order status is not getting updated to Staged in Cue";
    public static final String orderStatusNotStaging = "Order status is not getting updated to Staging in Cue";
    HarvesterServicesHelper harvesterServicesHelper = HarvesterServicesHelper.getInstance();
    KLogHelper klogHelper = KLogHelper.getInstance();
    DataDogHelper dataDogHelper = DataDogHelper.getInstance();
    HarvesterNativeSelectingPage harvesterNativeSelectingPage = HarvesterNativeSelectingPage.getInstance();

    private CueOrderValidation() {
    }

    public synchronized static CueOrderValidation getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueOrderValidation.class) {
            if (instance == null) {
                instance = new CueOrderValidation();
            }
        }
        return instance;
    }


    public HashMap<String, String> generateMapsForPclCommonTrolleys(String firstScenario, HashMap<String, String> firstOrderData, HashMap<String, String> secondOrderData) {
        try {
            HashMap<String, String> updatedPclTrolleyMap;
            HashMap<String, String> updatedPclTemperatureMap;
            HashMap<String, String> multiContainerTrolley;
            List<String> multiContainerUpcValues = new ArrayList<>();
            String firstMultiUpcList = firstOrderData.get(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES);
            String secondMultiUpcList = secondOrderData.get(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES);
            multiContainerUpcValues.add(firstMultiUpcList);
            multiContainerUpcValues.add(secondMultiUpcList);
            String firstPclTrolleyMap = StringUtils.substring(firstOrderData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP), 0, -1);
            String secondPclTrolleyMap = "," + secondOrderData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP).substring(1);
            String firstPclTempMap = StringUtils.substring(firstOrderData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP), 0, -1);
            String secondPclTempMap = "," + secondOrderData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP).substring(1);
            if (firstOrderData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES) || secondOrderData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES)) {
                firstOrderData.remove(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES);
                firstOrderData.put(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES, String.valueOf(multiContainerUpcValues));
            }
            String overSizeContainer = (firstOrderData.get(ExcelUtils.OVER_SIZED_CONTAINER) + secondOrderData.get(ExcelUtils.OVER_SIZED_CONTAINER)).replace("{}", "").replace("}{", ",");
            updatedPclTrolleyMap = ExcelUtils.convertStringToMap(firstPclTrolleyMap + secondPclTrolleyMap);
            multiContainerTrolley = updateMultiContainerTrolleys(updatedPclTrolleyMap);
            firstOrderData.remove(ExcelUtils.MULTI_CONTAINER_TROLLEY);
            if (!String.valueOf(multiContainerTrolley).equals("{}")) {
                firstOrderData.put(ExcelUtils.MULTI_CONTAINER_TROLLEY, String.valueOf(multiContainerTrolley));
            }
            updatedPclTemperatureMap = ExcelUtils.convertStringToMap(firstPclTempMap + secondPclTempMap);

            if (Boolean.parseBoolean(firstOrderData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                for (Map.Entry<String, String> PCL : updatedPclTrolleyMap.entrySet()) {
                    if (PCL.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        updatedPclTrolleyMap.remove(PCL.getKey());
                        break;
                    }
                }
                for (Map.Entry<String, String> PCL : updatedPclTemperatureMap.entrySet()) {
                    if (PCL.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        updatedPclTemperatureMap.remove(PCL.getKey());
                        break;
                    }
                }
            }
            firstOrderData.remove(ExcelUtils.OVER_SIZED_CONTAINER);
            firstOrderData.remove(ExcelUtils.PCL_LABEL_TROLLEY_MAP);
            firstOrderData.remove(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP);
            firstOrderData.put(ExcelUtils.OVER_SIZED_CONTAINER, overSizeContainer);
            firstOrderData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(updatedPclTrolleyMap));
            firstOrderData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclTemperatureMap));
            ExcelUtils.writeToExcel(firstScenario, firstOrderData);
            return firstOrderData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public void updateNonPclMapsBeforePicking(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        for (Map.Entry<String, String> e : multiContainerTrolley.entrySet()) {
            HashMap<String, String> newContainerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP));
            String deletedContainerKey = e.getKey();
            String deletedContainerValue = e.getValue();
            testOutputData.put(ExcelUtils.DELETED_CONTAINER_KEY, deletedContainerKey);
            testOutputData.put(ExcelUtils.DELETED_CONTAINER_VALUE, deletedContainerValue);
            newContainerMap.remove(e.getKey());
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(newContainerMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            break;
        }
    }

    public void updatePclMapsBeforeStaging(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> multiContainerTrolley1 = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> multiContainerTrolley2 = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        for (Map.Entry<String, String> e : multiContainerTrolley1.entrySet()) {
            String deletedContainerKey = e.getKey();
            String deletedContainerValue = e.getValue();
            testOutputData.put(ExcelUtils.DELETED_CONTAINER_KEY, deletedContainerKey);
            testOutputData.put(ExcelUtils.DELETED_CONTAINER_VALUE, deletedContainerValue);
            multiContainerTrolley2.remove(e.getKey());
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(multiContainerTrolley2));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            break;
        }
    }

    public void updateNonPclMapsAfterPicking(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> newContainerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP));
        newContainerMap.put(testOutputData.get(ExcelUtils.DELETED_CONTAINER_KEY), testOutputData.get(ExcelUtils.DELETED_CONTAINER_VALUE));
        testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(newContainerMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
    }

    public HashMap<String, String> updateMultiContainerTrolleys(HashMap<String, String> pclTrolleyMap) {
        Set<String> duplicateTrolley = new HashSet<>();
        Set<String> uniqueTrolleys = new HashSet<>();
        HashMap<String, String> multiContainerTrolley = new HashMap<>();
        for (Map.Entry<String, String> entry : pclTrolleyMap.entrySet()) {
            String value = entry.getValue();
            if (uniqueTrolleys.contains(value)) {
                duplicateTrolley.add(value);
            } else {
                uniqueTrolleys.add(value);
            }
        }
        for (Map.Entry<String, String> entry : pclTrolleyMap.entrySet()) {
            String pclLabel = entry.getKey();
            String trolley = entry.getValue();
            if (duplicateTrolley.contains(trolley)) {
                multiContainerTrolley.put(pclLabel, trolley);
            }
        }
        return multiContainerTrolley;
    }

    public HashMap<String, String> updatePclMapsForContainers(HashMap<String, String> containerMap, HashMap<String, String> testOutputData) {
        HashMap<String, String> pclLabelTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> pclIDMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> trolleyTemperatureMap = CueOrderDetailsPage.trolleyTemperatureMap;
        for (Map.Entry<String, String> container : containerMap.entrySet()) {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REUSE_PCL_SCENARIO))) {
                pclLabelMap.put(container.getKey(), trolleyTemperatureMap.get(container.getValue()));
            } else {
                break;
            }
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REUSE_PCL_SCENARIO))) {
            for (Map.Entry<String, String> container : pclLabelMap.entrySet()) {
                if (container.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    pclIdMap.put(container.getKey(), container.getValue());
                } else {
                    pclIdMap.put((container.getKey().split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID)))[1], container.getValue());
                }
            }
        } else {
            pclLabelMap = pclLabelTemperatureMap;
            pclIdMap = pclIDMap;
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclLabelMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdMap));
        return testOutputData;
    }

    public HashMap<String, String> verifyInstacartOrderInCue(HashMap<String, String> testOutputData) {
        try {
            baseCommands.wait(10);
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            cueHomePage.verifyForNewOrderStatus(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)), testOutputData);
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.instacartTagInOrderDashboard());
            String visualOrderId = cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            instacartCueVisualOrderID = visualOrderId;
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.instacartTag());
            testOutputData = cueOrderDetailsPage.getPickupTime(testOutputData);
            testOutputData.put(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID, visualOrderId);
            ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
            E2eListeners.testCaseIdOrderNumbers.put(testOutputData.get(ExcelUtils.SCENARIO), testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            E2eListeners.testCaseIdStoreNumbers.put(testOutputData.get(ExcelUtils.SCENARIO), testOutputData.get(ExcelUtils.STORE_ID));
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyOrderInCue(String scenario, HashMap<String, String> testOutputData) {
        try {
            if (!(containerMapPcl == null)) {
                this.containerMapPcl.clear();
            }
            testOutputData.put(ExcelUtils.CUE_URL, baseCommands.getUrl());
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            searchOrderInCueDashboard(testOutputData);
            performBatchingFromCue(testOutputData);
            cueHomePage.verifyForNewOrderStatus(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)), testOutputData);
            String orderSource = cueHomePage.verifyAndReturnOrderSource(testOutputData);
            String customerName = cueHomePage.getCustomerName();
            testOutputData.put(ExcelUtils.CUSTOMER_NAME, customerName);
            testOutputData.put(ExcelUtils.ORDER_SOURCE, orderSource);
            validateExpressOrderCount(testOutputData);
            String visualOrderId = cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            testOutputData = cueOrderDetailsPage.getPickupTime(testOutputData);
            verifyTrolleyDetailsInCueOrderDetails(testOutputData, ExcelUtils.getItemUpcData(scenario));
            if (containerMap != null)
                containerMap.clear();
            containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            cueOrderDetailsPage.validateOrderDetailsOnCue(containerMap, ExcelUtils.getItemUpcData(scenario), testOutputData);
            updateContainerMapFromCueUI(testOutputData);
            testOutputData = updateMapAndTriggerJobForCarryover(testOutputData, scenario);
            verifyTrolleysWhenOrderNotBatched(testOutputData);
            updateMapsInTestData(testOutputData, visualOrderId);
            validateTrolleysForDynamicBatching(testOutputData);
            ExcelUtils.writeToExcel(scenario, testOutputData);
            validateServiceCounterPageForNotReadyItems(testOutputData);
            log.info("Test output data for {} is{}", scenario, testOutputData);
            LOGGER.info("Test output data for {} is{}", scenario, testOutputData);
            E2eListeners.testCaseIdOrderNumbers.put(scenario, testOutputData.get(ExcelUtils.ORDER_NUMBER));
            E2eListeners.testCaseIdStoreNumbers.put(scenario, testOutputData.get(ExcelUtils.STORE_ID));
            return testOutputData;
        } catch (Exception | AssertionError e) {
            LOGGER.info(e);
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    private void validateServiceCounterPageForNotReadyItems(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.SC_ITEMS_MT_SCALE))) {
            validationOfServiceCounterTabOnCue(testOutputData);
        } else {
            List<HashMap<String, String>> itemList = ExcelUtils.getItemUpcData(testOutputData.get(ExcelUtils.SCENARIO));
            HashMap<String, String> upcLabel = harvesterNativeSelectingPage.getItemsWithLabelsFromTestData(itemList);
            if (upcLabel.containsKey(Constants.PickCreation.NOT_READY)) {
                validationOfServiceCounterTabOnCue(testOutputData);
            }
        }
    }

    private void validationOfServiceCounterTabOnCue(HashMap<String, String> testOutputData) {
        List<WebElement> serviceCounterItemList = searchOrderAndGetScItemDetails(testOutputData);
        boolean isSentToScaleItem = false;
        List<String> sentToScaleItems = new ArrayList<>();
        for (int i = 1; i <= serviceCounterItemList.size(); i++) {
            String status = baseCommands.getElementText(cueHomeMap.getStatusForServiceCounter(i));
            if (status.equalsIgnoreCase(Constants.SC_STATUS_NEW)) {
                Assert.assertEquals(status, Constants.SC_STATUS_NEW, Constants.INCORRECT_SC_NEW_STATUS);
            } else {
                isSentToScaleItem = true;
                Assert.assertEquals(status, Constants.SC_STATUS_SENT_TO_SCALE, Constants.INCORRECT_SC_SENT_TO_SCALE_STATUS);
                sentToScaleItems.add(baseCommands.getElementText(cueHomeMap.getItemNameForServiceCounter(i)));
            }
        }
        if (isSentToScaleItem) {
            dataDogHelper.getDataDogXmlResponse(testOutputData, Constants.ScDataDog.IN_PROCESS);
            int retry = 8;
            while (retry > 0) {
                try {
                    validateStatusForSentToScaleItems(sentToScaleItems, testOutputData, Constants.SC_STATUS_IN_PROGRESS);
                    break;
                } catch (Exception | AssertionError e) {
                    baseCommands.webpageRefresh();
                    retry--;
                }
            }
            dataDogHelper.getDataDogXmlResponse(testOutputData, Constants.ScDataDog.FINISHED);
            retry = 8;
            while (retry > 0) {
                try {
                    validateStatusForSentToScaleItems(sentToScaleItems, testOutputData, Constants.SC_STATUS_READY_AT_COUNTER);
                    break;
                } catch (Exception | AssertionError e) {
                    baseCommands.webpageRefresh();
                    retry--;
                }
            }
        }
    }

    private void validateStatusForSentToScaleItems(List<String> sentToScaleItems, HashMap<String, String> testOutputData, String expectedStatus) {
        List<WebElement> serviceCounterItemList = searchOrderAndGetScItemDetails(testOutputData);
        for (int i = 1; i <= serviceCounterItemList.size(); i++) {
            baseCommands.waitForElementVisibility(cueHomeMap.getItemNameForServiceCounter(i));
            if (sentToScaleItems.contains(baseCommands.getElementText(cueHomeMap.getItemNameForServiceCounter(i)))) {
                baseCommands.waitForElementVisibility(cueHomeMap.getStatusForServiceCounter(i));
                String actualStatus = baseCommands.getElementText(cueHomeMap.getStatusForServiceCounter(i));
                if (expectedStatus.equalsIgnoreCase(Constants.SC_STATUS_IN_PROGRESS)) {
                    Assert.assertEquals(actualStatus, expectedStatus, Constants.INCORRECT_SC_IN_PROGRESS_STATUS);
                } else {
                    Assert.assertEquals(actualStatus, expectedStatus, Constants.INCORRECT_SC_READY_AT_COUNTER_STATUS);
                }
            }
        }
    }

    private List<WebElement> searchOrderAndGetScItemDetails(HashMap<String, String> testOutputData) {
        baseCommands.waitForElementVisibility(cueHomeMap.serviceCounterTab());
        baseCommands.click(cueHomeMap.serviceCounterTab());
        cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
        return cueHomePage.itemList(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
    }

    private void verifyTrolleysWhenOrderNotBatched(HashMap<String, String> testOutputData) {
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_ORDER_ROMB)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SINGLE_THREAD))) {
            if (!testOutputData.containsKey(ExcelUtils.IS_DYNAMIC_BATCHING)) {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
                    cueOrderDetailsPage.verifyTrolleysNotCreated();
                }
            }
        }
    }

    private void validateTrolleysForDynamicBatching(HashMap<String, String> testOutputData) {
        if (testOutputData.containsKey(ExcelUtils.IS_DYNAMIC_BATCHING) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING)) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER)))) {
            testOutputData = cueOrderDetailsPage.verifyTrolleysCreated(testOutputData);
            testOutputData = verifyTrolleyAssociateNameAndRouteStepsInCue(testOutputData);
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
        }
    }

    private void updateMapsInTestData(HashMap<String, String> testOutputData, String visualOrderId) {
        testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
        testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, visualOrderId);
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
        } else {
            testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMap)));
        }
    }

    private HashMap<String, String> updateMapAndTriggerJobForCarryover(HashMap<String, String> testOutputData, String scenario) {
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
        if ((Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)) || Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER))) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_SINGLE_THREAD_CARRYOVER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER_ORDER_WITHOUT_PICKING))) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
                this.containerMapPcl = assignPclHelper.assignPclToContainers(scenario, testOutputData, this.containerMapPcl, testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData);
                testOutputData = updatePclMapsForContainers(this.containerMapPcl, testOutputData);
                baseCommands.webpageRefresh();
            }
            if (testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY) != null && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
                testOutputData = pickingServicesHelper.pickAnItemFromTrolley(testOutputData, Constants.Date.PREVIOUS_DAY, itemData);
                pickingServicesHelper.triggerCarryoverLoadTrolley();
            } else {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
                    pickingServicesHelper.pickTrolleysWithApi(testOutputData.get(ExcelUtils.ORDER_NUMBER), testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.PREVIOUS_DAY, itemData);
                    baseCommands.webpageRefresh();
                    baseCommands.browserBack();
                    cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                    cueHomePage.verifyOrderPickingStatus();
                }
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_SINGLE_THREAD_CARRYOVER)) || (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER_ORDER_WITHOUT_PICKING)))) {
            pickingServicesHelper.triggerCarryoverLoadTrolley();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
            pickingServicesHelper.triggerCarryoverLoadTrolley();
        }
        return testOutputData;
    }

    private void updateContainerMapFromCueUI(HashMap<String, String> testOutputData) {
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
            CueOrderDetailsPage.getMultipleContainerTrolley(containerMap, testOutputData);
            cueOrderDetailsPage.getContainerLabelForNonPcl(testOutputData);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
            this.containerMapPcl = cueOrderDetailsPage.getPclLabelsForContainers(testOutputData);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            this.containerMapPcl = cueOrderDetailsPage.getPclLabelsForContainers(testOutputData);
        }
    }

    private void verifyTrolleyDetailsInCueOrderDetails(HashMap<String, String> testOutputData, List<HashMap<String, String>> itemsList) {
        if ((testOutputData.containsKey(ExcelUtils.ENHANCED_BATCHING_MAP)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
            cueOrderDetailsPage.verifyTemperatureCollapseForEnhancedBatching(testOutputData, itemsList);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH))) {
            cueOrderDetailsPage.verifyCollapseAllTrolley();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER))) {
            cueOrderDetailsPage.verifyTrolleyDetailsForCollapseTempAll(testOutputData);
        }
    }

    private void validateExpressOrderCount(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EXPRESS_ORDER))) {
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            verifyExpressOrderLabelDisplayed(testOutputData);
            cueHomePage.verifyExpressOrderNewCount();
            cueHomePage.verifyExpressOrderCountIncreased();
        }
    }

    private void performBatchingFromCue(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
            requestTrolleyForDynamicBatching(testOutputData);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH))) {
            isRombCheckBoxEnabled(testOutputData);
            while (!cueHomePage.isOrderVisible() && !cueHomePage.isRushOrderCheckBoxVisible()) {
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            String visualOrderId = cueOrderDetailsPage.getVisualOrderId();
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            performSingleRushOrderBatching(visualOrderId);
        }
    }

    private void searchOrderInCueDashboard(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER))) {
            baseCommands.getWebDriverWait();
            performManualBatching(testOutputData);
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)) || Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER))) {
            cueHomePage.expandCarryoverOrdersSection();
        }
    }

    public void verifyPickingStatusInCue(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
                cueHomePage.expandCarryoverOrdersSection();
            }
            cueHomePage.verifyOrderPickingStatus();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyTrolleyAssociateNameAndRouteStepsInCue(HashMap<String, String> testOutputData) {
        cueOrderDetailsPage.openTrolleyPageInCue();
        List<String> trolleys = new ArrayList<>((ExcelUtils.convertStringToList(testOutputData.get(ExcelUtils.TROLLEYS))));
        for (String trolley : trolleys) {
            testOutputData = cueTrolleyDetailsPage.verifyTrolleyAssociateAndRouteSteps(trolley, testOutputData);
        }
        return testOutputData;
    }

    public HashMap<String, String> verifyOrderInCueReusePcl(String scenario, String scenario1, HashMap<String, String> testOutputData) {
        try {
            //**Cue - verify new order*//*
            testOutputData.put(ExcelUtils.CUE_URL, baseCommands.getUrl());
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            performBatching(testOutputData);
            cueHomePage.verifyForNewOrderStatus(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)), testOutputData);
            String orderSource = cueHomePage.verifyAndReturnOrderSource(testOutputData);
            String customerName = cueHomePage.getCustomerName();
            String visualOrderId = cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            if (containerMap != null)
                containerMap.clear();
            containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            cueOrderDetailsPage.validateOrderDetailsOnCue(containerMap, ExcelUtils.getItemUpcData(scenario), testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                this.containerMapPcl = cueOrderDetailsPage.getReusePclLabelsForContainers(scenario1);
            }
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER)) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING)))) {
                cueOrderDetailsPage.verifyTrolleysNotCreated();
            }
            testOutputData.put(ExcelUtils.ORDER_SOURCE, orderSource);
            testOutputData.put(ExcelUtils.CUSTOMER_NAME, customerName);
            testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, visualOrderId);
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
                testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REUSE_PCL_SCENARIO))) {
                    testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
                }
            } else {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMap)));
            }
            CueOrderDetailsPage.getMultipleContainerTrolley(containerMapPcl, testOutputData);
            E2eListeners.testCaseIdOrderNumbers.put(scenario, testOutputData.get(ExcelUtils.ORDER_NUMBER));
            E2eListeners.testCaseIdStoreNumbers.put(scenario, testOutputData.get(ExcelUtils.STORE_ID));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    private void performBatching(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
            performManualBatching(testOutputData);
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
            cueOrderDetailsPage.requestTrolleyForDynamicBatchingFromApi(testOutputData);
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        }
    }

    public HashMap<String, String> eteOrderApi(String scenario, String scenario1, HashMap<String, String> testOutputData) {
        try {
            /*Verify New order and Perform Ete from API*/
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.put(ExcelUtils.CUE_URL, baseCommands.getUrl());
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            performBatching(testOutputData);
            cueHomePage.verifyForNewOrderStatus(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)), testOutputData);
            String customerName = cueHomePage.getCustomerName();
            String orderSource = cueHomePage.verifyAndReturnOrderSource(testOutputData);
            String visualOrderId = cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            cueOrderDetailsPage.validateOrderDetailsOnCue(containerMap, ExcelUtils.getItemUpcData(scenario), testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                this.containerMapPcl = cueOrderDetailsPage.getPclLabelsForContainers(testOutputData);
            }
            this.containerMapPcl = assignPclHelper.assignPclToContainers(scenario, testOutputData, this.containerMapPcl, testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData);
            testOutputData = updatePclMapsForContainers(containerMapPcl, testOutputData);
            pickingServicesHelper.pickTrolleysWithApi(testOutputData.get(ExcelUtils.ORDER_NUMBER), testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY, itemData);
            testOutputData.put(ExcelUtils.ORDER_SOURCE, orderSource);
            testOutputData.put(ExcelUtils.CUSTOMER_NAME, customerName);
            testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, visualOrderId);
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
            testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            harvesterNativeStagingPage.verifyHarvesterStagingReusePcl(containerMap, testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
            ExcelUtils.writeToExcel(scenario, testOutputData);
            ExcelUtils.writeToExcel(scenario1, testOutputData);
            E2eListeners.testCaseIdOrderNumbers.put(scenario, testOutputData.get(ExcelUtils.ORDER_NUMBER));
            E2eListeners.testCaseIdStoreNumbers.put(scenario, testOutputData.get(ExcelUtils.STORE_ID));
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyPickedStatusInCueForInstacartOrder(HashMap<String, String> testOutputData) {
        try {
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            verifyOrderPickedStatus(testOutputData);
            cueOrderDetailsPage.verifyCueStatusForInstacartOrder(testOutputData, Constants.Instacart.PICKED);
            krogerSeamLessPortalOrderCreation.getStagingZones(testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyPickedStatusInCue(String scenario, HashMap<String, String> testOutputData) {
        try {
            /*Cue - verify picked status*/
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
                cueHomePage.expandCarryoverOrdersSection();
            }
            verifyOrderPickedStatus(testOutputData);
            cueOrderDetailsPage.verifyContainersAfterPicking(scenario, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)), testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NON_PCL_IS_OOS_SHORT))) {
                cueOrderDetailsPage.verifyItemsNotFulfilled(ExcelUtils.getItemUpcData(scenario));
            }
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public void verifyOrderPickedStatus(HashMap<String, String> testOutputData) {
        int retry = 8, i = 0;
        while (i < retry) {
            try {
                cueHomePage.verifyOrderPickedStatus();
                break;
            } catch (Exception | AssertionError e) {
                searchOrderAgainInCue(testOutputData);
                i++;
            }
        }
        if (i >= retry) {
            Assert.fail(orderStatusNotPicked);
        }
    }

    public void verifyOrderStagedStatus(HashMap<String, String> testOutputData) {
        int retry = 8, i = 0;
        while (i < retry) {
            try {
                cueHomePage.verifyStageCompletionStatus();
                break;
            } catch (Exception | AssertionError e) {
                searchOrderAgainInCue(testOutputData);
                i++;
            }
        }
        if (i >= retry) {
            Assert.fail(orderStatusNotStaged);
        }
    }

    private void searchOrderAgainInCue(HashMap<String, String> testOutputData) {
        baseCommands.webpageRefresh();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
        } else {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
            cueHomePage.expandCarryoverOrdersSection();
        }
    }

    public HashMap<String, String> verifyPclPickedStatusInCue(String scenario, HashMap<String, String> testOutputData) {
        try {
            /*Cue - verify picked status*/
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
                cueHomePage.expandCarryoverOrdersSection();
            }
            verifyOrderPickedStatus(testOutputData);
            cueOrderDetailsPage.verifyPclContainersAfterPicking(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData, scenario);
            if (testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY) != null && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
                HashMap<String, String> containerTempMap = cueOrderDetailsPage.getAllContainersTemperatureWithStoreLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
                HashMap<String, String> pclIdContainerTempMap = cueOrderDetailsPage.getAllContainersTemperatureWithoutStoreLocation();
                testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(containerTempMap));
                testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdContainerTempMap));
                ExcelUtils.writeToExcel(scenario, testOutputData);
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT))) {
                cueOrderDetailsPage.verifyItemsNotFulfilled(ExcelUtils.getItemUpcData(scenario));
            }
            harvesterServicesHelper.getStagingZones(testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public void updatePclMapsAfterPicking(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> containerTempMap = cueOrderDetailsPage.getAllContainersTemperatureWithStoreLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            HashMap<String, String> pclIdContainerTempMap = cueOrderDetailsPage.getAllContainersTemperatureWithoutStoreLocation();
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(containerTempMap));
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdContainerTempMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyStagedStatusInCue(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
                cueHomePage.expandCarryoverOrdersSection();
            }
            verifyOrderStagedStatus(testOutputData);
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            cueOrderDetailsPage.validateAfterStagingCompletion(testOutputData);
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NON_PCL_IS_OOS_SHORT))) {
                cueOrderDetailsPage.verifyOutOfStockContainerNotStaged(testOutputData);
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            /*Cue & Dash - verify customer check-in*/
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_CLAIMED, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_SHOPPER_ARRIVED, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_CLAIMED, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_SHOPPER_ARRIVED, testOutputData);
            } else {
                cueHomePage.customerCheckIn(testOutputData.get(ExcelUtils.SPOT));
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
        try {
            baseCommands.openNewUrl(PropertyUtils.getDashUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            dashPage.verifyOrderInDash(testOutputData, testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.DASH, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyReadyForStagingInDash(HashMap<String, String> testOutputData) {
        try {
            baseCommands.openNewUrl(PropertyUtils.getDashUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            dashPage.verifyInstacartOrderInDash(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID));
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.DASH, String.valueOf(e));
            return testOutputData;
        }
    }

    public void verifyStagedStatusInCueForInstacartOrder(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            verifyOrderStagedStatus(testOutputData);
            cueOrderDetailsPage.verifyCueStatusForInstacartOrder(testOutputData, Constants.Instacart.STAGED);
            instacartHelper.triggerOnMyWayForInstacartOrder(testOutputData);
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            cueHomePage.customerCheckIn(testOutputData.get(ExcelUtils.SPOT));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
        try {
            baseCommands.openNewUrl(PropertyUtils.getDashUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            dashPage.verifyOrderInDash(testOutputData, testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.DASH, String.valueOf(e));
        }
    }

    public void verifyStagedPclStatusInCue(HashMap<String, String> testOutputData) {
        String updateDeliveryStatus = Constants.Shipt.SHIPT_STATUS_CLAIMED;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            updateDeliveryStatus = Constants.Shipt.SHIPT_FIF_STATUS_OPEN;
        }
        try {
            verifyOrderStagedStatus(testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_RUSH_ORDER))) {
                cueHomePage.verifyRushOrderLabelRemoved();
            }
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            cueOrderDetailsPage.validateAfterPclStagingCompletion(testOutputData);
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT))) {
                cueOrderDetailsPage.verifyOutOfStockPclContainerNotStaged(testOutputData);
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            /*Cue & Dash - verify customer check-in*/
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_CLAIMED, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_SHOPPER_ARRIVED, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), updateDeliveryStatus, testOutputData);
                deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_SHOPPER_ARRIVED, testOutputData);
            } else {
                cueHomePage.customerCheckIn(testOutputData.get(ExcelUtils.SPOT));
                pickingServicesHelper.triggerCustomerCheckIn(testOutputData);
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
        try {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                pickingServicesHelper.triggerCustomerCheckIn(testOutputData);
            }
            baseCommands.openNewUrl(PropertyUtils.getDashUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            dashPage.verifyOrderInDash(testOutputData, testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.DASH, String.valueOf(e));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_CANCEL_ORDER))) {
            deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_CANCELLED, testOutputData);
            deliveryOrderHelper.updateDeliveryOrderStatus(testOutputData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_CANCELLED, testOutputData);
        }
    }

    public void verifyPickedUpAndPaidForInstacartOrder(HashMap<String, String> testOutputData) {
        try {
            baseCommands.wait(10);
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
        } catch (Exception | AssertionError e) {
            cueHomePage.searchOrderId(instacartCueVisualOrderID);
        }
        selectPreviousDate(testOutputData);
        cueHomePage.verifyPickedUpPaidStatus(testOutputData);
        cueOrderDetailsPage.verifyCueStatusForInstacartOrder(testOutputData, Constants.Instacart.PICKED_UP);
    }

    public void verifyStagingStatusInCue(HashMap<String, String> testOutputData) {
        cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        int retry = 8, i = 0;
        while (i < retry) {
            try {
                cueHomePage.verifyStagingStatus();
                break;
            } catch (Exception | AssertionError e) {
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                i++;
            }
        }
        if (i >= retry) {
            Assert.fail(orderStatusNotStaging);
        }
    }

    public void selectPreviousDate(HashMap<String, String> orderData) {
        String pickUpTime = orderData.get(ExcelUtils.PICKUP_TIME);
        int time = Integer.parseInt(pickUpTime.split(":")[0]);
        if (time <= 5) {
            cueHomePage.selectDate(Constants.Date.PREVIOUS_DAY);
        }
    }

    public void verifyPickedUpAndPaid(String scenario) {
        try {
            HashMap<String, String> orderData = ExcelUtils.getTestDataMap(scenario, scenario);
            /*Cue - verify picked up & paid status*/
            cueHomePage.searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(orderData.get(ExcelUtils.IS_CARRYOVER))) {
                cueHomePage.selectDate(Constants.Date.PREVIOUS_DAY);
            }
            if ((Boolean.parseBoolean(orderData.get(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH)) && Boolean.parseBoolean(orderData.get(ExcelUtils.IS_RUSH_ORDER))) || Boolean.parseBoolean(orderData.get(ExcelUtils.IS_EXPRESS_ORDER))) {
                selectPreviousDate(orderData);
            }
            if (Boolean.parseBoolean(orderData.get(ExcelUtils.IS_EXPRESS_ORDER))) {
                selectPreviousDate(orderData);
            }
            if (Boolean.parseBoolean(orderData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(orderData.get(ExcelUtils.STORE_ID)));
                cueHomePage.searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
                cueHomePage.verifyReadyForDelivery(orderData);
                deliveryOrderHelper.updateDeliveryOrderStatus(orderData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_PICKED_UP, orderData);
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(orderData.get(ExcelUtils.STORE_ID)));
                cueHomePage.searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
            }
            if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                cueHomePage.verifyPickedUpPaidStatus(orderData);
            }
            if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_EBT))) {
                cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
                if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_BYOB_FLOW))) {
                    cueOrderDetailsPage.verifyTotalAfterCheckout(orderData.get(ExcelUtils.AMOUNT_PROCESSED));
                }
            }
            if (Boolean.parseBoolean(orderData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                deliveryOrderHelper.updateDeliveryOrderStatus(orderData.get(ExcelUtils.ORDER_NUMBER), Constants.Shipt.SHIPT_STATUS_DELIVERED, orderData);
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(orderData.get(ExcelUtils.STORE_ID)));
                cueHomePage.searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
                cueHomePage.verifyDeliveredStatus(orderData);
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.visualOrderIdText());
                baseCommands.click(cueOrderDetailsMap.visualOrderIdText());
            } else {
                cueOrderDetailsPage.verifyPickedAndPaidStatusInOrderDetailsPage();
            }
            /*Verify KLog*/
            if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_EBT))) {
                if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                    klogHelper.verifyKLogResponse(orderData.get(ExcelUtils.ORDER_NUMBER), orderData.get(ExcelUtils.STORE_ID), orderData.get(ExcelUtils.LOYALTY_ID), orderData.get(ExcelUtils.AMOUNT_PROCESSED));
                }
            }
            /*Verify Control tower response through API*/
        /*Commented the below for now as the implementation for the same is sti in progress
         if (!Boolean.parseBoolean(orderData.get(ExcelUtils.IS_EBT))) {
         echoLogHelper.controlTowerApiResponseVerification(orderData.get(ExcelUtils.ORDER_NUMBER));
         }*/
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyCanceledStatusInCue(HashMap<String, String> testOutputData) {
        try {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER))) {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            } else {
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EXPRESS_ORDER))) {
                String pickUpTime = testOutputData.get(ExcelUtils.PICKUP_TIME);
                int time = Integer.parseInt(pickUpTime.split(":")[0]);
                if (time <= 5) {
                    cueHomePage.selectDate(Constants.Date.PREVIOUS_DAY);
                }
            }
            cueHomePage.verifyOrderCancelled(testOutputData);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                cueOrderDetailsPage.verifyCancelledStatusInOrderDetailsPage();
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyOosOrderStatusInCue(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueHomePage.verifyOOSStatus();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyCancelOrderFromCue(HashMap<String, String> testOutputData) {
        try {
            baseCommands.webpageRefresh();
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            } else {
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, baseCommands.getElementText(cueOrderDetailsMap.visualOrderIdText()));
            }
            ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
            cueHomePage.cancelOrder();
            federationSignInPage.pingOneAuthentication(PropertyUtils.getCueUsername(), PropertyUtils.getCuePassword());
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyTrolleysNotCreatedForCanceledOrder() {
        try {
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            cueOrderDetailsPage.verifyTrolleysNotCreated();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> modifyOrder(String scenario, String modifyScenario, String checkoutCompositeScenario, HashMap<String, String> testOutputData) {
        /* Modify order*/
        try {
            testOutputData.put(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER, String.valueOf(false));
            testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(false));
            seamlessCheckoutCompletePage.selectOrderNumber(testOutputData.get(ExcelUtils.ORDER_CONFIRMATION_URL));
            seamlessMyPurchasesPage.modifyOrder();
            List<HashMap<String, String>> itemDataModified = ExcelUtils.getItemUpcData(modifyScenario);
            seamlessUpcSearchPage.reviewItems(itemDataModified);
            seamlessModifyOrderPage.finishChanges();
            orderSummaryMap = seamlessPaymentPage.getOrderSummaryAndSubmit(testOutputData);
            seamlessCheckoutCompletePage.getOrderConfirmation(testOutputData);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
            } else {
                cueOrderDetailsPage.requestTrolleyForDynamicBatchingFromApi(testOutputData);
            }
            baseCommands.openNewUrl(testOutputData.get(ExcelUtils.CUE_URL));
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueHomePage.verifyForNewOrderStatus(false, testOutputData);
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            containerMap = cueOrderDetailsPage.getAllContainerTrolleyDetails();
            validatingOrderDetailsOnCue(modifyScenario, testOutputData, containerMap);
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
            testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMap)));
            /*generate PCL*/
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
                this.containerMapPcl = cueOrderDetailsPage.getPclLabelsForContainers(testOutputData);
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
                testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            }
            testOutputData.put(ExcelUtils.MODIFY_SCENARIO_NAME, modifyScenario);
            ExcelUtils.writeToExcel(scenario, testOutputData);
        } catch (Exception e) {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
            } else {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
                requestTrolleyForDynamicBatching(testOutputData);
            }
            testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(scenario, testOutputData);
            krogerSeamLessPortalOrderCreation.checkoutCompositeModifyOrder(checkoutCompositeScenario, testOutputData);
            testOutputData.put(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER, String.valueOf(true));
            testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
            testOutputData.put(ExcelUtils.MODIFY_SCENARIO_NAME, checkoutCompositeScenario);
            ExcelUtils.writeToExcel(checkoutCompositeScenario, testOutputData);
        }
        return testOutputData;
    }

    public void verifyOrderDroppedFromDash(HashMap<String, String> testOutputData) {
        try {
            boolean postCheckout = true;
            baseCommands.openNewUrl(PropertyUtils.getDashUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            dashPage.verifyDashAfterDeStaging(testOutputData, testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), postCheckout);
        } catch (Exception e) {
            webUtils.captureScreenshot(Constants.ApplicationName.DASH, String.valueOf(e));
        }
    }

    private void validatingOrderDetailsOnCue(String scenario, HashMap<String, String> testOutputData, HashMap<String, String> containerMap) {
        cueOrderDetailsPage.verifyItemsUpcAndQty(testOutputData, ExcelUtils.getItemUpcData(scenario));
        if (!(CueContainerDetailsPage.overSizeContainerUpcMap == null)) {
            CueContainerDetailsPage.overSizeContainerUpcMap.clear();
        }
        for (int i = 1; i <= containerMap.size(); i++) {
            String containerId = cueOrderDetailsPage.clickOnContainers(i);
            cueContainerDetailsPage.addItemsUpc(containerId, testOutputData);
            baseCommands.browserBack();
        }
        cueContainerDetailsPage.verifyItemsUpc(ExcelUtils.getItemUpcData(scenario));
        cueOrderDetailsPage.verifyTemperatureType(ExcelUtils.getItemUpcData(scenario));
    }

    public void verifyFromContainerStagedStatusInCue(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueHomePage.verifyStagingStatus();
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            String fromContainerLabel = PermanentContainerLabelHelper.fromContainerLabel.substring(6);
            cueOrderDetailsPage.validateFromContainerStagingCompletion(fromContainerLabel);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyToContainerStagedStatusInCue(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueHomePage.verifyStagingStatus();
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            String toContainerLabel = PermanentContainerLabelHelper.toContainerLabel.substring(6);
            cueOrderDetailsPage.validateToContainerStagingCompletion(toContainerLabel);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void updatePclMapsForMultiOrder(String scenario, HashMap<String, String> testOutputData) {
        try {
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            testOutputData.remove(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP);
            testOutputData.remove(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
            String[] columnHeadingList = {TEMP_TYPE_COLUMN};
            HashMap<String, Integer> columnHeading = cueOrderDetailsPage.getColumnHeadingMap(columnHeadingList);
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
            for (int i = 0; i < containerIdList.size(); i++) {
                if (containerIdList.get(i).getText().contains("T0")) {
                    updatedPclIdTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated).substring(6), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                    updatedPclLabelTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                } else {
                    updatedPclIdTemperatureMap.put(containerIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                    updatedPclLabelTemperatureMap.put(containerIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                }
            }
            testOutputData.put(ExcelUtils.CANCEL_TROLLEY_PCL_ID_TEMPERATURE_MAP, String.valueOf(updatedPclIdTemperatureMap));
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(updatedPclIdTemperatureMap));
            testOutputData.put(ExcelUtils.CANCEL_TROLLEY_PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclLabelTemperatureMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclLabelTemperatureMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyOrderInCueReuseStagedPcl(String scenario, String scenario1, HashMap<String, String> testOutputData) {
        try {
            //**Cue - verify new order*//*
            testOutputData.put(ExcelUtils.CUE_URL, baseCommands.getUrl());
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER))) {
                baseCommands.getWebDriverWait();
                performManualBatching(testOutputData);
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            cueHomePage.verifyForNewOrderStatus(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)), testOutputData);
            String orderSource = cueHomePage.verifyAndReturnOrderSource(testOutputData);
            String visualOrderId = cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            HashMap<String, String> containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            cueOrderDetailsPage.validateOrderDetailsOnCue(containerMap, ExcelUtils.getItemUpcData(scenario), testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                this.containerMapPcl = cueOrderDetailsPage.getReuseStagedPclLabelsForContainers(scenario1, testOutputData);
            }
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
                cueOrderDetailsPage.verifyTrolleysNotCreated();
            }
            testOutputData.put(ExcelUtils.ORDER_SOURCE, orderSource);
            testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, visualOrderId);
            testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
                testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMapPcl)));
                testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(containerMapPcl));
            } else {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pickingServicesHelper.getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), containerMap)));
            }
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> getTrolleyMapForFlexValidation(HashMap<String, String> testOutputData) {
        cueOrderDetailsPage.getTrolleyTemperatureMapForFlex(testOutputData);
        return testOutputData;
    }

    public HashMap<String, String> updateMapsForFlexPicking(String scenario, HashMap<String, String> testOutputData) {
        cueOrderDetailsPage.updateMapsForFlexPicking(testOutputData);
        ExcelUtils.writeToExcel(scenario, testOutputData);
        return testOutputData;
    }

    public void verifyTrolley(HashMap<String, String> testOutputData, HashMap<String, String> testOutputData2, HashMap<String, String> testOutputData3) {
        try {
            //**Cue - verify new order*//*
            testOutputData.put(ExcelUtils.CUE_URL, baseCommands.getUrl());
            pickingServicesHelper.requestTrolley();
            cueOrderDetailsPage.validateTrolleyGenerated(testOutputData);
            cueOrderDetailsPage.validateTrolleyNotGenerated(testOutputData2);
            cueOrderDetailsPage.validateTrolleyNotGenerated(testOutputData3);
            pickingServicesHelper.requestTrolley();
            cueOrderDetailsPage.validateTrolleyGenerated(testOutputData);
            cueOrderDetailsPage.validateTrolleyNotGenerated(testOutputData3);
            pickingServicesHelper.verifyAllContainerGeneratedForOrder(testOutputData3.get(ExcelUtils.ORDER_NUMBER));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> updateReusePclTemperatureMaps(HashMap<String, String> baseOrderData, HashMap<String, String> testOutputData, String scenario) {
        try {
            String pclLabelMap = baseOrderData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP);
            String pclIDMap = baseOrderData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
            HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(baseOrderData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
            HashMap<String, String> checkForNewTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.NOT_PICKED_CONTAINER_MAP));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING))) {
                String newTrolleyValue = "";
                for (Map.Entry<String, String> newTrolley : checkForNewTrolley.entrySet()) {
                    newTrolleyValue = newTrolley.getValue();
                    break;
                }
                for (Map.Entry<String, String> pclTrolley : pclTrolleyMap.entrySet()) {
                    String pclTrolleyValue = pclTrolley.getValue().replace(pclTrolley.getValue(), newTrolleyValue);
                    pclTrolleyMap.put(pclTrolley.getKey(), pclTrolleyValue);
                }
                testOutputData.remove(ExcelUtils.PCL_LABEL_TROLLEY_MAP);
                testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMap));
            }
            testOutputData.remove(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
            testOutputData.remove(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP);
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, pclLabelMap);
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, pclIDMap);
            ExcelUtils.writeToExcel(scenario, testOutputData);
            LOGGER.info("Test output data for {} is {}", scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public void performManualBatching(HashMap<String, String> testOutputData) {
        cueOrderDetailsPage.performManualBatchingForRequiredTimeSlot(testOutputData);
    }

    private void performSingleRushOrderBatching(String visualOrderId) {
        cueHomePage.performSingleRushOrderBatching(visualOrderId);
    }

    public HashMap<String, String> getContainersForCollapseAllTrolley(String scenario, HashMap<String, String> testOutputData) {
        try {
            baseCommands.browserBack();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            cueOrderDetailsPage.openTrolleyPage();
            List<String> containerIdList = cueTrolleyDetailsPage.getContainerList();
            testOutputData.put(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS, String.valueOf(containerIdList));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> getItemCount(String scenario, HashMap<String, String> testOutputData) {
        try {
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            String totalItemsInCue = cueHomePage.getTotalItemCountInCue();
            testOutputData.put(ExcelUtils.ITEM_COUNT, totalItemsInCue);
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> updatePclTemperatureMap(String scenario, HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            HashMap<String, String> containerMap = cueOrderDetailsPage.getAllContainersTemperature(testOutputData);
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(containerMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }
    }

    public void requestTrolleyForDynamicBatching(HashMap<String, String> testOutputData) {
        cueOrderDetailsPage.requestTrolleyForDynamicBatchingFromApi(testOutputData);
        baseCommands.webpageRefresh();
        cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    public HashMap<String, String> getContainersForCollapseTrolleys(String scenario, HashMap<String, String> testOutputData) {
        try {
            baseCommands.browserBack();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
            List<String> uniqueTrolleys = cueOrderDetailsPage.getUniqueTrolleyDetails();
            testOutputData.put(ExcelUtils.TROLLEY_COUNT, String.valueOf(uniqueTrolleys.size()));
            int i = 1;
            for (String trolley : uniqueTrolleys) {
                cueOrderDetailsPage.openSpecificTrolley(trolley);
                List<String> containerIdList = cueTrolleyDetailsPage.getContainersForEachTrolley();
                testOutputData.put(ExcelUtils.TROLLEY_CONTAINERS + i, containerIdList.toString());
                i++;
                baseCommands.browserBack();
            }
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
            return null;
        }

    }

    public void getPclPreWeighedContainer(HashMap<String, String> testOutputData) {
        HashMap<String, String> pclPreWeighedContainers = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_TOTE_MAP));
        String pclPreWeighedContainerValue = testOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER);
        for (Map.Entry<String, String> preWeighedContainer : pclPreWeighedContainers.entrySet()) {
            if (pclPreWeighedContainerValue.equals(preWeighedContainer.getKey())) {
                String pclPreWeighedValue = preWeighedContainer.getValue();
                testOutputData.put(ExcelUtils.PRE_WEIGHED_CONTAINER, pclPreWeighedValue);
                ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
            }
        }
    }

    public void updateContainerMap(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> notReadyContainerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.NOT_READY_CONTAINER));
        HashMap<String, String> containerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP));
        containerMap.putAll(notReadyContainerMap);
        testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
    }

    public void isRombCheckBoxEnabled(HashMap<String, String> testOutputData) {
        int i = 0;
        while (i < 10) {
            try {
                i++;
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                if (baseCommands.elementDisplayed(cueHomeMap.isRombCheckboxEnabled())) {
                    Assert.assertTrue(cueHomePage.isRombCheckboxEnabled());
                }
                break;
            } catch (Exception | AssertionError e) {
                baseCommands.webpageRefresh();
                cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
        }
    }

    public void verifyExpressOrderLabelDisplayed(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.verifyExpressOrderLabel();
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            cueHomePage.verifyExpressOrderLabel();
            cueHomePage.verifyRushOrderLabel();
        }
    }

    public HashMap<String, String> updatePclTrolleyMap(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        ArrayList<String> uniqueTrolleys = new ArrayList<>();
        List<WebElement> trolleyIdList = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        for (int i = 0; i <= baseCommands.numberOfElements(cueOrderDetailsMap.trolleyRowList()) - 1; i++) {
            if (!uniqueTrolleys.contains(trolleyIdList.get(i).getText())) {
                uniqueTrolleys.add(trolleyIdList.get(i).getText());
                if (uniqueTrolleys.size() == pclTrolleyMap.size()) {
                    break;
                }
            }
        }
        int index = 0;
        for (String trolley : pclTrolleyMap.keySet()) {
            if (index < uniqueTrolleys.size()) {
                pclTrolleyMap.put(trolley, uniqueTrolleys.get(index));
                index++;
            }
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
        return testOutputData;
    }

    public HashMap<String, String> getMultipleOrderBagCount(String scenario, List<HashMap<String, String>> multipleOrderTestData, HashMap<String, String> testOutputData) {
        HashMap<String, Integer> bagMap = new HashMap<>();
        for (Map<String, String> testData : multipleOrderTestData) {
            HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
            for (String containerWithBag : pclTrolleyMap.keySet()) {
                if ((!containerWithBag.contains(Constants.PickCreation.OVERSIZE)) && (Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES)))) {
                    bagMap.put(containerWithBag, 1);
                }
            }
        }
        testOutputData.put(ExcelUtils.BAG_MAP, String.valueOf(bagMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
        return testOutputData;
    }

    public static void updateMapsForDbOsSplitTrolley(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> pclLabelTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> updatedPclLabelTrolleyMap = new HashMap<>();
        HashMap<String, String> pclLabelTempMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> updatedPclLabelTempMap = new HashMap<>();
        HashMap<String, String> pclLabelIdMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> updatedPclLabelIdMap = new HashMap<>();
        String osLabel = "";
        boolean overSize = false;
        for (Map.Entry<String, String> map : pclLabelTrolleyMap.entrySet()) {
            if (map.getKey().contains(Constants.PickCreation.OVERSIZE) && !overSize) {
                updatedPclLabelTrolleyMap.put(map.getKey(), map.getValue());
                osLabel = map.getKey();
                overSize = true;
            } else if (!map.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                updatedPclLabelTrolleyMap.put(map.getKey(), map.getValue());
            }
        }
        for (Map.Entry<String, String> map : pclLabelTempMap.entrySet()) {
            if (map.getValue().contains(HarvesterServicesHelper.FROZEN_TEMPERATURE)) {
                updatedPclLabelTempMap.put(map.getKey(), map.getValue());
            }
            if (map.getKey().contains(osLabel)) {
                updatedPclLabelTempMap.put(map.getKey(), map.getValue());
            }
        }
        for (Map.Entry<String, String> map : pclLabelIdMap.entrySet()) {
            if (map.getValue().contains(HarvesterServicesHelper.FROZEN_TEMPERATURE)) {
                updatedPclLabelIdMap.put(map.getKey(), map.getValue());
            }
            if (osLabel.contains(map.getKey())) {
                updatedPclLabelIdMap.put(map.getKey(), map.getValue());
            }
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclLabelTempMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(updatedPclLabelIdMap));
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(updatedPclLabelTrolleyMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
    }

    public void verifyCancelledStatusForInstacartOrder(HashMap<String, String> testOutputData) {
        try {
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            baseCommands.webpageRefresh();
            cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            cueHomePage.verifyOrderCancelled(testOutputData);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    @SafeVarargs
    public final void batchOrders(HashMap<String, String>... testOutputData) {
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData[0].get(ExcelUtils.STORE_ID)));
        List<String> orderNumbers = new ArrayList<>();
        for (HashMap<String, String> testDataMap : testOutputData) {
            orderNumbers.add(testDataMap.get(ExcelUtils.ORDER_NUMBER));
        }
        cueHomePage.batchOrdersInDifferentSlot(orderNumbers);
    }
}
