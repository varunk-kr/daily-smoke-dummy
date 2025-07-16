package com.krogerqa.mobile.cases.rushOrderWithPCL;

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
     * Scenario RUSH_ORDER_SCENARIO_TC_6706 Non-Ebt Mobile Flows -
     * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
     * Stage Pcl containers using Harvester Native >
     * After Customer Check-in, Destage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
     */
    public class EteNativeFlowRushOrderNonEBTPclTest extends BaseTest {

        static HashMap<String, String> testOutputData = new HashMap<>();
        CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
        HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
        WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
        LoginNative loginNative = LoginNative.getInstance();

        @Test(groups = {"ete_rush_order_tc_6706_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt rush order")
        public void validateE2eNonEbtpclRushOrderSelecting() {
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6706, testOutputData);
        }

        @Test(groups = {"ete_rush_order_tc_6706_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt rush order")
        public void validateE2eNonEbtPclRushOrderStaging() {
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6706, testOutputData);
        }

        @Test(groups = {"ete_rush_order_tc_6706_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
        public void validateE2eNonEbtPclRushOrderCheckout() {
            loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6706, testOutputData);
        }

        private void loginUsingOktaSignIn(String userName, String password) {
            welcomeToChromePage.acceptChromeTerms();
            loginNative.LoginOktaSignIn(userName, password);
        }
    }