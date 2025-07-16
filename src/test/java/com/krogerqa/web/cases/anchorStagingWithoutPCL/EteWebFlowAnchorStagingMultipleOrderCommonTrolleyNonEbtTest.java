package com.krogerqa.web.cases.anchorStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-5310 Web Flows -
 * Submit Non-EBT Pickup order with multiple orders having common trolleys.
 * Verify Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */

/**
 * Execute 1st web testcase and four native testcases before running 2nd web testcase
 */
public class EteWebFlowAnchorStagingMultipleOrderCommonTrolleyNonEbtTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_anchor_tc_5310_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify non-EBT order placed with multiple container having same temperature type for anchor staging")
    public void validateE2eNewNonEbtOrder() {
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_SCENARIO_TC_5310);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_SCENARIO_TC_5310_1);
        firstOrderTestData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5310, firstOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5310_1, secondOrderTestData);
    }
    @Test(groups = {"ete_anchor_tc_5310_web_part2_nonEbt", "ete_web_part2_nonebt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for fully fulfilled Anchor staging order")
    public void validateE2ePickedStatusForShortOutOfStockAnchorStagingOrder() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5310, firstOrderTestData);
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5310_1, secondOrderTestData);
    }

    @Test(groups = {"ete_anchor_tc_5310_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify non-EBT order with multiple container having same temperature type is staged using anchor staging and checked-in")
    public void validateE2eNonEbtOrderStaged() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_anchor_tc_5310_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify non-EBT order with multiple container having same temperature type is picked up and paid")
    public void validateE2eNonEbtOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5310);
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5310_1);
    }
}
