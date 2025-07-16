package com.krogerqa.web.cases.dynamicBatchingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DYNAMIC_BATCH_TC_9854 Web Flows -
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store and perform dynamic Batching >
 * Verify New Non-EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowDbPclAnchorGranularStagingTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation=KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for Dynamic batching Non EBT anchor granular Order")
    public void validateE2eDbPclAnchorGranularStagingOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Dynamic batching Non EBT anchor granular Order")
    public void validateE2eDbPclAnchorGranularStagingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Dynamic batching Non EBT anchor granular Order")
    public void validateE2eDbPclAnchorGranularStagingOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Dynamic batching Non EBT anchor granular Order")
    public void validateE2eDbPclAnchorGranularStagingOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
    }
}