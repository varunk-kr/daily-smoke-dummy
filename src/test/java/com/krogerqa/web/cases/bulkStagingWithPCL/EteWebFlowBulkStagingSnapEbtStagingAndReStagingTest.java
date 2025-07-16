package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11720 Web Flows -
 * Submit Snap-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Snap-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, Perform item movement from refrigerated to existing refrigerated container, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowBulkStagingSnapEbtStagingAndReStagingTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_11720_bulkStaging_web_part1_snapEbt", "ete_web_part1_snapEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for bulk staging order")
    public void validateE2eSnapEbtPclBulkReStagingNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11720);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11720, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11720_bulkStaging_web_part2_snapEbt", "ete_web_part2_snapEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for bulk staging order")
    public void validateE2eSnapEbtPclBulkReStagingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11720, ExcelUtils.BULK_STAGING_TC_11720);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11720, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11720_bulkStaging_web_part3_snapEbt", "ete_web_part3_snapEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for bulk staging order after item movement")
    public void validateE2eSnapEbtPclBulkReStagingOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11720, ExcelUtils.BULK_STAGING_TC_11720);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11720_bulkStaging_web_part4_snapEbt", "ete_web_part4_snapEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for bulk staging order")
    public void validateE2eSnapEbtPclBulkReStagingOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11720, ExcelUtils.BULK_STAGING_TC_11720);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11720);
    }
}
