package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario  FFILLSVCS-TC-10945 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching-Enhanced Batching using request trolleys >
 * Customer Accepts bag fee for Kroger bag
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class SmokeEteWebFlowDbPclEnhancedBatchAMREByobAcceptTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.EB_PCL_TC_10945, testOutputData);
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AMRE);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.BYOB_ALL_COLLAPSE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10945, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10945, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eEnhancedBatchingPclByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Enhanced Batching db non-EBT pcl byob order (Picking and Staging using Harvester Native app)")
    public void validateE2eEnhancedBatchingPclByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10945);
    }
}
