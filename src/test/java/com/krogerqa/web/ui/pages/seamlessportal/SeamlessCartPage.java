package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessAddAddressMap;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessCartMap;

import java.util.HashMap;

public class SeamlessCartPage {
    private static SeamlessCartPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessCartMap seamlessCartMap = SeamlessCartMap.getInstance();
    SeamlessAddAddressMap seamlessAddAddressMap = SeamlessAddAddressMap.getInstance();

    private SeamlessCartPage() {
    }

    public synchronized static SeamlessCartPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessCartPage.class) {
            if (instance == null) {
                instance = new SeamlessCartPage();
            }
        }
        return instance;
    }

    public void clickCartButton() {
        baseCommands.click(seamlessCartMap.cartButton());
    }

    private void addOrEditAddress() {
        if (baseCommands.elementDisplayed(seamlessCartMap.addEditAddress()))
            baseCommands.click(seamlessCartMap.addEditAddress());
    }

    public void clickCheckoutPickupButton() {
        baseCommands.scrollToElement(seamlessCartMap.cartCheckoutPickupButton(), "down");
        baseCommands.click(seamlessCartMap.cartCheckoutPickupButton());
        if (baseCommands.getUrl().contains("cart")) {
            baseCommands.click(seamlessCartMap.cartCheckoutPickupButton());
        }
    }

    public void proceedToPickupCheckout() {
        clickCartButton();
        try {
            clickCheckoutPickupButton();
        } catch (Exception | AssertionError e) {
            clickCheckoutPickupButton();
        }
    }

    public void proceedToDeliveryCheckout() {
        proceedToAddAddress();
    }

    public void clickCheckoutDeliveryButton() {
        baseCommands.webpageRefresh();
        baseCommands.waitForElementVisibility(seamlessCartMap.cartCheckoutDeliveryButton());
        baseCommands.waitForElementClickability(seamlessCartMap.cartCheckoutDeliveryButton());
        baseCommands.assertElementEnabled(seamlessCartMap.cartCheckoutDeliveryButton(), true);
        if (baseCommands.getUrl().contains("cart")) {
            baseCommands.waitForElementClickability(seamlessCartMap.cartCheckoutDeliveryButton());
            baseCommands.click(seamlessCartMap.cartCheckoutDeliveryButton());
        }
    }

    private void proceedToAddAddress() {
        clickCartButton();
        addOrEditAddress();
    }

    public void enterPostalZipCode(String zipCode) {
        baseCommands.enterText(seamlessAddAddressMap.postalZipCodeInput(), zipCode, true);
    }

    public void enterAddress(String addressLine) {
        baseCommands.enterText(seamlessAddAddressMap.addressInput(), addressLine, true);
    }

    public void enterCity(String city) {
        baseCommands.enterText(seamlessAddAddressMap.cityInput(), city, true);
    }

    public void enterState(String state) {
        baseCommands.selectFromDroplist(seamlessAddAddressMap.stateDropList(), state);
    }

    public void saveAndContinue() {
        baseCommands.click(seamlessAddAddressMap.saveAndContinue());
    }

    public void confirmAddress() {
        if (baseCommands.elementDisplayed(seamlessAddAddressMap.confirmAddressPopup())) {
            baseCommands.waitForElementClickability(seamlessAddAddressMap.confirmAddressPopup());
            baseCommands.click(seamlessAddAddressMap.confirmAddressPopup());
        }
    }

    public void fillAddress(HashMap<String, String> testData) {
        if (baseCommands.elementDisplayed(seamlessAddAddressMap.addressInput())) {
            enterAddress(testData.get(ExcelUtils.ADDRESS_LINE1));
            enterCity(testData.get(ExcelUtils.CITY_TOWN));
            enterState(testData.get(ExcelUtils.STATE_PROVINCE));
            enterPostalZipCode(testData.get(ExcelUtils.POSTAL_CODE));
            try {
                saveAndContinue();
            } catch (Exception | AssertionError e) {
                saveAndContinue();
            }
            confirmAddress();
        }
        clickCheckoutDeliveryButton();
    }
}
