package com.krogerqa.mobile.ui.pages.littleBirdApp;

import com.krogerqa.mobile.ui.maps.littleBirdApp.TC52NotificationValidationMap;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.MobileUtils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.util.HashMap;

public class TC52NotificationValidationPage extends BaseTest {

    private static TC52NotificationValidationPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    TC52NotificationValidationMap tc52NotificationValidationMap = TC52NotificationValidationMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();
    String appPackageName;

    private TC52NotificationValidationPage() {
    }

    public synchronized static TC52NotificationValidationPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (TC52NotificationValidationPage.class) {
            if (instance == null) {
                instance = new TC52NotificationValidationPage();
            }
        }
        return instance;
    }

    public void installLittleBirdNotificationApplication() {
        appPackageName = testParameters.get("notificationAppPackage");
        AndroidDriver androidDriver = ((AndroidDriver) mobileCommands.getWebDriver());
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("noReset", "true");
        int retry = 5, i = 0;
        boolean appInstallSuccess = false;
        mobileCommands.wait(5);
        while (!appInstallSuccess && i < retry) {
            try {
                androidDriver.installApp("/builds/Notifications-1.27.2.apk");
                androidDriver.activateApp(appPackageName);
                appInstallSuccess = true;
                mobileCommands.wait(5);
            } catch (Exception | AssertionError e) {
                mobileCommands.wait(3);
                i++;
            }
        }
        mobileCommands.wait(5);
    }

    public void allowUsageAccessForNotificationsApp() {
        mobileCommands.waitForElementVisibility(tc52NotificationValidationMap.notificationsOption());
        mobileCommands.tap(tc52NotificationValidationMap.notificationsOption());
        mobileCommands.waitForElementVisibility(tc52NotificationValidationMap.enablePermitUsageAccess());
        mobileCommands.tap(tc52NotificationValidationMap.enablePermitUsageAccess());
        mobileCommands.wait(10);
        for (int i = 0; i < 2; i++) {
            mobileCommands.waitForElementVisibility(tc52NotificationValidationMap.backButton());
            mobileCommands.tap(tc52NotificationValidationMap.backButton());
        }
    }

    public void validateTC52Notification(HashMap<String, String> testOutputData) {
        appPackageName = testParameters.get("notificationAppPackage");
        try {
            String visualOrderId = testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
            for (int i = 1; i <= mobileCommands
                    .numberOfElements(tc52NotificationValidationMap.totalNotificationCount()); i++) {
                String notificationText = mobileCommands.getElementText(tc52NotificationValidationMap.notificationText(i));
                if (notificationText.contains(visualOrderId)) {
                    mobileCommands.tap(tc52NotificationValidationMap.notificationText(i));
                    mobileCommands.wait(5);
                    break;
                }
            }
            verifyPickupTimeAndItemCount(testOutputData);
            AndroidDriver androidDriver = ((AndroidDriver) mobileCommands.getWebDriver());
            androidDriver.removeApp(appPackageName);
            androidDriver.quit();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.TC52_NOTIFICATIONS, String.valueOf(e));
        }
    }

    public void validateRemoveTC52Notification(HashMap<String, String> testOutputData) {
        String cancelledOrder = mobileCommands.getWebDriver().getPageSource();
        mobileCommands.browserBack();
        Assert.assertFalse(cancelledOrder.contains(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID)),
                "Removed TC52 notification for cancelled order");
    }

    public void verifyPickupTimeAndItemCount(HashMap<String, String> testOutputData) {
        String pickup = mobileCommands.getElementText(tc52NotificationValidationMap.notificationHeader());
        Assert.assertTrue(pickup.toLowerCase().contains(testOutputData.get(ExcelUtils.PICKUP_TIME)));
        String itemCount = mobileCommands.getElementText(tc52NotificationValidationMap.notificationContent());
        Assert.assertTrue(itemCount.contains(testOutputData.get(ExcelUtils.ITEM_COUNT)));
    }
}
