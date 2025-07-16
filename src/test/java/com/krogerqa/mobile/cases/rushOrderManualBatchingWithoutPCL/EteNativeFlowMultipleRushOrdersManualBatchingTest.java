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
 * Scenario FFILLSVCS-TC-7123 mobile flows
 * Perform Selecting for multiple rush orders.
 * Stage the containers for both the rush orders
 * Perform ciao checkout and validate paid status
 */
public class EteNativeFlowMultipleRushOrdersManualBatchingTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_tc_7123_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT RUSH orders with different timeslot and CollapseTemperatureAll")
    public void validateE2eSelectingMultipleRushOrders() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingMultipleOrderWithCollapseTempAll(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_tc_7123_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT RUSH orders with different timeslot and CollapseTemperatureAll")
    public void validateE2eMultipleRushOrdersStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.ROMB_SCENARIO_TC_7123_1, secondOrderTestData);
    }

    @Test(groups = {"ete_tc_7123_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT RUSH orders with different timeslot and CollapseTemperatureAll")
    public void validateE2eMultipleRushOrdersCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7123_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
