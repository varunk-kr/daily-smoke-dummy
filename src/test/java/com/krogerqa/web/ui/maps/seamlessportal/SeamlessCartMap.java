package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessCartMap {
    private static SeamlessCartMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessCartMap() {
    }

    public synchronized static SeamlessCartMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessCartMap.class) {
                if (instance == null) {
                    instance = new SeamlessCartMap();
                }
            }
        }
        return instance;
    }

    public By cartButton() {
        return bySizzle.css("output[data-testid='CartHeader-badge']");
    }

    public By cartCheckoutPickupButton() {
        return bySizzle.css("div[data-qa=summary-section-container-PICKUP] button[data-qa=pickup-schedule-button]");
    }

    public By addEditAddress() {
        return bySizzle.css("div[data-qa='summary-section-container-DELIVERY'] button[data-qa='delivery-add-or-edit-address-button']");
    }

    public By cartCheckoutDeliveryButton() {
        return bySizzle.css("div[class^='kds-Card CartSummary-container'] div>button[data-qa='delivery-schedule-button']");
    }
}
