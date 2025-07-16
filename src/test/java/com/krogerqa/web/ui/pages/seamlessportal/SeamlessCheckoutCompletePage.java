package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessCheckoutCompleteMap;

import java.util.HashMap;

public class SeamlessCheckoutCompletePage {
    static String thankYouMsgPickupOrder = "Thank you, your Pickup order has been placed!";
    static String thankYouMsgDeliveryOrder = "Thank you, your Delivery order has been placed!";
    private static SeamlessCheckoutCompletePage instance;
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    SeamlessCheckoutCompleteMap checkOutCompleteMapKroger = SeamlessCheckoutCompleteMap.getInstance();

    private SeamlessCheckoutCompletePage() {
    }

    public synchronized static SeamlessCheckoutCompletePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessCheckoutCompletePage.class) {
            if (instance == null) {
                instance = new SeamlessCheckoutCompletePage();
            }
        }
        return instance;
    }

    public void assertOrderConfirmationText(HashMap<String, String> testOutputData) {
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
            baseCommands.assertElementText(checkOutCompleteMapKroger.orderPlacedMessage(), thankYouMsgPickupOrder, true);
        } else {
            baseCommands.assertElementText(checkOutCompleteMapKroger.orderPlacedMessage(), thankYouMsgDeliveryOrder, true);
        }
    }

    public String trimOrderNumber() {
        String orderNumber = baseCommands.getElementText(checkOutCompleteMapKroger.orderNumberText());
        return orderNumber.substring(7);
    }

    /**
     * @return order number
     */
    public String getOrderConfirmation(HashMap<String, String> testOutputData) {
        assertOrderConfirmationText(testOutputData);
        return trimOrderNumber();
    }

    public void selectOrderNumber(String url) {
        baseCommands.openNewUrl(url);
        baseCommands.click(checkOutCompleteMapKroger.orderNumberText());
    }

    public void acceptOrRejectBagCharges(HashMap<String, String> testOutputData, String orderNumber) {
        if ((testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(Constants.CheckoutComposite.NAME))) {
            baseCommands.openNewUrl(PropertyUtils.getByobUrl(orderNumber));
        } else if (testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(Constants.CheckoutComposite.MARIANOS)) {
            baseCommands.openNewUrl(PropertyUtils.getMarianosByobUrl(orderNumber));
        } else {
            baseCommands.openNewUrl(PropertyUtils.getKingSoopersByobUrl(orderNumber));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES_AFTER_PICKING))) {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(testOutputData.get(ExcelUtils.SCENARIO), ExcelUtils.SHEET_NAME_TEST_DATA);
            loginWeb.loginKrogerSeamlessPortal(testData.get(ExcelUtils.USER_EMAIL), testData.get(ExcelUtils.PASSWORD));
        }
        baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.byobPopUp());
        baseCommands.click(checkOutCompleteMapKroger.byobPopUp());
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CHANGE_BAG_PREF))) {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.changePrefButton());
            baseCommands.click(checkOutCompleteMapKroger.changePrefButton());
        }
        if (testOutputData.containsKey(ExcelUtils.REJECT_BAG_FEES) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.REJECT_BAG_FEES))) {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.bringYourOwnBagButton());
            baseCommands.click(checkOutCompleteMapKroger.bringYourOwnBagButton());
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.continueButton());
            baseCommands.click(checkOutCompleteMapKroger.continueButton());
        } else {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.acceptBagFeeButton());
            baseCommands.click(checkOutCompleteMapKroger.acceptBagFeeButton());
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.continueButton());
            baseCommands.click(checkOutCompleteMapKroger.continueButton());
            confirmBagFees();
        }
    }

    public void confirmBagFees() {
        baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.confirmBagFeePopup());
        baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.confirmBagCharges());
        baseCommands.click(checkOutCompleteMapKroger.confirmBagCharges());
        baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.confirmBagFeePopup());
    }

    public void reLoginKCP() {
        try {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.headerKCP());
            baseCommands.click(checkOutCompleteMapKroger.headerKCP());
        } catch (Exception | AssertionError e) {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.headerKCP());
            baseCommands.click(checkOutCompleteMapKroger.headerKCP());
        }
        baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.signOutButtonKCP());
        baseCommands.click(checkOutCompleteMapKroger.signOutButtonKCP());
        try {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.headerKCP());
            baseCommands.click(checkOutCompleteMapKroger.headerKCP());
        } catch (Exception | AssertionError e) {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.headerKCP());
            baseCommands.click(checkOutCompleteMapKroger.headerKCP());
        }
        try {
            baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.singInAgainButtonKCP());
            baseCommands.click(checkOutCompleteMapKroger.singInAgainButtonKCP());
        } catch (Exception | AssertionError e) {
            baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        }
    }
}
