package com.krogerqa.web.cases.rushOrderWithoutPCL;

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
 * Scenario RUSH_ORDER_SCENARIO_TC_6696 Web Flows -
 * Submit Non-EBT Non Pcl rush Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign Non pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned Non pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowRushOrderSingleThreadedTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();


    @Test(groups = {"ete_rush_order_tc_6696_web_part1_nonEbt", "ete_web_part1_singleThreaded_rushOrder_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify rush order placed for non-EBT Non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6696_web_part2_nonEbt", "ete_web_part2_singleThreaded_rushOrder_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify rush order picked for non-EBT non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6696_web_part3_nonEbt", "ete_web_part3_singleThreaded_rushOrder_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify rush order staged and checked-in for non-EBT non pcl order")
    public void validateE2eNewSingleThreadedNonEbtNonPclRushOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6696_web_part4_nonEbt", "ete_web_part4_singleThreaded_rushOrder_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify rush order picked up and paid for non-EBT non pcl order")
    public void validateE2eNonEbtNonPclRushOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696);
    }
}