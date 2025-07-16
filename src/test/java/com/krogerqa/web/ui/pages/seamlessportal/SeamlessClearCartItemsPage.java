package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessClearCartItemsMap;

public class SeamlessClearCartItemsPage {
    private static SeamlessClearCartItemsPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessClearCartItemsMap seamlessClearCartItemsMap = SeamlessClearCartItemsMap.getInstance();

    private SeamlessClearCartItemsPage() {
    }

    public synchronized static SeamlessClearCartItemsPage getInstance() {
        if (instance == null) {
            synchronized (SeamlessClearCartItemsPage.class) {
                if (instance == null) {
                    instance = new SeamlessClearCartItemsPage();
                }
            }
        }
        return instance;
    }

    public void getCountOfItemsAndClearCart() {
        int itemsInCart;
        if (baseCommands.getElementText(seamlessClearCartItemsMap.cartDiv()).isEmpty()) {
            itemsInCart = 0;
        } else {
            itemsInCart = Integer.parseInt(baseCommands.getElementText(seamlessClearCartItemsMap.cartDiv()));
        }
        if (itemsInCart > 0) {
            baseCommands.click(seamlessClearCartItemsMap.expandCartButton());
        }
        for (int i = 0; i < itemsInCart; i++) {
            baseCommands.click(seamlessClearCartItemsMap.decrementButton());
        }
    }
}
