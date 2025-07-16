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
 * Scenario FFILLSVCS-TC-7140 mobile flows
 * Perform Selecting for multiple rush and normal orders in different tile slot.
 * Stage the containers for both the rush orders and normal orders
 * Perform ciao checkout and validate paid status
 */

public class EteNativeFlowMultipleRushOrdersAndNormalOrdersTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_rushOrder_manualBatching"}, description = "Verify selecting for multiple rush orders in same store")
    public void validateE2eMultipleRushAndNormalOrdersSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3); loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        thirdOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        fourthOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple rush orders")
    public void validateEteMultipleRushAndNormalOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3); loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, secondOrderTestData);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple rush and normal orders")
    public void validateEteMultipleRushAndNormalOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3); loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(thirdOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
