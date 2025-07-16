package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario BYOB_TC_7453 Web Flows -
 * Submit 3 Non-EBT Pcl Pickup orders in Kroger.com for multi Threaded store>
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting for all the orders.
 * Stage one order and Cancel one order with kroger bag.
 * After Bulk Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class E2eWebFlowOOSByobTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_byob_tc_7453_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_TC_7453, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.BYOB_TC_7453, testOutputData);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AMRE);
        testOutputData.put(ExcelUtils.BYOB_ALL_COLLAPSE, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_TC_7453, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7453_web_part2_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify change preference BYOB SingleThread pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtSingleThreadPclChangeBagPrefBYOBfOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_TC_7453, ExcelUtils.BYOB_TC_7453);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        testOutputData.put(ExcelUtils.CHANGE_BAG_PREF, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData, testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_byob_tc_7453_web_part3_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_TC_7453, ExcelUtils.BYOB_TC_7453);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_TC_7453, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7453_web_part4_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_TC_7453, ExcelUtils.BYOB_TC_7453);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7453_web_part5_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Enhanced Batching db non-EBT pcl byob order (Picking and Staging using Harvester Native app)")
    public void validateE2eEnhancedBatchingPclByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_TC_7453, ExcelUtils.BYOB_TC_7453);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_TC_7453);
    }
}

