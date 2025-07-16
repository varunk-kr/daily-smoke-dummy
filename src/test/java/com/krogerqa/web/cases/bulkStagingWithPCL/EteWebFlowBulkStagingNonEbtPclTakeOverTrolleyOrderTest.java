package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11715 Web Flows -
 * Submit Non-EBT Pcl order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels  and exit from trolley>
 * Take over and continue selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Cancel the order from Ciao
 * Verify order is Cancelled in Cue and validate K-log
 */
public class EteWebFlowBulkStagingNonEbtPclTakeOverTrolleyOrderTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11715);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_TAKE_OVER_TROLLEY, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11715, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify pcl order picked for bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715, ExcelUtils.BULK_STAGING_TC_11715);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11715,testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify pcl order staged and checked-in for bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715, ExcelUtils.BULK_STAGING_TC_11715);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify pcl order status as picked up and paid for bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715, ExcelUtils.BULK_STAGING_TC_11715);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
