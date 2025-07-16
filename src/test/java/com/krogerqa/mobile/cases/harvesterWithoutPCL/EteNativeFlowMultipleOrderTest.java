package com.krogerqa.mobile.cases.harvesterWithoutPCL;

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
 * Scenario 6 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multiple orders in the same Multi-threaded store using Harvester Native >
 * Stage containers for Multiple orders in the same store using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for Multiple non-EBT Pickup Orders in the same using Ciao Native
 */
public class EteNativeFlowMultipleOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete6_native_part1_multipleOrders", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersSelecting() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1,ExcelUtils.SCENARIO_6_1);
        secondOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2,ExcelUtils.SCENARIO_6_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_6_1, firstOrderTestData);
    }

    @Test(groups = {"ete6_native_part2_multipleOrders", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersStaging() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1,ExcelUtils.SCENARIO_6_1);
        secondOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2,ExcelUtils.SCENARIO_6_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_6_1, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_6_2, secondOrderTestData);
    }

    @Test(groups = {"ete6_native_part3_multipleOrders", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersCheckout() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1,ExcelUtils.SCENARIO_6_1);
        secondOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2,ExcelUtils.SCENARIO_6_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(),PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_6_1, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_6_2, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
