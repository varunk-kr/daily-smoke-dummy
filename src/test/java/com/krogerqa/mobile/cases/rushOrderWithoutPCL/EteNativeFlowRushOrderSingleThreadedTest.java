package com.krogerqa.mobile.cases.rushOrderWithoutPCL;

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
 * Scenario RUSH_ORDER_SCENARIO_TC_6696 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Non Pcl store using Harvester Native >
 * Stage Non Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout forEBT Order using Ciao Native for Non Pcl Order
 */
public class EteNativeFlowRushOrderSingleThreadedTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_rush_order_tc_6696_native_part1_nonEbt", "ete_native_part1_singleThreaded_rushOrder_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6696_native_part2_nonEbt", "ete_native_part2_singleThreaded_rushOrder_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6696_native_part3_nonEbt", "ete_native_part3_singleThreaded_rushOrder_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6696, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}