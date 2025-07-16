package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SINGLE THREAD PCL FFILLSVCS-TC-9001 Web Flows -
 * Submit Non-EBT SingleThread Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * take over the trolley from in progress trolley screen, verify the trolley status in assigning >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 * Note:Execute 2 web testcases before running native
 */
public class EteWebFlowTakeOverTrolleyInAssigningStatusSingleThreadPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> baseOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9001_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify api orders placed in SingleThread Pcl")
    public void validateE2eNonEbtSingleThreadPclOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9001_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_9001_1, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.eteOrderApi(ExcelUtils.SINGLE_THREAD_PCL_TC_9001_1, ExcelUtils.SINGLE_THREAD_PCL_TC_9001, testOutputData);
        baseOrderData = testOutputData;
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9001_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify the second order placed in SingleThread pcl")
    public void validateE2eNewNonEbtPclOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9001);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCueReuseStagedPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9001, ExcelUtils.SINGLE_THREAD_PCL_TC_9001_1, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9001_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order picked for non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9001, ExcelUtils.SINGLE_THREAD_PCL_TC_9001);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9001, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9001_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order staged and checked-in for non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9001, ExcelUtils.SINGLE_THREAD_PCL_TC_9001);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9001_web_part5_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_singleThreaded"}, description = "Verify order picked up and paid for non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9001, ExcelUtils.SINGLE_THREAD_PCL_TC_9001);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9001);
    }
}
