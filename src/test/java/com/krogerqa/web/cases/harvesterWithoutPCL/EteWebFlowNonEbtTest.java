package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.utils.ExcelUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 1 Web Flows -
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete1_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded","smokeWebPart1"}, description = "Verify order placed for non-EBT order")
    public void validateE2eNewNonEbtOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_1);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_1, testOutputData);
    }

    @Test(groups = {"ete1_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded","smokeWebPart2"}, description = "Verify order picked for non-EBT order")
    public void validateE2eNonEbtOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_1, testOutputData);
    }

    @Test(groups = {"ete1_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded","smokeWebPart3"}, description = "Verify order staged and checked-in for non-EBT order")
    public void validateE2eNonEbtOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete1_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded","smokeWebPart4"}, description = "Validate E2E Non-EBT order fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_1);
    }
}