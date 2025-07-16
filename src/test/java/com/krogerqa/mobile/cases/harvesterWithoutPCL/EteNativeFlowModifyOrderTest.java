package com.krogerqa.mobile.cases.harvesterWithoutPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.harvesterWithoutPCL.EteWebFlowModifyOrderTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 11 Mobile Flows -
 * Perform Selecting for Modify Order by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers for Modified Order using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Modify Order using Ciao Native
 */
public class EteNativeFlowModifyOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete11_native_part1_nonEbt_modifyOrder", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Modify order")
    public void validateE2eModifyOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1_1, ExcelUtils.SCENARIO_11_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_11_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1, ExcelUtils.SCENARIO_11_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_11_1, testOutputData);
        }
    }

    @Test(groups = {"ete11_native_part2_nonEbt_modifyOrder", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Modify order")
    public void validateE2eModifyOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1_1, ExcelUtils.SCENARIO_11_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_11_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1, ExcelUtils.SCENARIO_11_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_11_1, testOutputData);
        }
    }

    @Test(groups = {"ete11_native_part3_nonEbt_modifyOrder", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Modify order")
    public void validateE2eModifyOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1_1, ExcelUtils.SCENARIO_11_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_11_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_11_1, ExcelUtils.SCENARIO_11_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_11_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
