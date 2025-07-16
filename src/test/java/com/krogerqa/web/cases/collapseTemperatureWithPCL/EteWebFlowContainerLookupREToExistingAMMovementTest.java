package com.krogerqa.web.cases.collapseTemperatureWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario pcl_with_collapse_temp_tc_5499 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowContainerLookupREToExistingAMMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_5499_existingContainerItemMovement_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateExistingRefrigeratedContainerItemMovementLookupScreenOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_5499);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_SCENARIO_TC_5499, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5499_existingContainerItemMovement_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateExistingRefrigeratedContainerItemMovementLookupScreenOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_5499, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5499_existingContainerItemMovement_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateNewFrozenContainerMovementLookupScreenOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5499_existingContainerItemMovement_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateNewFrozenContainerMovementLookupScreenOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_5499);
    }
}
