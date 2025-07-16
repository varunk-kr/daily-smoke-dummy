package com.krogerqa.web.cases.harvesterWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Ebt Scenario Web Flows -
 * Submit EBT Pickup order in Kroger.com for Multi-threaded PCL store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * Assign PCLs in harvester native, After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowEbtPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_4550_web_part1_ebt", "ete_web_part1_Ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for PCL EBT order")
    public void validateE2eEbtPclOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_4550);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_SCENARIO_TC_4550, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4550_web_part2_ebt", "ete_web_part2_Ebt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for EBT order")
    public void validateE2eEbtPclOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_4550, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4550_web_part3_ebt", "ete_web_part3_Ebt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for EBT order")
    public void validateE2eEbtPclOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4550_web_part4_ebt", "ete_web_part4_Ebt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for EBT order")
    public void validateE2eEbtPclOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_4550);
    }
}
