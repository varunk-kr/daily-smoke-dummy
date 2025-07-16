package com.krogerqa.web.cases.pclOversize;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12225 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store with hot items and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowMoveItemsFromAmToNewViaPickingPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12225_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for pcl oversize order")
    public void validateE2eNewNonEbtItemMovementViaPickingPclOversizeOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12225);
        testOutputData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12225, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12225_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for pcl oversize order")
    public void validateE2eNonEbtItemMovementViaPickingPclOversizePicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12225, ExcelUtils.PCL_OVERSIZE_TC_12225);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12225, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12225_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for pcl oversize order")
    public void validateE2eNonEbtItemMovementViaPickingPclOversizeStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12225, ExcelUtils.PCL_OVERSIZE_TC_12225);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12225_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for pcl oversize order")
    public void validateE2eNonEbtItemMovementViaPickingPclOversizePaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12225, ExcelUtils.PCL_OVERSIZE_TC_12225);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12225);
    }
}
