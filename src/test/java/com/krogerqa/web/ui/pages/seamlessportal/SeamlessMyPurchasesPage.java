package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.web.ui.maps.seamlessportal.SeamlessMyPurchasesMap;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;

public class SeamlessMyPurchasesPage {

    private static SeamlessMyPurchasesPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessMyPurchasesMap seamlessMyPurchasesMap = SeamlessMyPurchasesMap.getInstance();

    private SeamlessMyPurchasesPage() {
    }

    public synchronized static SeamlessMyPurchasesPage getInstance() {
        if (instance == null) {
            synchronized (SeamlessMyPurchasesPage.class) {
                if (instance == null) {
                    instance = new SeamlessMyPurchasesPage();
                }
            }
        }
        return instance;
    }

    public void cancelOrder() {
        baseCommands.click(seamlessMyPurchasesMap.cancelOrderButton());
        baseCommands.click(seamlessMyPurchasesMap.confirmCancelOrderButton());
    }

    public void modifyOrder() {
        baseCommands.click(seamlessMyPurchasesMap.modifyOrderButton());
    }
}
