package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoDestageCompleteMap {
    private  static CiaoDestageCompleteMap instance;
    private CiaoDestageCompleteMap (){}
    public synchronized static CiaoDestageCompleteMap getInstance() {
        if (instance == null) {
            synchronized (CiaoDestageCompleteMap.class)
            {
                if (instance == null) {
                    instance = new CiaoDestageCompleteMap();
                }
            }
        }
        return instance;
    }

    public By scrollToRemoveFromTaskButton() {
        return AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"Remove from Tasks\"));");
    }

    public By removeFromTasksButton() {
        return AppiumBy.xpath("//*[contains(@text,'Remove from Tasks')]");
    }

    public By completedButton() {
        return AppiumBy.xpath("//*[contains(@text,'Completed')]");
    }

    public By completedText() {
        return AppiumBy.xpath("//*[contains(@text,'Have you completed this order?')]");
    }

    public By orderIdText() {
        return AppiumBy.xpath("//*[contains(@text,'Order ID')]");
    }

    public By spotNumberText() {
        return AppiumBy.xpath("//*[contains(@text,'Spot')]");
    }

    public By goToCheckoutButton() {
        return AppiumBy.xpath("//*[contains(@text,'Checkout')]");
    }

    public By finishedText() {
        return AppiumBy.xpath("//*[contains(@text,'Destage Finished!')]");
    }
}
