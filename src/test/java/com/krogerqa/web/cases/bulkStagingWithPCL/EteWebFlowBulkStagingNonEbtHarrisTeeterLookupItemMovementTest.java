package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11786 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded Harris Teeter store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */

public class EteWebFlowBulkStagingNonEbtHarrisTeeterLookupItemMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_11786_bulkStaging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_harrisTeeter"}, description = "Verify pcl order placed for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11786);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11786, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11786_bulkStaging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_harrisTeeter"}, description = "Verify order picked for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11786, ExcelUtils.BULK_STAGING_TC_11786);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11786, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11786_bulkStaging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_harrisTeeter"}, description = "Verify order staged and checked-in for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11786, ExcelUtils.BULK_STAGING_TC_11786);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11786_bulkStaging_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_harrisTeeter"}, description = "Verify order picked up and paid for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11786, ExcelUtils.BULK_STAGING_TC_11786);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11786);
    }
}