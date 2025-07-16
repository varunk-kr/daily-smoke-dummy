package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessDeliveryMap {
    private static SeamlessDeliveryMap instance;
    private SeamlessDeliveryMap() {
    }

    public synchronized static SeamlessDeliveryMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessDeliveryMap.class) {
                if (instance == null) {
                    instance = new SeamlessDeliveryMap();
                }
            }
        }
        return instance;
    }

    BySizzle bySizzle = new BySizzle();

    public By acceptDeliveryCheckbox() {
        return bySizzle.css("input[data-qa='delivery-disclaimer-accept']");
    }

    public By deliveryHeading(){
        return bySizzle.css("h1[data-qa='panel-heading']>b");
    }

    public By continueButton(){
        return bySizzle.css("button[data-qa='scheduling-continue-button']");
    }
}
