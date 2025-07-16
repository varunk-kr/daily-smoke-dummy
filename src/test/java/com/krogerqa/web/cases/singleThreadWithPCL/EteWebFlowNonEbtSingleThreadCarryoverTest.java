package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-8813 Web Flows -
 * Submit NON-EBT Pcl Carryover Pickup order >
 * Verify New NON-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order , container status and OOS container in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadCarryoverTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify carryover order placed for Non-EBT pcl Single Thread order")
    public void validatePclSingleThreadCarryoverTestOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_PCL_SINGLE_THREAD_CARRYOVER, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_8813, testOutputData);
    }

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify carryover order picked and OOS for Non-EBT pcl Single Thread order")
    public void validatePclSingleThreadCarryoverTestOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_8813, testOutputData);
    }

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify carryover order staged and checked-in for Non-EBT pcl Single Thread order")
    public void validatePclSingleThreadCarryoverTestOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify carryover order picked up and paid for Non-EBT pcl Single Thread order")
    public void validatePclSingleThreadCarryoverTestOrderPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
    }
}
