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
 * Scenario FFILLSVCS-TC-7122 mobile flows
 * Perform Selecting for multiple rush orders.
 * Stage the containers for both the rush orders
 * Perform ciao checkout and validate paid status
 */
public class EteNativeFlowMultipleRushOrdersManualBatchingSameSlotTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> lastOrderTestData = new HashMap<>();

    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_tc_7122_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT RUSH orders with Same timeslot and CollapseTemperatureAll")
    public void validateE2eSelectingMultipleRushOrdersWithSameSlot() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingMultipleOrderWithCollapseTempAll(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData, lastOrderTestData);
    }

    @Test(groups = {"ete_tc_7122_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT RUSH orders with Same timeslot and CollapseTemperatureAll")
    public void validateE2eMultipleRushOrdersWithSameSlotStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.ROMB_SCENARIO_TC_7122_2, lastOrderTestData);
    }

    @Test(groups = {"ete_tc_7122_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT RUSH orders with Same timeslot and CollapseTemperatureAll")
    public void validateE2eMultipleRushOrdersWithSameSlotCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7122_2, lastOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
