package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessDeliveryMap;

public class SeamlessDeliveryPage {
    private static SeamlessDeliveryPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessDeliveryMap seamlessDeliveryMap = SeamlessDeliveryMap.getInstance();

    private SeamlessDeliveryPage() {
    }

    public synchronized static SeamlessDeliveryPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessDeliveryPage.class) {
            if (instance == null) {
                instance = new SeamlessDeliveryPage();
            }
        }
        return instance;
    }

    public void clickAcceptTerms() {
        baseCommands.click(seamlessDeliveryMap.acceptDeliveryCheckbox());
    }
}