package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-10940 Web Flows -Enhanced dynamic batching
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store and perform enhanced batching using request trolley >
 * Verify New Non-EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowDbNonPCLEnhancedBatchNrAnchorGranularTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingNrAnchorGranularOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_NON_PCL_TC_10940);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.MULTIPLE_TEMP_COLLAPSE, String.valueOf(true));
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AMRE_OSFR);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingNrAnchorGranularOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingNrAnchorGranularOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eEnhancedBatchingNrAnchorGranularOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_NON_PCL_TC_10940);
    }
}
