package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoCancelOrderMap {

    private static CiaoCancelOrderMap instance;

    private CiaoCancelOrderMap() {
    }

    public static synchronized CiaoCancelOrderMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoCancelOrderMap.class) {
            if (instance == null) {
                instance = new CiaoCancelOrderMap();
            }
        }
        return instance;
    }

    public By cancelOrderMenuOption() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Cancel Order']");
    }

    public By backButton() {
        return AppiumBy.accessibilityId("Exit Customer Details Button");
    }

    public By customerDetailsOption() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Customer Details']");
    }
}
