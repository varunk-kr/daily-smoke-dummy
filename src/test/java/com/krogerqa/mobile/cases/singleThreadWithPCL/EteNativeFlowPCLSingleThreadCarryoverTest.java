package com.krogerqa.mobile.cases.singleThreadWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-8813 Mobile Flow :
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * During selecting mark 1 container as OOS
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for Non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowPCLSingleThreadCarryoverTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for Non-EBT pcl Single Thread carry over order and mark container as OOS")
    public void validatePclSingleThreadCarryoverTestOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_8813, testOutputData);
    }

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for Non-EBT pcl Single Thread carry order")
    public void validatePclSingleThreadCarryoverTestOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_8813, testOutputData);
    }

    @Test(groups = {"ete_tc_pcl_singleThread_carryover_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for Non-EBT pcl Single Thread carry over order")
    public void validatePclSingleThreadCarryoverTestOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8813,ExcelUtils.SINGLE_THREAD_PCL_TC_8813);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_8813, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
