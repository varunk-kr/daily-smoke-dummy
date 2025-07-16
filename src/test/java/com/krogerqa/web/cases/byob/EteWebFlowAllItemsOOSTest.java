package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario BYOB_TC_9170 Web Flows BYOB functionality-
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowAllItemsOOSTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_byob_tc_9170_AllItemsOOS_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify BYOB order placed for non-EBT order for item movement from Picking screen")
    public void validateE2eNewNonEbtPickingItemMovementOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_9170);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.BYOB_ALL_COLLAPSE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_9170, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_9170_AllItemsOOS_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT order for item movement from Picking screen")
    public void validateE2eNonEbtPickingItemMovementOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9170, ExcelUtils.BYOB_PCL_TC_9170);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        baseCommands.webpageRefresh();
        cueOrderValidation.verifyOosOrderStatusInCue(testOutputData);
    }
}
