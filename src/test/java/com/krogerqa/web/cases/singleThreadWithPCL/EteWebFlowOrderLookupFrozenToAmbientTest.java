package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-9009 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowOrderLookupFrozenToAmbientTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9009_newContainerMovement_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for existing container movement from OrderLookup non-EBT pcl order")
    public void validateNewFrozenContainerMovementLookupScreenOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9009);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9009, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9009_newContainerMovement_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for existing container movement from OrderLookup non-EBT pcl order")
    public void validateNewFrozenContainerMovementLookupScreenOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9009, ExcelUtils.SINGLE_THREAD_PCL_TC_9009);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9009, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9009_newContainerMovement_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for existing container movement from OrderLookup non-EBT pcl order")
    public void validateNewFrozenContainerMovementLookupScreenOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9009, ExcelUtils.SINGLE_THREAD_PCL_TC_9009);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9009_newContainerMovement_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for existing container movement from OrderLookup non-EBT pcl order")
    public void validateNewFrozenContainerMovementLookupScreenOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9009, ExcelUtils.SINGLE_THREAD_PCL_TC_9009);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9009);
    }
}
