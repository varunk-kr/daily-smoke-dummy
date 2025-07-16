package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * Scenario 13 Mobile Flows -
 * Perform Shipt Delivery Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class SmokeEteNativeFlowShiptDeliveryTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete13_native_part1_nonEbt_shiptDelivery", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Shipt Delivery order")
    public void validateE2eShiptDeliveryOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13,ExcelUtils.SCENARIO_13);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SCENARIO_13, testOutputData);
    }

    @Test(groups = {"ete13_native_part2_nonEbt_shiptDelivery", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Shipt Delivery order")
    public void validateE2eShiptDeliveryOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13,ExcelUtils.SCENARIO_13);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SCENARIO_13, testOutputData);
    }

    @Test(groups = {"ete13_native_part3_nonEbt_shiptDelivery", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Shipt Delivery order")
    public void validateE2eShiptDeliveryOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_13,ExcelUtils.SCENARIO_13);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(),PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_13, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
