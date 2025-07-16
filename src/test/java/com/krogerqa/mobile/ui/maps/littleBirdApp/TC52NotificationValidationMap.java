package com.krogerqa.mobile.ui.maps.littleBirdApp;


import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class TC52NotificationValidationMap {
    private static TC52NotificationValidationMap instance;

    private TC52NotificationValidationMap() {
    }

    public synchronized static TC52NotificationValidationMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (TC52NotificationValidationMap.class) {
            if (instance == null) {
                instance = new TC52NotificationValidationMap();
            }
        }
        return instance;
    }


    public By notificationsOption() {
        return AppiumBy.xpath("//*[@text='Notifications']");
    }

    public By enablePermitUsageAccess() {
        return AppiumBy.id("android:id/switch_widget");
    }

    public By backButton() {
        return AppiumBy.accessibilityId("Navigate up");
    }

    public By notificationText(int i) {
        return AppiumBy.xpath("(//android.widget.TextView[@resource-id='com.kroger.mobile.sps.notifications.qa:id/display_body_item'])[" + i + "]");
    }

    public By totalNotificationCount() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='com.kroger.mobile.sps.notifications.qa:id/display_body_item']");
    }

    public By notificationHeader() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='com.kroger.mobile.sps.notifications.qa:id/display_text_details']");
    }

    public By notificationContent() {
        return AppiumBy.id("com.kroger.mobile.sps.notifications.qa:id/display_body_details");
    }
}

