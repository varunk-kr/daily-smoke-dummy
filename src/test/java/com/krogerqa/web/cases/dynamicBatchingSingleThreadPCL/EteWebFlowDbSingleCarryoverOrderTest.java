package com.krogerqa.web.cases.dynamicBatchingSingleThreadPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-12732 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-thread store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */
public class EteWebFlowDbSingleCarryoverOrderTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_pcl_tc_12732_singleThread_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for db non-EBT pcl order")
    public void validateE2eDbPclOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_SINGLE_THREAD_12732);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_PCL_SINGLE_THREAD_CARRYOVER, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_SINGLE_THREAD_12732, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_12732_singleThread_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for db non-EBT pcl order")
    public void validateE2eDbPclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12732, ExcelUtils.DB_SINGLE_THREAD_12732);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_SINGLE_THREAD_12732, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_12732_singleThread_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl order")
    public void validateE2eDbPclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12732, ExcelUtils.DB_SINGLE_THREAD_12732);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_12732_singleThread_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl order")
    public void validateE2eDbPclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12732, ExcelUtils.DB_SINGLE_THREAD_12732);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_SINGLE_THREAD_12732);
    }
}