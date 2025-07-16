package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 7 Web Flows -
 * Submit EBT Pickup order in Kroger.com for Single-threaded store >
 * Verify New EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After canceling order from Ciao, verify order is dropped from Dash >
 * Verify Order and Payment Status is Canceled in Cue
 */
public class EteWebFlowCancelFromCiaoTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete7_web_part1_cancelFromCiao", "ete_web_part1_ebt", "ete_web_part1_singleThreaded"}, description = "Verify EBT order placed")
    public void validateE2eNewStatusForCancelFromCiaoOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_7);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_7, testOutputData);
    }

    @Test(groups = {"ete7_web_part2_cancelFromCiao", "ete_web_part2_ebt", "ete_web_part2_singleThreaded"}, description = "Verify EBT order picked")
    public void validateE2ePickedStatusForCancelFromCiaoOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_7, testOutputData);
    }

    @Test(groups = {"ete7_web_part3_cancelFromCiao", "ete_web_part3_ebt", "ete_web_part3_singleThreaded"}, description = "Verify EBT order staged and checked-in")
    public void validateE2eStagedStatusForCancelFromCiaoOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete7_web_part4_cancelFromCiao", "ete_web_part4_ebt", "ete_web_part4_singleThreaded"}, description = "Verify EBT order canceled")
    public void validateE2eCanceledStatusForCancelFromCiaoOrder() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
