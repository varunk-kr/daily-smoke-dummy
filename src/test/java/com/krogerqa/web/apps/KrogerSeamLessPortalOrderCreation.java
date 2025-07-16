package com.krogerqa.web.apps;

import com.krogerqa.api.CheckoutCompositeHelper;
import com.krogerqa.api.HarvesterServicesHelper;
import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.utils.WebUtils;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessCheckoutCompleteMap;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessStoreChangeMap;
import com.krogerqa.web.ui.pages.seamlessportal.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KrogerSeamLessPortalOrderCreation {

    public static final Logger LOGGER = LogManager.getLogger(KrogerSeamLessPortalOrderCreation.class);
    public static String orderNumber;
    public static String activateResponseVersionKey;
    public static Set<String> rombOrders = new HashSet<>();
    public static Set<String> normalOrders = new HashSet<>();
    private static KrogerSeamLessPortalOrderCreation instance;
    SeamlessStoreChangePage seamlessStoreChangePage = SeamlessStoreChangePage.getInstance();
    SeamlessClearCartItemsPage seamlessClearCartItemsPage = SeamlessClearCartItemsPage.getInstance();
    SeamlessUpcSearchPage seamlessUpcSearchPage = SeamlessUpcSearchPage.getInstance();
    SeamlessCartPage seamlessCartPage = SeamlessCartPage.getInstance();
    SeamlessSchedulingPage seamlessSchedulingPage = SeamlessSchedulingPage.getInstance();
    SeamlessPaymentPage seamlessPaymentPage = SeamlessPaymentPage.getInstance();
    SeamlessCheckoutCompletePage seamlessCheckoutCompletePage = SeamlessCheckoutCompletePage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    CheckoutCompositeHelper checkoutCompositeHelper = CheckoutCompositeHelper.getInstance();
    SeamlessDeliveryPage seamlessDeliveryPage = SeamlessDeliveryPage.getInstance();
    SeamlessStoreChangeMap seamlessStoreChangeMap = SeamlessStoreChangeMap.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    Response activateCheckoutResponse;
    SeamlessCheckoutCompleteMap checkOutCompleteMapKroger = SeamlessCheckoutCompleteMap.getInstance();
    WebUtils webUtils = WebUtils.getInstance();
    HarvesterServicesHelper harvesterServicesHelper = HarvesterServicesHelper.getInstance();

    private KrogerSeamLessPortalOrderCreation() {
    }

    public synchronized static KrogerSeamLessPortalOrderCreation getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (KrogerSeamLessPortalOrderCreation.class) {
            if (instance == null) {
                instance = new KrogerSeamLessPortalOrderCreation();
            }
        }
        return instance;
    }

    public HashMap<String, String> verifyNewOrderCreation(String scenarioName) {
        ExcelUtils.removeExistingDataFromExcel(scenarioName);
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenarioName, ExcelUtils.SHEET_NAME_TEST_DATA);
        try {
            loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
            if ((testOutputData.get(ExcelUtils.STORE_BANNER)).equalsIgnoreCase(ExcelUtils.BANNER_HARRIS_TEETER)) {
                baseCommands.waitForElementVisibility(seamlessStoreChangeMap.resendConfirmation());
                baseCommands.openNewUrl(PropertyUtils.getHarrisTeeterUrl());
            }
            testOutputData = verifyOrderInKSP(scenarioName, testOutputData);
            LOGGER.info("Order created through UI");
        } catch (Exception | AssertionError e) {
            LOGGER.info("Order created through API");
            testOutputData = createCompositeCheckoutPickOrder(scenarioName, testOutputData);
        }
        return testOutputData;
    }


    public HashMap<String, String> getStagingZones(HashMap<String, String> testOutputData) {
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
        testOutputData.put(ExcelUtils.GET_STAGING_ZONES, String.valueOf(stagingZonesBarcode));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        return testOutputData;
    }

    public HashMap<String, String> verifyOrderInKSP(String scenario, HashMap<String, String> testOutputData) {
        try {
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            /*Kroger seamless portal - non-EBT order placed*/
            verifyAddItemsToCart(testOutputData, itemData);
            verifyKspCheckout(testOutputData);
            verifyKspSubmitOrder(scenario, testOutputData);
            testOutputData = getStagingZones(testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BYOB_FLOW)) || Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                webUtils.captureScreenshot(Constants.ApplicationName.KSP, String.valueOf(e));
            } else {
                throw e;
            }
            return null;
        }
    }

    public void verifyAddItemsToCart(HashMap<String, String> testOutputData, List<HashMap<String, String>> itemData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT))) {
            baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrlEbt());
        }
        try {
            seamlessStoreChangePage.verifyStore(testOutputData, testOutputData.get(ExcelUtils.STORE_NAME), testOutputData.get(ExcelUtils.POSTAL_CODE), testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            seamlessStoreChangePage.verifyStore(testOutputData, testOutputData.get(ExcelUtils.STORE_NAME), testOutputData.get(ExcelUtils.POSTAL_CODE), testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        }
        try {
            seamlessClearCartItemsPage.getCountOfItemsAndClearCart();
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            seamlessClearCartItemsPage.getCountOfItemsAndClearCart();
        }
        seamlessUpcSearchPage.addItemsToCart(itemData);
    }

    public void verifyKspCheckout(HashMap<String, String> testData) {
        if (!Boolean.parseBoolean(testData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
            seamlessCartPage.proceedToPickupCheckout();
            seamlessSchedulingPage.selectPickupSlotEnterBirthDate(testData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR), Boolean.parseBoolean(testData.get(ExcelUtils.CIAO_AGE_CHECK)), testData);
        } else {
            seamlessCartPage.proceedToDeliveryCheckout();
            seamlessCartPage.fillAddress(testData);
            try {
                seamlessSchedulingPage.selectDeliveryTime(Constants.Shipt.SHIPT_DELIVERY, testData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR), Boolean.parseBoolean(testData.get(ExcelUtils.CIAO_AGE_CHECK)));
            } catch (Exception | AssertionError e) {
                seamlessCartPage.fillAddress(testData);
                seamlessSchedulingPage.selectDeliveryTime(Constants.Shipt.SHIPT_DELIVERY, testData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR), Boolean.parseBoolean(testData.get(ExcelUtils.CIAO_AGE_CHECK)));
            }
        }
    }

    public void verifyKspSubmitOrder(String scenario, HashMap<String, String> testOutputData) {
        boolean batchOrder = Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE));
        seamlessPaymentPage.enterContactInformation(testOutputData.get(ExcelUtils.PHONE_NUMBER), testOutputData.get(ExcelUtils.LAST_NAME));
        if (batchOrder && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING))) && (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER))) {
            pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
            seamlessDeliveryPage.clickAcceptTerms();
        }
        HashMap<String, String> orderSummaryMap = seamlessPaymentPage.selectPaymentAndSubmit(testOutputData);
        testOutputData.putAll(orderSummaryMap);
        testOutputData.putIfAbsent(ExcelUtils.COUPON_TOTAL, "0");
        testOutputData.putIfAbsent(ExcelUtils.KROGER_SAVINGS, "0");
        String orderNumber = seamlessCheckoutCompletePage.getOrderConfirmation(testOutputData);
        testOutputData.put(ExcelUtils.ORDER_NUMBER, orderNumber);
        String orderConfirmationUrl = baseCommands.getUrl();
        testOutputData.put(ExcelUtils.ORDER_CONFIRMATION_URL, orderConfirmationUrl);
        ExcelUtils.writeToExcel(scenario, testOutputData);
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BYOB_FLOW))) {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.orderNumberText());
            baseCommands.click(checkOutCompleteMapKroger.orderNumberText());
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.purchaseDetails());
            verifyBagPreference(testOutputData, orderNumber);
        }
        /*Batching through API for multi-batching store*/
        if (batchOrder && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING))) && (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.LAST_ORDER))) {
            pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
        }
    }

    public void verifyCancelOrder(HashMap<String, String> testOutputData) {
        try {
            SeamlessMyPurchasesPage seamlessMyPurchasesPage = SeamlessMyPurchasesPage.getInstance();
            baseCommands.openNewUrl(PropertyUtils.getKrogerPurchaseDetailsUrl() + testOutputData.get(ExcelUtils.ORDER_NUMBER));
            seamlessMyPurchasesPage.cancelOrder();
        } catch (Exception e) {
            checkoutCompositeCancelOrder(testOutputData);
        }
    }

    public HashMap<String, String> createCompositeCheckoutPickOrder(String scenario, HashMap<String, String> testOutputData) {
        try {
            testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            if ((!testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)))) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Integer.parseInt(testOutputData.get(ExcelUtils.DAYS_FROM_TODAY)));
            }
            Response response = checkoutCompositeHelper.createCheckoutCompositePickOrder(Integer.parseInt(testOutputData.get(ExcelUtils.DAYS_FROM_TODAY)), testOutputData, itemData);
            baseCommands.wait(6);
            orderNumber = response.jsonPath().getString("data.checkouts.id");
            String versionKey = response.jsonPath().getString("data.checkouts.versionKey");
            String nonEbtTotal = response.jsonPath().getString("data.checkouts.summary.cost.nonEbtTotal");
            String ebtTotal = response.jsonPath().getString("data.checkouts.summary.cost.ebtTotal");
            testOutputData.put(ExcelUtils.ORDER_NUMBER, orderNumber);
            testOutputData.put(ExcelUtils.VERSION_KEY, versionKey);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT))) {
                activateCheckoutResponse = checkoutCompositeHelper.activateCheckoutCompositeEbtOrder(testOutputData, orderNumber, versionKey);
            } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SNAP_EBT))) {
                activateCheckoutResponse = checkoutCompositeHelper.activateCheckoutCompositeSnapEbtOrder(testOutputData, orderNumber, versionKey, nonEbtTotal, ebtTotal);
            } else {
                activateCheckoutResponse = checkoutCompositeHelper.activateCheckoutCompositeNonEbtOrder(testOutputData, itemData, orderNumber, versionKey);
            }
            activateResponseVersionKey = activateCheckoutResponse.jsonPath().getString("data.checkouts.versionKey");
            testOutputData.put(ExcelUtils.ACTIVATE_RESPONSE_VERSION_KEY, activateResponseVersionKey);
            if ((!testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER)) && (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.LAST_ORDER)))) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Integer.parseInt(testOutputData.get(ExcelUtils.DAYS_FROM_TODAY)));
            }
            testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ROMB)))
                rombOrders.add(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            else {
                normalOrders.add(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
            testOutputData = getStagingZones(testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.KSP, String.valueOf(e));
            return null;
        }
    }

    public void checkoutCompositeCancelOrder(HashMap<String, String> testOutputData) {
        String versionKey = testOutputData.get(ExcelUtils.ACTIVATE_RESPONSE_VERSION_KEY);
        String orderNumber = testOutputData.get(ExcelUtils.ORDER_NUMBER);
        checkoutCompositeHelper.cancelCheckoutCompositeOrder(orderNumber, versionKey, testOutputData);
    }

    public void checkoutCompositeModifyOrder(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, ExcelUtils.SHEET_NAME_TEST_DATA);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            testOutputData.put(ExcelUtils.ORDER_NUMBER, orderNumber);
            baseCommands.wait(3);
            checkoutCompositeHelper.modifyCheckoutCompositeOrder(testData, itemData, orderNumber, activateResponseVersionKey);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DYNAMIC_BATCHING))) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyNewDynamicBatchingOrderCreation(String scenarioName) {
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenarioName, ExcelUtils.SHEET_NAME_TEST_DATA);
        try {
            if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                if (testOutputData.get(ExcelUtils.STORE_BANNER).equals(ExcelUtils.KROGER_FEDERATION)) {
                    baseCommands.openNewUrl(PropertyUtils.getKrogerFederationSeamlessUrl());
                    loginWeb.federationLoginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
                } else {
                    loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
                }
            }
            testOutputData = verifyOrderInKSP(scenarioName, testOutputData);
        } catch (Exception e) {
            testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
            testOutputData = createCompositeCheckoutPickOrder(scenarioName, testOutputData);
        }
        return testOutputData;
    }

    public void verifyBagPreference(HashMap<String, String> testOutputData, String orderNumber) {
        try {
            seamlessCheckoutCompletePage.acceptOrRejectBagCharges(testOutputData, orderNumber);
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.KSP, String.valueOf(e));
        }
    }

    public void logoutAndLoginKCP() {
        try {
            seamlessCheckoutCompletePage.reLoginKCP();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.KSP, String.valueOf(e));
        }
    }
}
