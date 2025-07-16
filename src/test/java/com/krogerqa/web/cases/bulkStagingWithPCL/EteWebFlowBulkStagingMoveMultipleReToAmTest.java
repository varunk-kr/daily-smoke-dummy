package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11732 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Perform Item movement via order lookup from multiple refrigerated containers to existing Ambient container
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowBulkStagingMoveMultipleReToAmTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_11732_bulkStaging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for bulk staging item movement order")
    public void validateE2eNonEbtPclBulkStagingItemMoveMultipleReToAMNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11732);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_AMBIENT_CONTAINER, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11732, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11732_bulkStaging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for bulk staging item movement order")
    public void validateE2eNonEbtPclBulkStagingItemMoveMultipleReToAMOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11732,ExcelUtils.BULK_STAGING_TC_11732);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11732, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11732_bulkStaging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for bulk staging item movement order")
    public void validateE2eNonEbtPclBulkStagingItemMoveMultipleReToAMOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11732,ExcelUtils.BULK_STAGING_TC_11732);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11732_bulkStaging_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for bulk staging item movement order")
    public void validateE2eNonEbtPclBulkStagingItemMoveMultipleReToAMPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11732,ExcelUtils.BULK_STAGING_TC_11732);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11732);
    }
}
