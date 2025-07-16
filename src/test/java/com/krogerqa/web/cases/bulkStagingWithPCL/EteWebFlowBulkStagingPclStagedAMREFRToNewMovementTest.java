package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING FFILLSVCS-TC-11752  Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * Stage the order and re-stage the container, Perform item movement from Ambient ,refrigerated and frozen to new container, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowBulkStagingPclStagedAMREFRToNewMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for bulk staging ,re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validateE2eNonEbtMultipleContainerItemMovementPclBulkStagingNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11752);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE_BEFORE_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_MULTIPLE_TEMP_CONTAINER_ITEM_MOVEMENT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11752, testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for bulk staging ,re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validateE2eNonEbtMultipleContainerItemMovementPclBulkStagingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11752, testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for bulk staging , re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validateE2eNonEbtMultipleContainerItemMovementPclBulkStagingOrderStagedReStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for bulk staging ,re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validateE2eNonEbtMultipleContainerItemMovementPclBulkStagingOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11752);
    }
}
