package com.krogerqa.mobile.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12216 Non-Ebt Mobile Flows -
 * Perform Selecting by assigning pcl and performing picking As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Before staging, move All items from Picked to new container from Staging screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowNonEbtPickedToNewTest  extends BaseTest {
     static HashMap<String, String> testOutputData = new HashMap<>();
        CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
        HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
        WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
        LoginNative loginNative = LoginNative.getInstance();

        @Test(groups = {"ete_pclOversize_tc_12216_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize order with item movement flow")
        public void validateE2eNonEbtHotItemsPclOversizeOrderSelecting() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12216, ExcelUtils.PCL_OVERSIZE_TC_12216);
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12216, testOutputData);
        }

        @Test(groups = {"ete_pclOversize_tc_12216_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify items moved from Picked to New container and staging for pcl oversize order")
        public void validateE2eNonEbtHotItemsPclOversizeOrderStaging() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12216, ExcelUtils.PCL_OVERSIZE_TC_12216);
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12216, testOutputData);
        }

        @Test(groups = {"ete_pclOversize_tc_12216_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize order with item movement flow")
        public void validateE2eNonEbtHotItemsPclOversizeOrderCheckout() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12216, ExcelUtils.PCL_OVERSIZE_TC_12216);
            loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12216, testOutputData);
        }

        private void loginUsingOktaSignIn(String userName, String password) {
            welcomeToChromePage.acceptChromeTerms();
            loginNative.LoginOktaSignIn(userName, password);
        }
    }