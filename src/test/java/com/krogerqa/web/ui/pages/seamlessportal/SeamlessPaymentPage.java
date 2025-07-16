package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.WebUtils;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessPaymentMap;

import java.util.HashMap;

public class SeamlessPaymentPage {
    private static SeamlessPaymentPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessPaymentMap seamlessPaymentMap = SeamlessPaymentMap.getInstance();
    WebUtils utilities = WebUtils.getInstance();

    private SeamlessPaymentPage() {
    }

    public synchronized static SeamlessPaymentPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessPaymentPage.class) {
            if (instance == null) {
                instance = new SeamlessPaymentPage();
            }
        }
        return instance;
    }

    public void enterContactInformation(String phoneNumber, String lastName) {
        baseCommands.waitForElementVisibility(seamlessPaymentMap.lastNameInput());
        baseCommands.enterText(seamlessPaymentMap.lastNameInput(), "ETE", true);
        baseCommands.waitForElementVisibility(seamlessPaymentMap.phoneNumberInput());
        baseCommands.enterText(seamlessPaymentMap.phoneNumberInput(), phoneNumber, true);
        baseCommands.waitForElementVisibility(seamlessPaymentMap.updateContactButton());
        baseCommands.click(seamlessPaymentMap.updateContactButton());
    }

    /*** @return order summary from seamless application checkout page*/
    public HashMap<String, String> selectPaymentAndSubmit(HashMap<String, String> testOutputData) {
        utilities.scrollToBottom();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT))) {
            if (baseCommands.getElementText(seamlessPaymentMap.existingPaymentSection()).contains("SNAP/EBT")) {
                baseCommands.click(seamlessPaymentMap.snapOrEbtRadioButton());
            } else {
                baseCommands.click(seamlessPaymentMap.creditDebitTextToggle());
                baseCommands.click(seamlessPaymentMap.snapOrEbtRadioButton());
            }
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SNAP_EBT))) {
            baseCommands.waitForElementVisibility(seamlessPaymentMap.continueWithCheckoutButton());
            baseCommands.click(seamlessPaymentMap.continueWithCheckoutButton());
            baseCommands.assertElementDisplayed(seamlessPaymentMap.snapEbtCheckBox(), true);
            baseCommands.assertElementDisplayed(seamlessPaymentMap.creditCardDetails(), true);
        } else {
            baseCommands.assertElementDisplayed(seamlessPaymentMap.creditCardDetails(), true);
        }
        return getOrderSummaryAndSubmit(testOutputData);
    }

    public HashMap<String, String> getOrderSummaryAndSubmit(HashMap<String, String> testOutputData) {
        String[] orderSummaryArray = baseCommands.getElementText(seamlessPaymentMap.orderSummarySection()).split("[\\r\\n]+");
        HashMap<String, String> orderSummaryMap = new HashMap<>();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT))) {
            orderSummaryMap.put(ExcelUtils.SNAP_EBT_TOTAL, baseCommands.getElementText(seamlessPaymentMap.snapEbtTotalText()));
            orderSummaryMap.put(ExcelUtils.NON_SNAP_EBT_TOTAL, baseCommands.getElementText(seamlessPaymentMap.nonSnapEbtTotalText()));
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SNAP_EBT))) {
            orderSummaryMap.put(ExcelUtils.ORDER_TOTAL, baseCommands.getElementText(seamlessPaymentMap.orderTotalText()));
        } else {
            orderSummaryMap.put(ExcelUtils.ORDER_TOTAL, baseCommands.getElementText(seamlessPaymentMap.orderTotalText()));
        }
        for (int i = 0; i < orderSummaryArray.length - 1; i += 2) {
            switch (orderSummaryArray[i]) {
                case "Estimated Subtotal":
                    orderSummaryMap.put(ExcelUtils.ESTIMATED_SUBTOTAL, orderSummaryArray[i + 1]);
                    break;
                case "Coupon Total":
                    orderSummaryMap.put(ExcelUtils.COUPON_TOTAL, orderSummaryArray[i + 1].substring(2));
                    break;
                case "Kroger Savings":
                    orderSummaryMap.put(ExcelUtils.KROGER_SAVINGS, orderSummaryArray[i + 1].substring(2));
                    break;
                case "Sales Tax":
                    orderSummaryMap.put(ExcelUtils.SALES_TAX, orderSummaryArray[i + 1]);
                    break;
                case "Fee":
                    orderSummaryMap.put(ExcelUtils.FEE, orderSummaryArray[i + 1]);
                    break;
            }
        }
        try {
            baseCommands.click(seamlessPaymentMap.submitButton());
        } catch (Exception | AssertionError e) {
            baseCommands.click(seamlessPaymentMap.submitButton());
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SNAP_EBT))) {
            baseCommands.switchToFrameByWebelement(seamlessPaymentMap.snapEbtPinFrame());
            baseCommands.waitForElementVisibility(seamlessPaymentMap.snapEbtPinNumberText());
            baseCommands.enterText(seamlessPaymentMap.snapEbtPinNumberText(), ExcelUtils.SNAP_EBT_PIN_NUMBER, true);
            baseCommands.switchToDefaultContent();
            baseCommands.click(seamlessPaymentMap.snapEbtDoneButton());
        }
        return orderSummaryMap;
    }

}
