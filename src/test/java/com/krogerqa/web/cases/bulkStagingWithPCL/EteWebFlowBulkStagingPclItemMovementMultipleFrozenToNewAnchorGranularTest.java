package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11733 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * In Order lookup Perform item movement from Multiple frozen to new container >
 * After Staging,  validate COR event, and Staged Order Status and perform Customer Check-in in Cue>
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowBulkStagingPclItemMovementMultipleFrozenToNewAnchorGranularTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_web_part1_nonEbt_itemMovement", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl single thread order placed for bulk staging anchor granular store")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11733);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE_BEFORE_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.NO_ITEM_MOVEMENT_CONTAINER, ExcelUtils.AMBIENT);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11733, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_web_part2_nonEbt_itemMovement", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify pcl single thread order picked for bulk staging anchor granular store")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11733, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_web_part3_nonEbt_itemMovement", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify pcl single thread order staged and checked-in for bulk staging anchor granular store and perform item movement")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_web_part4_nonEbt_itemMovement", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify pcl single thread order picked up and paid for bulk staging anchor granular store")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11733);
    }
}
