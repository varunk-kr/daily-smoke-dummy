package com.krogerqa.mobile.cases.rushOrderManualBatchingWithoutPCL;

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
 * Scenario RUSH_ORDER_SCENARIO_TC_7120 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Non Pcl store using Harvester Native >
 * Stage Non Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout forEBT Order using Ciao Native for Non Pcl Order
 */
public class EteNativeFlowMultipleRushOrdersInSameSlotTest extends BaseTest {

    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();

    @Test(groups = {"ete_romb_order_tc_7120_native_part1_nonEbt", "ete_native_part1_multiThreaded_rushOrder_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
    }

    @Test(groups = {"ete_romb_order_tc_7120_native_part2_nonEbt", "ete_native_part2_multiThreaded_rushOrder_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, secondOrderTestData);
    }

    @Test(groups = {"ete_romb_order_tc_7120_native_part3_nonEbt", "ete_native_part3_multiThreaded_rushOrder_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eNonEbtNonPclRushOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7120_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}