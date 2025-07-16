package com.krogerqa.web.cases.anchorStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario  Anchor Staging TC 4527 Web Flows -
 * Submit Non-EBT Anchor staging Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue >
 * After selecting trolleys with Shorting and Out of Stock, verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After destaging, verify order is dropped from Dash >
 * Verify Non-EBT order is Picked Up and Paid in Cue
 */
public class EteWebAnchorStagingFlowShortOutOfStockTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_anchor_tc_5316_web_part1_anchorShortOutOfStock", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for partially fulfilled Anchor staging order")
    public void validateE2eNewStatusForShortOutOfStockAnchorStagingOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_SCENARIO_TC_5316);
        testOutputData.put(ExcelUtils.NON_PCL_IS_OOS_SHORT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5316, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5316_web_part2_anchorShortOutOfStock", "ete_web_part2_ebt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for partially fulfilled Anchor staging order")
    public void validateE2ePickedStatusForShortOutOfStockAnchorStagingOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5316, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5316_web_part3_anchorShortOutOfStock", "ete_web_part3_ebt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for partially fulfilled Anchor staging order")
    public void validateE2eStagedStatusForShortOutOfStockAnchorStagingOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5316_web_part4_anchorShortOutOfStock", "ete_web_part4_ebt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for partially fulfilled Anchor staging order")
    public void validateE2ePaidStatusForShortOutOfStockAnchorStagingOrder() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5316);
    }
}
