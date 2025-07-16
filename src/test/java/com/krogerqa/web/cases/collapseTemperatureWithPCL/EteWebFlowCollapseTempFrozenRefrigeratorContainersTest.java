package com.krogerqa.web.cases.collapseTemperatureWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-5492 Web Flows -
 * Submit Snap-EBT Pcl Pickup order for various fulfillment types in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Snap-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowCollapseTempFrozenRefrigeratorContainersTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for various fulfillment types for snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_5492);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_SCENARIO_TC_5492, testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for various fulfillment types for snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_5492, testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for various fulfillment types for snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Fulfillment Smoke-(1. Substitute Accept and Reject, 2. Full OOS, 3. Not Ready Service Counter item, 4. Shorting, 5. Non-EBT order fulfillment)")
    public void validatePclFulfillmentTypesSmokeTestOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_5492);
    }
}