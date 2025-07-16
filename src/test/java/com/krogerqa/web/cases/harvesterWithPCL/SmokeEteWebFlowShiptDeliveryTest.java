package com.krogerqa.web.cases.harvesterWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 13 Web Flows -
 * Submit Non-EBT Shipt Delivery order in Kroger.com and perform Batching
 * Verify New Non-EBT Shipt Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up, Paid and Delivered in Cue and validate K-log
 */
public class SmokeEteWebFlowShiptDeliveryTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete13_web_part1_nonEbt_shiptDelivery", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify Shipt Delivery order placed")
    public void validateE2eNewNonEbtShiptOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_13);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_13, testOutputData);
    }

    @Test(groups = {"ete13_web_part2_nonEbt_shiptDelivery", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify Shipt Delivery order picked")
    public void validateE2eNonEbtShiptOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13, ExcelUtils.SCENARIO_13);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SCENARIO_13, testOutputData);
    }

    @Test(groups = {"ete13_web_part3_nonEbt_shiptDelivery", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Shipt Delivery order")
    public void validateE2eNonEbtShiptOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13, ExcelUtils.SCENARIO_13);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete13_web_part4_nonEbt_shiptDelivery", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E Shipt Delivery order fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtShiptOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13, ExcelUtils.SCENARIO_13);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_13);
    }
}
