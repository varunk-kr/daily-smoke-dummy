package com.krogerqa.web.cases.rushOrderManualBatchingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario RUSH_ORDER_SCENARIO_TC_7120 Web Flows -
 * Submit multiple non-EBT rush Pickup orders in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign Non pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned Non pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowMultipleRushOrdersInSameSlotTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();


    @Test(groups = {"ete_romb_tc_7120_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify rush order placed for non-EBT Non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrder() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, secondOrderTestData);
        baseCommands.wait(60);
        baseCommands.webpageRefresh();
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.multipleOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders);
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, secondOrderTestData);
    }

    @Test(groups = {"ete_romb_tc_7120_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify rush order picked for non-EBT non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, secondOrderTestData);
    }

    @Test(groups = {"ete_romb_tc_7120_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify rush order staged and checked-in for non-EBT non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_romb_tc_7120_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify rush order picked up and paid for non-EBT non pcl order")
    public void validateE2eNonEbtNonPclRushOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
    }
}