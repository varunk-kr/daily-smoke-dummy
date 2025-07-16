package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoCancelOrderMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import org.testng.Assert;

public class CiaoCancelOrderPage {
    private static CiaoCancelOrderPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoCancelOrderMap ciaoCancelOrderMap = CiaoCancelOrderMap.getInstance();
    CiaoHomePage ciaoHomePage = CiaoHomePage.getInstance();
    static String cancelMessage="Cancel order option is visible in Ciao.";

    private CiaoCancelOrderPage() {
    }

    public static synchronized CiaoCancelOrderPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoCancelOrderPage.class) {
            if (instance == null) {
                instance = new CiaoCancelOrderPage();
            }
        }
        return instance;
    }

    public void tapCustomerDetailsOption() {
        mobileCommands.waitForElementVisibility(ciaoCancelOrderMap.customerDetailsOption());
        mobileCommands.tap(ciaoCancelOrderMap.customerDetailsOption());
    }

    public void cancelOrder(String orderId) {
        ciaoHomePage.tapKebabMenu(orderId);
        if(mobileCommands.elementDisplayed(ciaoCancelOrderMap.cancelOrderMenuOption())){
            Assert.fail(cancelMessage);
        }
        tapCustomerDetailsOption();
        mobileCommands.waitForElementVisibility(ciaoCancelOrderMap.backButton());
        mobileCommands.tap(ciaoCancelOrderMap.backButton());
    }
}
