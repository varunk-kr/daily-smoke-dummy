package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SINGLE THREAD PCL FFILLSVCS-TC-9003 Web Flows -
 * Submit Non-EBT SingleThread Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is cancelled from ciao in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadPclCancelCiaoTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9003_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify SingleThread pcl order placed for non-EBT Cancel from ciao order")
    public void validateE2eNonEbtSingleThreadPclCancelFromCiaoOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9003);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9003, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9003_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for non-EBT SingleThread Cancel from ciao pcl order")
    public void validateE2eNonEbtSingleThreadPclCancelFromCiaoOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9003, ExcelUtils.SINGLE_THREAD_PCL_TC_9003);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9003, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9003_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for non-EBT SingleThread Cancel from ciao pcl order")
    public void validateE2eNonEbtSingleThreadPclCancelFromCiaoOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9003, ExcelUtils.SINGLE_THREAD_PCL_TC_9003);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9003_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT SingleThread Cancel from ciao pcl order")
    public void validateE2eNonEbtSingleThreadPclCancelFromCiaoOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9003, ExcelUtils.SINGLE_THREAD_PCL_TC_9003);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}
