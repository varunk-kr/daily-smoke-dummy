package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoSummaryMap {

    private static CiaoSummaryMap instance;

    private CiaoSummaryMap() {
    }

    public static synchronized CiaoSummaryMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoSummaryMap.class) {
            if (instance == null) {
                instance = new CiaoSummaryMap();
            }
        }
        return instance;
    }

    public By completeButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Complete']");
    }

    public By kebabMenu() {
        return AppiumBy.xpath("(//android.widget.Button[@content-desc='Adjustment Menu button'])[1]");
    }

    public By quantity() {
        return AppiumBy.xpath("(//android.widget.Button[@content-desc='Adjustment Menu button'])//preceding-sibling::android.widget.TextView[contains(@text,'qty')]");
    }

    public By removeItemOption() {
        return AppiumBy.xpath("//*[contains(@text,'Remove Item')]");
    }

    public By removeItemDropdown() {
        return AppiumBy.xpath("//*[contains(@text,'Select')]");
    }

    public By cancelOrderButton() {
        return AppiumBy.xpath("//*[@text='CANCEL ORDER']");
    }

    public By getAllItemsCount() {
        return AppiumBy.xpath("//*[contains(@text,'All Items for Pickup')]");
    }

    public By selectOptionForRemove() {
        return AppiumBy.xpath("//*[contains(@text,'Customer')]");
    }

    public By removeButton() {
        return AppiumBy.xpath("//*[contains(@text,'REMOVE')]");
    }
}
