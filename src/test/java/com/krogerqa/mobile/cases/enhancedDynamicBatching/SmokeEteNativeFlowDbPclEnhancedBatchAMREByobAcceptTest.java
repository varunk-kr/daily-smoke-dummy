package com.krogerqa.mobile.cases.enhancedDynamicBatching;

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
 * Scenario FFILLSVCS-TC-10945 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Validate Bag Icons while Picking
 * Stage Pcl containers using Harvester Native and Validate Bag Icon>
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class SmokeEteNativeFlowDbPclEnhancedBatchAMREByobAcceptTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eDbPclByobOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10945, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eDbPclByobOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10945, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10945_byobAccept_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Enhanced Batching db non-EBT pcl byob order")
    public void validateE2eDbPclByobOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10945, ExcelUtils.EB_PCL_TC_10945);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10945, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
