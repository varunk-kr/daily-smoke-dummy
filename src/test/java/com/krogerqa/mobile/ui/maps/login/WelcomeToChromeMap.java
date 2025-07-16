package com.krogerqa.mobile.ui.maps.login;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class WelcomeToChromeMap {

    private static WelcomeToChromeMap instance;

    private WelcomeToChromeMap() {
    }

    public synchronized static WelcomeToChromeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (WelcomeToChromeMap.class) {
            if (instance == null) {
                instance = new WelcomeToChromeMap();
            }
        }
        return instance;
    }

    public By acceptTermsButton() {
        return AppiumBy.xpath("//*[@text='Accept & continue']|//*[@text='Use without an account']");
    }

    public By noThanksButton() {
        return AppiumBy.id("com.android.chrome:id/negative_button");
    }

    public By appsButton() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Apps']");
    }

    public By harvesterStageButton() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Harvester Stage']");
    }

    public By notificationsButton() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Notifications']");
    }

    public By allowNotifications() {
        return AppiumBy.xpath("//android.widget.Switch[@resource-id='android:id/switch_widget']");
    }

    public By getNotificationsText() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='Notifications']//following-sibling::android.widget.TextView");
    }
}
