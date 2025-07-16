package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeStoreMap {

    private static HarvesterNativeStoreMap instance;

    private HarvesterNativeStoreMap() {
    }

    public synchronized static HarvesterNativeStoreMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeStoreMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeStoreMap();
            }
        }
        return instance;
    }

    public By divisionInput() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Division input']/following-sibling::android.widget.EditText");
    }

    public By storeInput() {
        return AppiumBy.xpath("//android.widget.EditText[contains(@text,'Store')]");
    }

    public By enterButton() {
        return AppiumBy.accessibilityId("Right Button in Bottom Sheet");
    }
}
