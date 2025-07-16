package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 8 Web Flows -
 * Submit Non-EBT Pickup order in HarrisTeeter.com for Single-threaded Harris Teeter store >
 * Verify New Non-EBT Harris Teeter Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowHarrisTeeterTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete8_web_part1_harrisTeeter", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify Harris Teeter order placed")
    public void validateE2eNewHarrisTeeterOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_8);
        testOutputData.put(ExcelUtils.IS_SINGLE_THREAD, String.valueOf(true));
        testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_8, testOutputData);
    }

    @Test(groups = {"ete8_web_part2_harrisTeeter", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify Harris Teeter order picked")
    public void validateE2eHarrisTeeterOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_8, testOutputData);
    }

    @Test(groups = {"ete8_web_part3_harrisTeeter", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify Harris Teeter order staged and checked-in")
    public void validateE2eHarrisTeeterOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete8_web_part4_harrisTeeter", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Validate E2E Harris Teeter order fulfillment (Picking and Staging Using Harvester App)")
    public void validateE2eHarrisTeeterOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_8);
    }
}