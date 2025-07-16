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
 * Scenario FFILLSVCS-TC-9007 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Stage the containers and verify in Cue
 *  Move all items from Staged RE container to existing Staged RE container
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowItemMovementStagedReToExistingReContainerTest extends BaseTest
{
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9007_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify the selecting for Item Movement order")
    public void validateE2eOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, ExcelUtils.SINGLE_THREAD_PCL_TC_9007);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9007_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for Item Movement order")
    public void validateE2eOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, ExcelUtils.SINGLE_THREAD_PCL_TC_9007);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9007_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for Item Movement order")
    public void validateE2eOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, ExcelUtils.SINGLE_THREAD_PCL_TC_9007);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9007, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}



