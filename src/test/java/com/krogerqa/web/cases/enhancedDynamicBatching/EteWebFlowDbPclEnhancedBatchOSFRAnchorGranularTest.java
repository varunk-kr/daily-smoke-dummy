package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-10946 Web Flows -Enhanced Batching PCL Anchor/Granular Staging Flow.
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching-Enhanced Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbPclEnhancedBatchOSFRAnchorGranularTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_pcl_anchorGranular_tc_10946_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Enhanced Batching db non-EBT pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingPclOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10946);
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.OSFR);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10946, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_anchorGranular_tc_10946_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Enhanced Batching db non-EBT pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingPclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10946, ExcelUtils.EB_PCL_TC_10946);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10946, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_anchorGranular_tc_10946_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Enhanced Batching db non-EBT pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingPclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10946, ExcelUtils.EB_PCL_TC_10946);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_anchorGranular_tc_10946_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Enhanced Batching db non-EBT pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingPclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10946, ExcelUtils.EB_PCL_TC_10946);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10946);
    }
}
