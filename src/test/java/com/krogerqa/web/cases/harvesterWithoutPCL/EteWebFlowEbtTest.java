package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 2 Web Flows -
 * Submit EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowEbtTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete2_web_part1_ebt", "ete_web_part1_ebt", "ete_web_part1_multiThreaded","smokeWebPart1"}, description = "Verify order placed for EBT order")
    public void validateE2eNewEbtOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_2);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_2, testOutputData);
    }

    @Test(groups = {"ete2_web_part2_ebt", "ete_web_part2_ebt", "ete_web_part2_multiThreaded","smokeWebPart2"}, description = "Verify order picked for EBT order")
    public void validateE2eEbtOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_2, testOutputData);
    }

    @Test(groups = {"ete2_web_part3_ebt", "ete_web_part3_ebt", "ete_web_part3_multiThreaded","smokeWebPart3"}, description = "Verify order staged and checked-in for EBT order")
    public void validateE2eEbtOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete2_web_part4_ebt", "ete_web_part4_ebt", "ete_web_part4_multiThreaded","smokeWebPart4"}, description = "Verify order picked up and paid for EBT order")
    public void validateE2eEbtOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_2);
    }
}
