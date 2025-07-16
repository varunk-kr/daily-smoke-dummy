package com.krogerqa.web.ui.pages.cue;

import com.krogerqa.api.CheckoutCompositeHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.*;
import com.krogerqa.web.ui.maps.cue.CueHomeMap;
import com.krogerqa.web.ui.maps.cue.CueOrderDetailsMap;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CueHomePage {
    public static final String CUE_ORDER_PICKED_UP = "Picked Up";
    public static final String CUE_PAYMENT_PAID = "Paid";
    public static final String CUE_ORDER_CANCELED = "Canceled";
    public static final String CUE_ORDER_DETAILS_CANCELLED = "Cancelled";
    CheckoutCompositeHelper checkoutCompositeHelper = CheckoutCompositeHelper.getInstance();
    public static final String CUE_SOURCE_HOME_STORE = "HomeStore";
    private static final String CUE_SOURCE_DELIVERY = "HomeDelivery";
    private static final String CUE_SOURCE_INSTACART = "Instacart";
    private static final String CUE_PAYMENT_NON_EBT = "Approved";
    private static final String CUE_PAYMENT_EBT = "EBT";
    private static final String CUE_ORDER_NEW = "New Order";
    private static final String CUE_ORDER_PICKED = "Picked";
    private static final String CUE_ORDER_STAGED = "Staged";
    private static final String CUE_ORDER_STAGING_COMPLETION = "100%";
    private static final String CUE_ORDER_OOS = "OOS";
    private static final String CUE_ORDER_PICKING = "Picking";
    private static final String CUE_ORDER_STAGING = "Staging";
    private static final String CUE_RUSH_ORDER_DESCRIPTION = "New rush order. Customer will be arriving soon, please batch, print and select accordingly!";
    private static final String express = "Express";
    private static final String CUE_EXPRESS_ORDER_DESCRIPTION = "New Express order. Customer will arrive approximately within the next hour, please ensure the order is batched and selected!";
    static int rushOrderBeforeOrderCreation;
    static int expressOrderBeforeOrderCreation;
    static int expressOrderAfterOrderCreation;
    static String readyForDelivery = "Ready For Delivery";
    static String ORDER_DELIVERED = "Delivered";
    private static CueHomePage instance;
    BaseCommands baseCommands = new BaseCommands();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();
    CueHomeMap cueHomeMap = CueHomeMap.getInstance();
    CueOrderDetailsMap cueOrderDetailsMap = CueOrderDetailsMap.getInstance();
    WebUtils webUtils = WebUtils.getInstance();

    private CueHomePage() {
    }

    public synchronized static CueHomePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueHomePage.class) {
            if (instance == null) {
                instance = new CueHomePage();
            }
        }
        return instance;
    }

    public void searchOrderId(String orderId) {
        try {
            baseCommands.waitForElementVisibility(cueHomeMap.searchOrderInputField());
            baseCommands.element(cueHomeMap.searchOrderInputField()).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            baseCommands.enterText(cueHomeMap.searchOrderInputField(), orderId, true);
            baseCommands.waitForElementVisibility(cueHomeMap.orderRow());
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            baseCommands.waitForElementVisibility(cueHomeMap.searchOrderInputField());
            baseCommands.enterText(cueHomeMap.searchOrderInputField(), orderId, true);
        }
    }

    public void cancelOrder() {
        baseCommands.waitForElementVisibility(cueHomeMap.kebabMenu());
        baseCommands.click(cueHomeMap.kebabMenu());
        baseCommands.click(cueHomeMap.cancelOrderOption());
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String oos = baseCommands.getElementText(cueHomeMap.orderStatusText());
        if (oos.equals("OOS")) {
            baseCommands.click(cueHomeMap.selectCancelOptionAsOos());
        } else {
            baseCommands.click(cueHomeMap.selectCancelOption());
        }
        baseCommands.click(cueHomeMap.cancelButton());
    }

    public void verifyNewOrderStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String newStatus = baseCommands.getElementText(cueHomeMap.orderStatusText());
        Assert.assertEquals(newStatus, CUE_ORDER_NEW);
    }

    public int getNumberOfTrolleys() {
        baseCommands.waitForElementClickability(cueHomeMap.getTrolleyText());
        baseCommands.waitForElementVisibility(cueHomeMap.getTrolleyText());
        String text = baseCommands.getElementText(cueHomeMap.getTrolleyText());
        return Integer.parseInt(text.split(" ")[0]);
    }

    public String verifyAndReturnOrderSource(HashMap<String, String> testOutputData) {
        baseCommands.waitForElementVisibility(cueHomeMap.homePageOrderSourceText());
        String orderSource = baseCommands.getElementText(cueHomeMap.homePageOrderSourceText());
        int i = 0;
        while (orderSource == null || orderSource.isEmpty()) {
            i++;
            baseCommands.webpageRefresh();
            searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            baseCommands.waitForElementVisibility(cueHomeMap.homePageOrderSourceText());
            orderSource = baseCommands.getElementText(cueHomeMap.homePageOrderSourceText());
            if (i > 15) {
                break;
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
            Assert.assertEquals(orderSource, CueHomePage.CUE_SOURCE_DELIVERY);
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
            Assert.assertEquals(orderSource, CueHomePage.CUE_SOURCE_INSTACART);
        } else {
            Assert.assertEquals(orderSource, CUE_SOURCE_HOME_STORE);
        }
        return orderSource;
    }

    public String getCustomerName() {
        baseCommands.waitForElement(cueHomeMap.customerNameText());
        return baseCommands.getElementText(cueHomeMap.customerNameText());
    }

    public void verifyPaymentStatus(String paymentStatus) {
        String payment = baseCommands.getElementText(cueHomeMap.paymentStatusText());
        Assert.assertEquals(payment, paymentStatus);
    }

    public void verifyForNewOrderStatus(boolean isEbt, HashMap<String, String> testOutputData) {
        boolean isMultiOrderLastData = testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) && testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.LAST_ORDER);
        if (!isMultiOrderLastData) {
            try {
                verifyNewOrderStatus();
            } catch (Exception e) {
                baseCommands.webpageRefresh();
                searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                verifyNewOrderStatus();
            }
        }
        verifyAndReturnOrderSource(testOutputData);
        if (isEbt) {
            verifyPaymentStatus(CUE_PAYMENT_EBT);
        } else {
            verifyPaymentStatus(CUE_PAYMENT_NON_EBT);
        }
    }

    public void verifyStageCompletionStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String stageStatus = baseCommands.getElementText(cueHomeMap.orderStatusText());
        Assert.assertEquals(stageStatus, CUE_ORDER_STAGED);
        baseCommands.assertElementText(cueHomeMap.stagePercentText(), CUE_ORDER_STAGING_COMPLETION, true);
    }

    public void verifyOrderPickedStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String orderStatus = baseCommands.getElementText(cueHomeMap.orderStatusText());
        Assert.assertEquals(orderStatus, CUE_ORDER_PICKED);
    }

    public void customerCheckIn(String spot) {
        baseCommands.waitForElementVisibility(cueHomeMap.kebabMenu());
        baseCommands.waitForElementClickability(cueHomeMap.kebabMenu());
        baseCommands.click(cueHomeMap.kebabMenu());
        baseCommands.waitForElement(cueHomeMap.customerCheckInOption());
        baseCommands.click(cueHomeMap.customerCheckInOption());
        baseCommands.enterText(cueHomeMap.parkingSpotInput(), spot, true);
        baseCommands.click(cueHomeMap.vehicleTypeButton());
        baseCommands.click(cueHomeMap.vehicleColor());
        baseCommands.waitForElement(cueHomeMap.checkInButton());
        baseCommands.click(cueHomeMap.checkInButton());
        baseCommands.waitForElementVisibility(cueHomeMap.closeButton());
        try {
            baseCommands.click(cueHomeMap.closeButton());
        } catch (Exception e) {
            baseCommands.click(cueHomeMap.closeButton());
        }
    }

    public void verifyPickedUpPaidStatus(HashMap<String, String> testOutputData) {
        int retry = 8, i = 0;
        while (i < retry) {
            try {
                baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
                baseCommands.waitForElementVisibility(cueHomeMap.paymentStatusText());
                baseCommands.assertElementText(cueHomeMap.orderStatusText(), CUE_ORDER_PICKED_UP, true);
                baseCommands.assertElementText(cueHomeMap.paymentStatusText(), CUE_PAYMENT_PAID, true);
                break;
            } catch (Exception | AssertionError e) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                    baseCommands.webpageRefresh();
                    searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
                } else {
                    checkoutCompositeHelper.putFinalizeCheckout(testOutputData.get(ExcelUtils.ORDER_NUMBER), testOutputData);
                    baseCommands.webpageRefresh();
                    searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                }
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER))) {
                    expandCarryoverOrdersSection();
                }
                i++;
            }
        }
    }

    public void verifyCanceledStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        baseCommands.assertElementText(cueHomeMap.orderStatusText(), CUE_ORDER_CANCELED, true);
        baseCommands.assertElementText(cueHomeMap.paymentStatusText(), CUE_ORDER_CANCELED, true);
    }

    public void verifyOOSStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        baseCommands.assertElementText(cueHomeMap.orderStatusText(), CUE_ORDER_OOS, true);
        baseCommands.assertElementText(cueHomeMap.paymentStatusText(), CUE_PAYMENT_NON_EBT, true);
    }

    public void expandCarryoverOrdersSection() {
        baseCommands.click(cueHomeMap.carryoverOrdersSection());
    }

    public void selectDate(int daysFromToday) {
        baseCommands.click(cueHomeMap.datePicker());
        baseCommands.element(cueHomeMap.datePicker()).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, DateUtils.getRequiredDateString(Constants.Date.CUE_DATE_FORMAT, daysFromToday), Keys.ENTER);
    }

    public void verifyOrderPickingStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String orderStatus = baseCommands.getElementText(cueHomeMap.orderStatusText());
        Assert.assertEquals(orderStatus, CUE_ORDER_PICKING);
    }

    public void verifyReadyForDelivery(HashMap<String, String> orderData) {
        for (int i = 0; i < 10; i++) {
            try {
                baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
                baseCommands.assertElementText(cueHomeMap.orderStatusText(), readyForDelivery, true);
                break;
            } catch (Exception | AssertionError e) {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(orderData.get(ExcelUtils.STORE_ID)));
                searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
            }
        }
    }

    public void verifyDeliveredStatus(HashMap<String, String> orderData) {
        for (int i = 0; i < 10; i++) {
            try {
                baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
                baseCommands.assertElementText(cueHomeMap.orderStatusText(), ORDER_DELIVERED, true);
                break;
            } catch (Exception | AssertionError e) {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(orderData.get(ExcelUtils.STORE_ID)));
                searchOrderId(orderData.get(ExcelUtils.ORDER_NUMBER));
            }
        }
    }

    public void verifyStagingStatus() {
        baseCommands.waitForElementVisibility(cueHomeMap.orderStatusText());
        String stageStatus = baseCommands.getElementText(cueHomeMap.orderStatusText());
        Assert.assertEquals(stageStatus, CUE_ORDER_STAGING);
    }

    public void verifyRushOrderLabel() {
        baseCommands.waitForElementVisibility(cueHomeMap.rushOrderLabelWithLightningIcon());
        baseCommands.click(cueHomeMap.rushOrderLabelWithLightningIcon());
        baseCommands.waitForElementVisibility(cueHomeMap.rushOrderDescriptionInAlerts());
        String orderLabel = baseCommands.getElementText(cueHomeMap.rushOrderDescriptionInAlerts());
        if (orderLabel.contains(express)) {
            Assert.assertEquals(orderLabel, CUE_EXPRESS_ORDER_DESCRIPTION);
        } else {
            Assert.assertEquals(orderLabel, CUE_RUSH_ORDER_DESCRIPTION);
        }
        baseCommands.click(cueHomeMap.okButtonPopup());
    }

    public void verifyExpressOrderLabel() {
        baseCommands.waitForElementVisibility(cueHomeMap.rushOrderLabelWithLightningIcon());
        baseCommands.click(cueHomeMap.rushOrderLabelWithLightningIcon());
        baseCommands.waitForElementVisibility(cueHomeMap.expressOrderDescriptionInAlerts());
        String orderLabel = baseCommands.getElementText(cueHomeMap.expressOrderDescriptionInAlerts());
        Assert.assertEquals(orderLabel, CUE_EXPRESS_ORDER_DESCRIPTION);
        baseCommands.click(cueHomeMap.okButtonPopup());
    }

    public void verifyExpressOrderInitialCount() {
        try {
            baseCommands.waitForElement(cueHomeMap.expressOrderCount());
            expressOrderBeforeOrderCreation = Integer.parseInt(baseCommands.getElementText(cueHomeMap.expressOrderCount()));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyRushOrderInitialCount() {
        try {
            baseCommands.waitForElement(cueHomeMap.rushOrderCount());
            rushOrderBeforeOrderCreation = Integer.parseInt(baseCommands.getElementText(cueHomeMap.rushOrderCount()));
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void verifyExpressOrderNewCount() {
        baseCommands.waitForElement(cueHomeMap.expressOrderCount());
        expressOrderAfterOrderCreation = Integer.parseInt(baseCommands.getElementText(cueHomeMap.expressOrderCount()));
    }

    public void verifyExpressOrderCountIncreased() {
        Assert.assertTrue(expressOrderAfterOrderCreation > expressOrderBeforeOrderCreation);
    }

    public void verifyRushOrderLabelRemoved() {
        baseCommands.waitForElement(cueHomeMap.rushOrderLabelWithLightningIcon());
        baseCommands.click(cueHomeMap.rushOrderLabelWithLightningIcon());
        Assert.assertFalse(baseCommands.getElementText(cueHomeMap.alertText()).contains(CUE_RUSH_ORDER_DESCRIPTION));
        baseCommands.click(cueHomeMap.okButtonPopup());
    }

    public void multipleOrderBatching(Set<String> orderNums) {
        for (String orderId : orderNums) {
            while (!isRushOrderCheckBoxVisible(orderId)) {
                baseCommands.webpageRefresh();
            }
        }
        for (String orderId : orderNums) {
            baseCommands.waitForElementVisibility(cueHomeMap.rushOrderCheckBox(orderId));
            baseCommands.click(cueHomeMap.rushOrderCheckBox(orderId));
        }
        baseCommands.waitForElement(cueOrderDetailsMap.manualBatchingButton());
        baseCommands.click(cueOrderDetailsMap.manualBatchingButton());
        baseCommands.click(cueOrderDetailsMap.confirmManualBatching());
    }

    public boolean isRushOrderCheckBoxVisible(String orderId) {
        return baseCommands.elementDisplayed(cueHomeMap.rushOrderCheckBox(orderId));
    }

    public void multipleRushAndNormalOrderBatching(Set<String> orderNums, Set<String> normalOrder) {
        try {
            baseCommands.wait(60);
            baseCommands.webpageRefresh();
            for (String orderId : orderNums) {
                baseCommands.click(cueHomeMap.rushOrderCheckBox(orderId));
            }
            for (String nonRushOrder : normalOrder) {
                searchOrderId(nonRushOrder);
                cueOrderDetailsPage.selectCheckBox();
                closeCircle();
            }
            cueOrderDetailsPage.performManualBatchingForRushOrderAndNormalOrder();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void batchOrdersInDifferentSlot(List<String> orderNumbers) {
        try {
            baseCommands.webpageRefresh();
            for (String order : orderNumbers) {
                searchOrderId(order);
                cueOrderDetailsPage.selectCheckBox();
            }
            cueOrderDetailsPage.clickManualBatchingButton();
            cueOrderDetailsPage.confirmManualBatching();
            cueOrderDetailsPage.waitForMessageInvisibility();
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void closeCircle() {
        baseCommands.waitForElement(cueHomeMap.closeCircle());
        baseCommands.click(cueHomeMap.closeCircle());
    }

    public void performSingleRushOrderBatching(String visualOrderId) {
        if (baseCommands.elementDisplayed(cueHomeMap.rushOrderCheckBox())) {
            selectSingleRushOrderCheckbox();
            clickBatchRushOrderButton();
            verifyRushOrderIdPopup(visualOrderId);
            clickBatchRushOrderPopupButton();
        }
    }

    private void verifyRushOrderIdPopup(String visualOrderId) {
        baseCommands.waitForElement(cueHomeMap.rushOrderPopupText());
        Assert.assertEquals(baseCommands.getElementText(cueHomeMap.rushOrderPopupText()).trim(), visualOrderId);
    }

    private void clickBatchRushOrderPopupButton() {
        baseCommands.waitForElement(cueHomeMap.clickBatchOrderButtonPopup());
        baseCommands.click(cueHomeMap.clickBatchOrderButtonPopup());
    }

    private void clickBatchRushOrderButton() {
        baseCommands.waitForElement(cueHomeMap.clickBatchOrderButton());
        baseCommands.click(cueHomeMap.clickBatchOrderButton());
    }

    private void selectSingleRushOrderCheckbox() {
        baseCommands.waitForElementVisibility(cueHomeMap.rushOrderCheckBox());
        baseCommands.click(cueHomeMap.rushOrderCheckBox());
    }

    public boolean isOrderVisible() {
        return baseCommands.elementDisplayed(cueOrderDetailsMap.visualOrderIdText());
    }

    public boolean isRushOrderCheckBoxVisible() {
        return baseCommands.elementDisplayed(cueHomeMap.rushOrderCheckBox());
    }

    public boolean isRombCheckboxEnabled() {
        return baseCommands.elementExists(cueHomeMap.isRombCheckboxEnabled());
    }

    public String getTotalItemCountInCue() {
        baseCommands.waitForElement(cueHomeMap.totalItemCount());
        return baseCommands.getElementText(cueHomeMap.totalItemCount()).split("/")[1];
    }

    public void verifyOrderCancelled(HashMap<String, String> testOutputData) {
        try {
            verifyCanceledStatus();
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                searchOrderId(testOutputData.get(ExcelUtils.INSTACART_VISUAL_ORDER_ID));
            } else {
                searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
                testOutputData.put(ExcelUtils.VISUAL_ORDER_ID, baseCommands.getElementText(cueOrderDetailsMap.visualOrderIdText()));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EXPRESS_ORDER))) {
                String pickUpTime = testOutputData.get(ExcelUtils.PICKUP_TIME);
                int time = Integer.parseInt(pickUpTime.split(":")[0]);
                if (time <= 5) {
                    selectDate(Constants.Date.PREVIOUS_DAY);
                }
            }
            verifyCanceledStatus();
        }
    }

    public List<WebElement> itemList(String order) {
        int retry = 10;
        while (retry > 0) {
            try {
                Assert.assertTrue(baseCommands.elementDisplayed(cueHomeMap.serviceCounterItemRowList(order)), Constants.INCORRECT_SC_ITEMS_DISPLAY);
                break;
            } catch (Exception | AssertionError e) {
                retry--;
                baseCommands.webpageRefresh();
                searchOrderId(order);
            }
        }
        baseCommands.elementDisplayed(cueHomeMap.serviceCounterItemRowList(order));
        return baseCommands.elements(cueHomeMap.serviceCounterItemRowList(order));
    }
}
