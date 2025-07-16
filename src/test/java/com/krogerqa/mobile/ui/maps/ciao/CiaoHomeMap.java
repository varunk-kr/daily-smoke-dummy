package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoHomeMap {

    private static CiaoHomeMap instance;

    private CiaoHomeMap() {
    }

    public synchronized static CiaoHomeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoHomeMap.class) {
            if (instance == null) {
                instance = new CiaoHomeMap();
            }
        }
        return instance;
    }

    public By visualOrderIdText(String orderId) {
        return AppiumBy.xpath("//*[contains(@text,'" + orderId + "')]");
    }

    public By orderStatusText(String orderId) {
        return AppiumBy.xpath("//*[@text='" + orderId + "']//following-sibling::*[position()=1]");
    }

    public By startButton(String orderId) {
        return AppiumBy.xpath("(//*[@text='" + orderId + "']//following-sibling::*//*[@text='Start'])[position()=1]");
    }

    public By ageCheckLabel(String orderId) {
        return AppiumBy.xpath("//*[@text='" + orderId + "']//following-sibling::*//*[contains(@text,'Age Check Tag')]");
    }

    public By ebtLabel(String orderId) {
        return AppiumBy.xpath("//*[@text='" + orderId + "']//following-sibling::*//*[@text='EBT Tag']");
    }

    public By homeKebabMenu(String orderId) {
        return AppiumBy.xpath("//*[@text='" + orderId + "']/following-sibling::android.widget.Button[1]");
    }

    public By rejectedLabel(String orderId) {
        return AppiumBy.xpath("//*[@text='" + orderId + "']//following-sibling::*//*[@text='Rejected Subs Tag']");
    }

    public By hamburgerMenuIcon() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Hamburger Menu Button']|//android.view.View[@content-desc='Ciao']");
    }

    public By changeButton() {
        return AppiumBy.xpath("//*[@text='Change']");
    }

    public By numberOfOrders() {
        return AppiumBy.xpath("//*[contains(@text,'Next Task')]");
    }
}