package com.krogerqa.web.cases.flexWithDeMeter;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueFlexPage;
import com.krogerqa.web.ui.pages.deMeter.DeMeterPickingPage;
import com.krogerqa.web.ui.pages.deMeter.DeMeterValidationsPage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FLEX_SCENARIO_TC_4863 Web Flows
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded Pcl store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * Create flex job request and accept it
 * Perform DeMeter picking for AM and RE items
 * After picking remaining items through harvester native, stage the items
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */

public class EteWebFlowPclFlexLabourPickingTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueFlexPage cueFlexPage = CueFlexPage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    DeMeterValidationsPage deMeterValidationsPage = DeMeterValidationsPage.getInstance();
    DeMeterPickingPage deMeterPickingPage = DeMeterPickingPage.getInstance();

    @Test(groups = {"ete_flexPcl_tc_4863_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed and Perform DeMeter Picking for non-EBT order")
    public void validatePclFlexOrderPlacedAndPickedWithDeMeter() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.FLEX_SCENARIO_TC_4863);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.FLEX_SCENARIO_TC_4863, testOutputData);
        testOutputData = cueOrderValidation.getTrolleyMapForFlexValidation(testOutputData);
        testOutputData.put(ExcelUtils.IS_FLEX_SCENARIO, String.valueOf(true));
        testOutputData = cueOrderValidation.updateMapsForFlexPicking(ExcelUtils.FLEX_SCENARIO_TC_4863, testOutputData);
        cueFlexPage.verifyFlexJobAndPerformValidations();
        deMeterValidationsPage.openDeMeterInNewTabAndPerformLogin();
        deMeterValidationsPage.loginDeMeterAndsetUpStore(testOutputData.get(ExcelUtils.STORE_ID));
        deMeterPickingPage.verifyPclFlexPickingWithDemeter(ExcelUtils.FLEX_SCENARIO_TC_4863,testOutputData, DeMeterValidationsPage.flexWindowHandle, DeMeterValidationsPage.deMeterWindowHandle);
        cueFlexPage.validateTrolleyStatusFromTrolleyPage(testOutputData);
    }

    @Test(groups = {"ete_flexPcl_tc_4863_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for Non Pcl flex labour")
    public void validatePclFlexOrderPickedStatus() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.FLEX_SCENARIO_TC_4863, testOutputData);
        cueFlexPage.validateFlexJobCompletedStatus();
        cueFlexPage.validateTrolleyStatusCompletionInFlexPage(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP)));
    }

    @Test(groups = {"ete_flexPcl_tc_4863_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Non Pcl flex labour")
    public void validatePclFlexOrderStagedStatus() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_flexPcl_tc_4863_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for Non Pcl flex labour")
    public void validatePclFlexOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.FLEX_SCENARIO_TC_4863);
    }
}