package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoHomeMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.MobileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class CiaoHomePage {

    private static CiaoHomePage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoHomeMap ciaoHomeMap = CiaoHomeMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();

    private CiaoHomePage() {
    }

    public synchronized static CiaoHomePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoHomePage.class) {
            if (instance == null) {
                instance = new CiaoHomePage();
            }
        }
        return instance;
    }

    public void scrollToOrderId(String visualOrderId) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        mobileCommands.waitForElementVisibility(ciaoHomeMap.numberOfOrders());
        String orders = mobileCommands.getElementText(ciaoHomeMap.numberOfOrders());
        String totalOrders = ((orders.split("\\("))[1].split("\\)"))[0];
        int numberOfOrders = Integer.parseInt(totalOrders);
        try {
            for (int i = 0; i < (numberOfOrders * 2 / 3); i++) {
                String text = mobileCommands.getWebDriver().getPageSource();
                if (text.contains(visualOrderId)) {
                    break;
                } else {
                    for (int j = 0; j < 4; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail("Order not found");
        }
    }

    public void assertOrderStatusText(String orderId) {
        mobileUtils.scrollByPercent(100, 450, "down", 1);
        Assert.assertTrue(mobileCommands.getElementText(ciaoHomeMap.orderStatusText("Order ID: " + orderId)).contains(Constants.PickCreation.STAGED));
    }

    public void tapStartButton(String orderId) {
        mobileCommands.tap(ciaoHomeMap.startButton("Order ID: " + orderId));
    }

    public void assertAgeCheckLabel(String orderId) {
        mobileCommands.assertElementDisplayed(ciaoHomeMap.ageCheckLabel("Order ID: " + orderId), true);
    }

    public void assertEbtLabel(String orderId) {
        mobileCommands.assertElementDisplayed(ciaoHomeMap.ebtLabel("Order ID: " + orderId), true);
    }

    public void tapKebabMenu(String orderId) {
        mobileCommands.tap(ciaoHomeMap.homeKebabMenu("Order ID: " + orderId));
    }


    public void verifyOrderDetails(String orderId, boolean ageCheck, boolean isEbt) {
        scrollToOrderId(orderId);
        assertOrderStatusText(orderId);
        if (ageCheck) {
            assertAgeCheckLabel(orderId);
        }
        if (isEbt) {
            assertEbtLabel(orderId);
        }
    }

    public void tapHamburgerMenuIcon() {
        mobileCommands.waitForElementVisibility(ciaoHomeMap.hamburgerMenuIcon());
        mobileCommands.tap(ciaoHomeMap.hamburgerMenuIcon());
    }

    public void tapChangeButton() {
        mobileCommands.waitForElementVisibility(ciaoHomeMap.changeButton());
        mobileCommands.tap(ciaoHomeMap.changeButton());
    }

    public void assertRejectedSubsLabelDisplayed(String orderId) {
        mobileCommands.assertElementDisplayed(ciaoHomeMap.rejectedLabel("Order ID: " + orderId), true);
    }

    public void assertEbtLabelDisplayed(String orderId) {
        mobileCommands.assertElementDisplayed(ciaoHomeMap.ebtLabel("Order ID: " + orderId), true);
    }

    public void selectRejectedSubsOrder(String orderId, Boolean isEbt) {
        assertOrderStatusText(orderId);
        assertRejectedSubsLabelDisplayed(orderId);
        if (isEbt) {
            assertEbtLabelDisplayed(orderId);
        }
        tapStartButton(orderId);
    }
}
