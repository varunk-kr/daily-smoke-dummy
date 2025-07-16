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
 * Scenario SINGLE_THREAD_PCL_TC_9002 Mobile Flows -
 * Perform taking over run from In Progress trolleys section using Harvester Native>
 * Stage containers using Harvester Native >
 * Move all items from AM container to new Container via Staging screen using Harvester Native>
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class EteNativeFlowNonEbtSingleThreadPclTakeOverTrolleyTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9002_native_part1_takeOver_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for take over Pcl singleThreaded order")
    public void validateE2eNonEbtPclTakeOverOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_native_part2_takeOver_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify take over trolley for Pcl singleThreaded order")
    public void validateE2eNonEbtPclTakeOverSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        harvesterSelectingAndStaging.takeOverRun(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_native_part3_takeOver_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify staging for take over trolley for Pcl singleThreaded order")
    public void validateE2eNonEbtPclTakeOverOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_9002_native_part4_takeOver_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify de-staging and checkout for take over trolley Pcl singleThreaded order")
    public void validateE2eNonEbtPclTakeOverOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
