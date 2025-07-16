package com.krogerqa.web.cases.serviceCounterIntegration;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SC_INTEGRATION_TC_14431 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class SmokeEteWebFlowScIntegrationBauTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_scIntegration_tc_14431_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT sc integration order")
    public void validateE2eNewNonEbtPclScIntegrationOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SC_INTEGRATION_TC_14431);
        testOutputData.put(ExcelUtils.SC_ITEMS_MT_SCALE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
    }

    @Test(groups = {"ete_scIntegration_tc_14431_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT sc integration pcl order")
    public void validateE2eNonEbtPclScIntegrationOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
    }

    @Test(groups = {"ete_scIntegration_tc_14431_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT sc integration pcl order")
    public void validateE2eNonEbtPclScIntegrationOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_scIntegration_tc_14431_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E SC MT integration order fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtPclScIntegrationOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SC_INTEGRATION_TC_14431);
    }
}
