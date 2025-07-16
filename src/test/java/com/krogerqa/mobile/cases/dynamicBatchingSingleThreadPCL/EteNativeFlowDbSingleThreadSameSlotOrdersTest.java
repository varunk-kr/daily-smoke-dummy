package com.krogerqa.mobile.cases.dynamicBatchingSingleThreadPCL;

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
 * Scenario DB_PCL_TC-12728 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-thread Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbSingleThreadSameSlotOrdersTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_12728_singleThread_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for db non ebt pcl order")
    public void validateE2eDbPclOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12728, ExcelUtils.DB_SINGLE_THREAD_12728);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_SINGLE_THREAD_12728, firstOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_12728_singleThread_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for db non ebt pcl order")
    public void validateE2eDbPclOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12728, ExcelUtils.DB_SINGLE_THREAD_12728);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_SINGLE_THREAD_12728, firstOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_12728_singleThread_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl order")
    public void validateE2eDbPclOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12728, ExcelUtils.DB_SINGLE_THREAD_12728);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_SINGLE_THREAD_12728, firstOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
