package com.krogerqa.web.cases.tc52NotificationsWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario TC 52 Notification Web Flows - Mark all items as Out of Stock
 * Submit Non-EBT Pcl rush Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 */
public class EteWebFlowNonEbtPclAllItemOutOfStockTc52Test extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_tc52_web_part1_nonEbt", "ete_web_part1_nonEbt_tc52", "ete_web_part1_multiThreaded"}, description = "Verify rush order placed for non-EBT pcl order")
    public void validateE2eNewNonEbtPclTc52() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_Notifications_7044, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.TC_52_Notifications_7044, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.TC_52_Notifications_7044, testOutputData);
        baseCommands.browserBack();
        testOutputData = cueOrderValidation.getItemCount(ExcelUtils.TC_52_NOTIFICATION_TC_7035, testOutputData);
    }

    @Test(groups = {"ete_tc52_web_part2_nonEbt", "ete_web_part2_nonEbt_tc52", "ete_web_part2_multiThreaded"}, description = "Verify rush order picked for non-EBT pcl order")
    public void validateE2eNonEbtPclTc52OutOfStockAndCancel() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_Notifications_7044, ExcelUtils.TC_52_Notifications_7044);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        baseCommands.webpageRefresh();
        cueOrderValidation.verifyOosOrderStatusInCue(testOutputData);
        cueOrderValidation.verifyCancelOrderFromCue(testOutputData);
        baseCommands.webpageRefresh();
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
