package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessClearCartItemsMap {
    private static SeamlessClearCartItemsMap instance;
    private SeamlessClearCartItemsMap() {
    }

    public synchronized static SeamlessClearCartItemsMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessClearCartItemsMap.class) {
                if (instance == null) {
                    instance = new SeamlessClearCartItemsMap();
                }
            }
        }
        return instance;
    }
    BySizzle bySizzle = new BySizzle();

    public By cartDiv() {
        return bySizzle.css(".KrogerHeader-Basket");
    }

    public By expandCartButton() {
        return bySizzle.css("button[aria-label='expand cart quick view']");
    }

    public By decrementButton() {
        return bySizzle.css("div[data-qa='pinned-cart-authenticated'] button[data-testid='kds-QuantityStepper-decButton']");
    }
}
