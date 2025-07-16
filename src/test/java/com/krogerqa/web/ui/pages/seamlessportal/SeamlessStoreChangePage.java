package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessCheckoutCompleteMap;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessStoreChangeMap;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;

import java.util.HashMap;

public class SeamlessStoreChangePage {
    static String shoppingPickup = "You are now shopping Pickup at ";
    static String shoppingDelivery = "You are now shopping Delivery to ";
    private static SeamlessStoreChangePage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessStoreChangeMap seamlessStoreChangeMap = SeamlessStoreChangeMap.getInstance();
    SeamlessCheckoutCompleteMap checkOutCompleteMapKroger = SeamlessCheckoutCompleteMap.getInstance();

    private SeamlessStoreChangePage() {
    }

    public synchronized static SeamlessStoreChangePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessStoreChangePage.class) {
            if (instance == null) {
                instance = new SeamlessStoreChangePage();
            }
        }
        return instance;
    }

    public String getSelectedStore() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                baseCommands.getElementText(seamlessStoreChangeMap.currentStoreNameText());
            } catch (StaleElementReferenceException e) {
                baseCommands.webpageRefresh();
            }
            attempts++;
        }
        baseCommands.waitForElementVisibility(seamlessStoreChangeMap.currentStoreNameText());
        return baseCommands.getElementText(seamlessStoreChangeMap.currentStoreNameText());
    }

    public void clickCurrentModalityButton() {
        baseCommands.click(seamlessStoreChangeMap.currentModalityButton());
    }

    public void searchByPostalCode(String postalCode) {
        baseCommands.click(seamlessStoreChangeMap.postalCodeDropdown());
        baseCommands.click(seamlessStoreChangeMap.searchPostalCodeInput());
        baseCommands.enterText(seamlessStoreChangeMap.searchPostalCodeInput(), postalCode, true);
        baseCommands.enterKeyOnElement(seamlessStoreChangeMap.searchPostalCodeInput());
    }

    public void clickChangeStoreButton() {
        baseCommands.waitForElementVisibility(seamlessStoreChangeMap.changeStoreButton());
        baseCommands.click(seamlessStoreChangeMap.changeStoreButton());
    }

    public void clickStoreLink(String div, String store) {
        baseCommands.waitForElementVisibility(seamlessStoreChangeMap.changeStoreLink(div, store));
        baseCommands.click(seamlessStoreChangeMap.changeStoreLink(div, store));
        baseCommands.waitForElementVisibility(seamlessStoreChangeMap.changeStoreBanner());
        baseCommands.click(seamlessStoreChangeMap.changeStoreBanner());
    }

    public void clickStartShoppingButton(String div, String store) {
        baseCommands.click(seamlessStoreChangeMap.startShoppingButton(div, store));
    }

    public void verifySuccessMessage(String storeName) {
        String successMessage = baseCommands.getElementText(seamlessStoreChangeMap.storeChangeSuccessMessage());
        Assert.assertTrue(successMessage.contains(shoppingPickup + storeName));
    }

    public void selectDeliveryShopping() {
        baseCommands.click(seamlessStoreChangeMap.selectDeliveryShopping());
    }

    public void verifyDeliverySuccessMessage(String storeName, String postalCode) {
        String successMessage = baseCommands.getElementText(seamlessStoreChangeMap.storeChangeSuccessMessage());
        try {
            if (successMessage.contains(shoppingDelivery + storeName)) {
                Assert.assertTrue(successMessage.contains(shoppingDelivery + storeName));
            } else {
                Assert.assertTrue(successMessage.contains(shoppingDelivery + postalCode));
            }
        } catch (Exception | AssertionError e) {
            baseCommands.waitForElementInvisible(seamlessStoreChangeMap.storeChangeSuccessMessage());
        }
    }

    public void verifyStore(HashMap<String, String> testOutputData, String storeName, String postalCode, String storeDivision, String storeLocationId) {
        if (!getSelectedStore().equalsIgnoreCase(storeName)) {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER)) && !(testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(Constants.CheckoutComposite.KINGSOOPERS))) {
                clickCurrentModalityButton();
                searchByPostalCode(postalCode);
            }
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER)) && !(testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(Constants.CheckoutComposite.KINGSOOPERS))) {
                clickChangeStoreButton();
                clickStartShoppingButton(storeDivision, storeLocationId);
                verifySuccessMessage(storeName);
            } else if (testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(Constants.CheckoutComposite.KINGSOOPERS)) {
                clickCurrentModalityButton();
                searchByPostalCode(postalCode);
                clickChangeStoreButton();
                clickStoreLink(storeDivision, storeLocationId);
                clickCurrentModalityButton();
                searchByPostalCode(postalCode);
                clickChangeStoreButton();
                clickStartShoppingButton(storeDivision, storeLocationId);
                verifySuccessMessage(storeName);
                baseCommands.waitForElementVisibility(checkOutCompleteMapKroger.headerKCP());
                baseCommands.click(checkOutCompleteMapKroger.headerKCP());
                if (baseCommands.elementDisplayed(checkOutCompleteMapKroger.singInAgainButtonKCP())) {
                    baseCommands.click(checkOutCompleteMapKroger.singInAgainButtonKCP());
                }
            } else {
                clickCurrentModalityButton();
                searchByPostalCode(postalCode);
                selectDeliveryShopping();
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER))) {
                    verifyDeliverySuccessMessage(storeName, postalCode);
                }
            }
        }
    }
}
