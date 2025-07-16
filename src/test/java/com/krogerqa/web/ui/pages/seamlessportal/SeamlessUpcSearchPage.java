package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.web.ui.maps.seamlessportal.SeamlessUpcSearchMap;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.ExcelUtils;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

public class SeamlessUpcSearchPage {
    public static String DELIVERY_ONLY_ITEM = "Delivery Only";
    private static SeamlessUpcSearchPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessUpcSearchMap seamlessUpcSearchMap = SeamlessUpcSearchMap.getInstance();

    private SeamlessUpcSearchPage() {
    }

    public synchronized static SeamlessUpcSearchPage getInstance() {
        if (instance == null) {
            synchronized (SeamlessUpcSearchPage.class) {
                if (instance == null) {
                    instance = new SeamlessUpcSearchPage();
                }
            }
        }
        return instance;
    }

    public void clickSearchField() {
        baseCommands.click(seamlessUpcSearchMap.searchFieldDefaultState());
    }

    public void enterItemUpc(String itemUpc) {
        if (!baseCommands.getElementAttribute(seamlessUpcSearchMap.searchFieldOpenedState(), "value").isEmpty()) {
            clickClearButton();
        }
        baseCommands.enterText(seamlessUpcSearchMap.searchFieldOpenedState(), itemUpc, true);
        baseCommands.enterKeyOnElement(seamlessUpcSearchMap.searchFieldOpenedState());
    }

    public void clickAddToCartButton() {
        baseCommands.waitForElementVisibility(seamlessUpcSearchMap.addToCartButton());
        String text = baseCommands.getElementText(seamlessUpcSearchMap.addToCartButton());
        if (text.equals(DELIVERY_ONLY_ITEM)) {
            Assert.fail(DELIVERY_ONLY_ITEM);
        }
        baseCommands.click(seamlessUpcSearchMap.addToCartButton());
    }

    public void clickClearButton() {
        baseCommands.click(seamlessUpcSearchMap.clearSearchTextButton());
    }

    public void clickIncrementItemButton(int quantity) {
        for (int i = 1; i < quantity; i++) {
            baseCommands.waitForElementVisibility(seamlessUpcSearchMap.itemIncrementButton());
            baseCommands.click(seamlessUpcSearchMap.itemIncrementButton());
        }
    }

    public void addItemsToCart(List<HashMap<String, String>> itemsList) {
        for (HashMap<String, String> itemDetails : itemsList) {
            clickSearchField();
            String itemUpc = itemDetails.get(ExcelUtils.ITEM_UPC);
            int qty = Integer.parseInt(itemDetails.get(ExcelUtils.ITEM_QUANTITY));
            enterItemUpc(itemUpc);
            baseCommands.scrollToElement(seamlessUpcSearchMap.addToCartButton(), "down");
            clickAddToCartButton();
            if (qty > 1) {
                clickIncrementItemButton(qty);
            }
        }
    }

    public void reviewItems(List<HashMap<String,String>> itemsList) {
        addItemsToCart(itemsList);
        baseCommands.click(seamlessUpcSearchMap.reviewButton());
    }
}
