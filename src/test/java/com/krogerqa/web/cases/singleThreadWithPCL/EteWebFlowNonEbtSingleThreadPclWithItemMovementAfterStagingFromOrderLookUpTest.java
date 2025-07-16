package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SINGLE THREAD PCL FFILLSVCS-TC-9004 Web Flows -
 * Submit Non-EBT SingleThread Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadPclWithItemMovementAfterStagingFromOrderLookUpTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9004_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify SingleThread pcl order placed non-EBT order for item movement after staging from Order LookUp")
    public void validateE2eNewNonEbtSingleThreadPclOrderForItemMovement() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9004);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        //testOutputData.put(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9004, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9004_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for non-EBT SingleThread pcl order for item movement after staging from Order LookUp")
    public void validateE2eNonEbtSingleThreadPclOrderPickedForItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9004, ExcelUtils.SINGLE_THREAD_PCL_TC_9004);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9004, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9004_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for non-EBT SingleThread pcl order for item movement after staging from Order LookUp")
    public void validateE2eNonEbtSingleThreadPclOrderStagedForItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9004, ExcelUtils.SINGLE_THREAD_PCL_TC_9004);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9004_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT SingleThread pcl order for item movement after staging from Order LookUp")
    public void validateE2eNonEbtSingleThreadPclOrderPaidForItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9004, ExcelUtils.SINGLE_THREAD_PCL_TC_9004);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9004);
    }
}
