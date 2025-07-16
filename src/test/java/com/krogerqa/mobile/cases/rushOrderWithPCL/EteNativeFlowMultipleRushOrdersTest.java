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
 * PCL_SCENARIO_TC_6685 mobile flows
 * Perform Selecting for one trolley which is combined for both order
 * Stage the containers for both the rush orders
 * Perform ciao checkout and paid status
 */
public class EteNativeFlowMultipleRushOrdersTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_6685_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eSelectingBothTrolleys() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_6685_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT pcl rush orders in same store")
    public void validateE2eMultipleOrdersStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_6685_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT pcl rush orders in same store")
    public void validateE2eMultipleOrdersCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6685_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}