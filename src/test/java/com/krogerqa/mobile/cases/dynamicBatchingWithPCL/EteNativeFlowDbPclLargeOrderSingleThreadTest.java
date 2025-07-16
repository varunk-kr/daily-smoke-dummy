package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

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
 * Scenario Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded DyB Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbPclLargeOrderSingleThreadTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_largeOrder_native_part1_nonEbt", "ete_native_part1_singleThreaded", "ete_native_part1_nonEbt"}, description = "Verify selecting for db non ebt pcl large order")
    public void validateE2eDbPclLargeOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYB_LARGE_ORDER, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_largeOrder_native_part2_nonEbt", "ete_native_part2_singleThreaded", "ete_native_part2_nonEbt"}, description = "Verify staging for db non ebt pcl large order")
    public void validateE2eDbPclLargeOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYB_LARGE_ORDER, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_largeOrder_native_part3_nonEbt", "ete_native_part3_singleThreaded", "ete_native_part3_nonEbt"}, description = "Verify de-staging and checkout for db non-EBT pcl large order")
    public void validateE2eDbPclLargeOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYB_LARGE_ORDER, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
