package com.krogerqa.web.cases.harrisTeeterDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-14375 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-thread HT store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */
public class SmokeEteWebFlowHarrisTeeterDyBSingleThreadBauTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_Ht_db_pcl_tc_14375_singleThread_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for HT db non-EBT pcl order")
    public void validateE2eHarrisTeeterSingleThreadOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.HARRIS_TEETER_DYB_14375);
        testOutputData.put(ExcelUtils.IS_SINGLE_THREAD, String.valueOf(true));
        testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        testOutputData.put(ExcelUtils.HARRIS_TEETER_DYB, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HARRIS_TEETER_DYB_14375, testOutputData);
    }

    @Test(groups = {"ete_Ht_db_pcl_tc_14375_singleThreaded_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for HT db non-EBT pcl order")
    public void validateE2eHarrisTeeterSingleThreadOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14375, ExcelUtils.HARRIS_TEETER_DYB_14375);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.HARRIS_TEETER_DYB_14375, testOutputData);
    }

    @Test(groups = {"ete_Ht_db_pcl_tc_14375_singleThread_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for HT db non-EBT pcl order")
    public void validateE2eHarrisTeeterSingleThreadOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14375, ExcelUtils.HARRIS_TEETER_DYB_14375);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_Ht_db_pcl_tc_14375_singleThread_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for HT db pcl order")
    public void validateE2eHarrisTeeterSingleThreadOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14375, ExcelUtils.HARRIS_TEETER_DYB_14375);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HARRIS_TEETER_DYB_14375);
    }
}