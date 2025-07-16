package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoDestageCompleteMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.ExcelUtils;
import org.testng.Assert;

import java.util.HashMap;

public class CiaoDeStageCompletePage {
    static String HAVE_YOU_COMPLETED_THIS_ORDER = "Have you completed this order?";
    static String DE_STAGE_FINISHED = "Destage Finished!";
    private static CiaoDeStageCompletePage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoDestageCompleteMap ciaoDestageCompleteMap = CiaoDestageCompleteMap.getInstance();

    private CiaoDeStageCompletePage() {
    }

    public synchronized static CiaoDeStageCompletePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoDeStageCompletePage.class) {
            if (instance == null) {
                instance = new CiaoDeStageCompletePage();
            }
        }
        return instance;
    }

    public void assertOrderIdText(String orderId) {
        Assert.assertTrue(mobileCommands.getElementText(ciaoDestageCompleteMap.orderIdText()).contains(orderId));
    }

    public void assertSpotText(String spot) {
        Assert.assertTrue(mobileCommands.getElementText(ciaoDestageCompleteMap.spotNumberText()).contains(spot));
    }

    public void tapGoToCheckoutButton() {
        mobileCommands.tap(ciaoDestageCompleteMap.goToCheckoutButton());
    }

    public void verifyEbtDeStageComplete(String orderId) {
        assertDeStageFinishedStatus();
        assertOrderIdText(orderId);
    }

    public void removeOrderFromTasks() {
        mobileCommands.element(ciaoDestageCompleteMap.scrollToRemoveFromTaskButton());
        mobileCommands.click(ciaoDestageCompleteMap.removeFromTasksButton());
        Assert.assertTrue(mobileCommands.getElementText(ciaoDestageCompleteMap.completedText()).contains(HAVE_YOU_COMPLETED_THIS_ORDER));
        mobileCommands.click(ciaoDestageCompleteMap.completedButton());
    }

    public void assertDeStageFinishedStatus() {
        Assert.assertTrue(mobileCommands.getElementText(ciaoDestageCompleteMap.finishedText()).contains(DE_STAGE_FINISHED));
    }

    public void verifyDeStageComplete(String orderId, String spot, HashMap<String, String> testOutputData) {
        assertDeStageFinishedStatus();
        assertOrderIdText(orderId);
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER))) {
            assertSpotText(spot);
        }

        try {
            tapGoToCheckoutButton();
        } catch (Exception | AssertionError e) {
            tapGoToCheckoutButton();
        }
    }
}
