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
 * Scenario SINGLE THREAD PCL Submit order FFILLSVCS-TC-9005 Item movement Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowNonEbtSingleThreadPclItemMovementfrozentonewTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9005_new_ContainerItemMovement_native_part1_nonEbt", "ete_native_part1_submitOrder", "ete_native_part1_singleThreaded"}, description = "Verify selecting for SingleThread PCL order")
    public void validateE2eNonEbtItemMovementPclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, ExcelUtils.SINGLE_THREAD_PCL_TC_9005);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9005_new_ContainerItemMovement_native_part2_nonEbt", "ete_native_part2_submitOrder", "ete_native_part2_singleThreaded"}, description = "Verify staging for SingleThread PCL order")
    public void validateE2eNonEbtItemMovementPclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, ExcelUtils.SINGLE_THREAD_PCL_TC_9005);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9005_new_ContainerItemMovement_native_part3_nonEbt", "ete_native_part3_submitOrder", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for SingleThread PCL order")
    public void validateE2eNonEbtItemMovementPclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, ExcelUtils.SINGLE_THREAD_PCL_TC_9005);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9005, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}