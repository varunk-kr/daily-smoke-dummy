package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeStoreMap;
import com.krogerqa.mobile.ui.maps.login.WelcomeToChromeMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.MobileUtils;
import io.appium.java_client.android.AndroidDriver;

public class HarvesterNativeStorePage {

    private static HarvesterNativeStorePage instance;
    HarvesterNativeStoreMap harvesterNativeStoreMap = HarvesterNativeStoreMap.getInstance();
    MobileCommands mobileCommands = new MobileCommands();
    MobileUtils mobileUtils = MobileUtils.getInstance();
    WelcomeToChromeMap welcomeToChromeMap = WelcomeToChromeMap.getInstance();

    private HarvesterNativeStorePage() {
    }

    public synchronized static HarvesterNativeStorePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeStorePage.class) {
            if (instance == null) {
                instance = new HarvesterNativeStorePage();
            }
        }
        return instance;
    }

    public void enterDivision(String division) {
        mobileCommands.waitForElement(harvesterNativeStoreMap.divisionInput());
        mobileCommands.enterText(harvesterNativeStoreMap.divisionInput(), division, true);
    }

    public void clickEnterButton() {
        mobileCommands.tap(harvesterNativeStoreMap.enterButton());
    }

    public void enterStore(String store) {
        mobileCommands.enterText(harvesterNativeStoreMap.storeInput(), store, true);
    }

    public void allowPermissions() {
        mobileCommands.waitForElementVisibility(harvesterNativeStoreMap.divisionInput());
        AndroidDriver driver = (AndroidDriver) mobileCommands.getWebDriver();
        driver.activateApp("com.android.settings");
        mobileCommands.waitForElementVisibility(welcomeToChromeMap.appsButton());
        mobileCommands.tap(welcomeToChromeMap.appsButton());
        mobileCommands.waitForElementVisibility(welcomeToChromeMap.harvesterStageButton());
        mobileCommands.tap(welcomeToChromeMap.harvesterStageButton());
        mobileCommands.waitForElementVisibility(welcomeToChromeMap.notificationsButton());
        mobileCommands.waitForElementVisibility(welcomeToChromeMap.getNotificationsText());
        String notificationsText = mobileCommands.getElementText(welcomeToChromeMap.getNotificationsText());
        if (notificationsText.equalsIgnoreCase("Off")) {
            mobileCommands.tap(welcomeToChromeMap.notificationsButton());
            mobileCommands.waitForElementVisibility(welcomeToChromeMap.allowNotifications());
            mobileCommands.tap(welcomeToChromeMap.allowNotifications());
            mobileCommands.wait(1);
        }
        driver.activateApp("com.kroger.harvester.stage");
    }

    public void setupLocation(String division, String store) {
        try {
            allowPermissions();
            enterDivision(division);
            enterStore(store);
            clickEnterButton();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }
}