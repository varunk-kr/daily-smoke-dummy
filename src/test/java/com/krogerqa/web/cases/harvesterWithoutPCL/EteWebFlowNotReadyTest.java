package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 10 Web Flows -
 * Submit EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue and validate the details for Not Ready container>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowNotReadyTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete1_web_part1_NotReadyItem", "ete_web_part1_Ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Not Ready EBT")
    public void validateE2eNewStatusForNotReadyEbtOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_10);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_10, testOutputData);
    }

    @Test(groups = {"ete1_web_part2_NotReadyItem", "ete_web_part2_Ebt", "ete_web_part2_multiThreaded"}, description = "Verify order picked in Cue marking item as Not Ready")
    public void validateE2ePickedStatusForNotReadyEbtOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_10, testOutputData);
    }

    @Test(groups = {"ete1_web_part3_NotReadyItem", "ete_web_part3_Ebt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for not ready EBT order")
    public void validateE2eStagedStatusForNotReadyEbtOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete1_web_part4_NotReadyItem", "ete_web_part4_Ebt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for not Ready EBT order")
    public void validateE2ePaidStatusForNotReadyEbtOrder() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_10);
    }
}
