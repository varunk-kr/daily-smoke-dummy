package com.krogerqa.mobile.cases.harvesterWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.harvesterWithPCL.SmokeEteWebFlowModifyCancelPclTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_SCENARIO_TC_4563 Mobile Flows -
 * Perform Selecting for Modify Order by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers for Modified Order using Harvester Native >
 * After Customer Check-in,cancel the order from Ciao.
 */
public class SmokeEteNativeFlowModifyCancelPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4563_native_part1_modifyCancel_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Modify order")
    public void validateE2eModifyCancelOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((SmokeEteWebFlowModifyCancelPclTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, ExcelUtils.PCL_SCENARIO_TC_4563_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1, ExcelUtils.PCL_SCENARIO_TC_4563_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4563_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_4563_native_part2_modifyCancel_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Modify order")
    public void validateE2eModifyCancelOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((SmokeEteWebFlowModifyCancelPclTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, ExcelUtils.PCL_SCENARIO_TC_4563_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1, ExcelUtils.PCL_SCENARIO_TC_4563_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4563_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_4563_native_part3_modifyCancel_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Modify order")
    public void validateE2eModifyCancelOrderCancelledInCiao() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((SmokeEteWebFlowModifyCancelPclTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, ExcelUtils.PCL_SCENARIO_TC_4563_1_1);
            ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4563_1, ExcelUtils.PCL_SCENARIO_TC_4563_1);
            ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
