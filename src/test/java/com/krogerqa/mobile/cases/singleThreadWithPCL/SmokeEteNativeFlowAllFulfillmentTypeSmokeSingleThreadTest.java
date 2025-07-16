package com.krogerqa.mobile.cases.singleThreadWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-9000 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for Non-EBT Order using Ciao Native for Pcl Order
 */

public class SmokeEteNativeFlowAllFulfillmentTypeSmokeSingleThreadTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_SingleThread_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for various fulfillment types Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeTestOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_native_SingleThread_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for various fulfillment types Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeTestOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_native_SingleThread_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for various fulfillment types Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeTestOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
