package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-7455 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue before Batching>
 * Accept bag fee from kroger portal
 * Cancel order from Kroger.com
 * Verify Canceled Order and Payment Status in Cue
 */

public class EteWebFlowCancelOrderFromKCPAfterAcceptingTheBagTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_byob_tc_7455_web_part1_cancelFromKroger", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify non-Ebt byob order canceled from Kroger.com before batching")
    public void validateE2eByobCancelFromKrogerOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7455);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7455, testOutputData);
        krogerSeamLessPortalOrderCreation.verifyCancelOrder(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
