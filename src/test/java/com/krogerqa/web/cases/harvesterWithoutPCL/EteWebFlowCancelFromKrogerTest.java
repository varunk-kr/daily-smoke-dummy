package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 12 Web Flow -
 * Submit non-EBT Pickup order in Kroger.com for Multi-threaded store >
 * Verify New EBT Order Details in Cue before Batching>
 * Cancel order from Kroger.com
 * Verify Canceled Order and Payment Status in Cue
 */
public class EteWebFlowCancelFromKrogerTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete12_web_part1_cancelFromKroger", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify non-Ebt order canceled from Kroger.com before batching")
    public void validateE2eCancelFromKrogerOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_12);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_12, testOutputData);
        krogerSeamLessPortalOrderCreation.verifyCancelOrder(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
