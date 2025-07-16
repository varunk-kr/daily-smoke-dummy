package com.krogerqa.mobile.cases.dynamicBatchingWeightedTrolley;

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
 * Scenario DB_WEIGHTED_TROLLEY FFILLSVCS-TC-13140 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native and Validate Bag Icon>
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowDybWeightedTrolleyAMREContainersTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_weightedTrolley_13140_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db non ebt pcl Weighted Trolley")
    public void validateE2eDbWeightedTrolleyPclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, testOutputData);
    }

    @Test(groups = {"ete_db_weightedTrolley_13140_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db non ebt pcl Weighted Trolley")
    public void validateE2eDbWeightedTrolleyPclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, testOutputData);
    }

    @Test(groups = {"ete_db_weightedTrolley_13140_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl Weighted Trolley")
    public void validateE2eDbWeightedTrolleyPclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}