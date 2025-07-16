package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessMyPurchasesMap {

    private static SeamlessMyPurchasesMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessMyPurchasesMap() {
    }

    public synchronized static SeamlessMyPurchasesMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessMyPurchasesMap.class) {
                if (instance == null) {
                    instance = new SeamlessMyPurchasesMap();
                }
            }
        }
        return instance;
    }

    public By cancelOrderButton() {
        return bySizzle.css("button[aria-label ='cancel this order']");
    }

    public By confirmCancelOrderButton() {
        return bySizzle.css("button[aria-label ='confirm cancel order']");
    }

    public By modifyOrderButton() {
        return bySizzle.css("button[aria-label='modify this order']");
    }
}
