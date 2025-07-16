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
 * Scenario SINGLE THREAD PCL FFILLSVCS-TC-9724 Non-Ebt Mobile Flows -
 * Perform Selecting for Single-threaded Pcl store using Harvester Native >
 * Perform item movement from Picked to picked
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowNonEbtSingleThreadPclItemMoveBYOBRushTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9724_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for item movement from picked to picked non ebt SingleThread PCL order")
    public void validateE2eNonEbtPclItemMoveBYOBRushOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, ExcelUtils.SINGLE_THREAD_PCL_TC_9724);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9724_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for item movement from picked to picked non ebt SingleThread PCL order")
    public void validateE2eNonEbtPclItemMoveBYOBRushOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, ExcelUtils.SINGLE_THREAD_PCL_TC_9724);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9724_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for item movement from picked to picked non ebt SingleThread PCL order")
    public void validateE2eNonEbtPclItemMoveBYOBRushOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, ExcelUtils.SINGLE_THREAD_PCL_TC_9724);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9724, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
