package com.krogerqa.mobile.cases.serviceCounterIntegration;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SC_INTEGRATION_TC_14431 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class SmokeEteNativeFlowScIntegrationBauTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_scIntegration_tc_14431_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt sc integration order")
    public void validateE2eNonEbtPclScIntegrationOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
    }

    @Test(groups = {"ete_scIntegration_tc_14431_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt sc integration order")
    public void validateE2eNonEbtPclScIntegrationOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
        harvesterNativeOrderAdjustmentPage.pickAllNrItems(testOutputData);
    }

    @Test(groups = {"ete_scIntegration_tc_14431_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt sc integration order")
    public void validateE2eNonEbtPclScIntegrationOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SC_INTEGRATION_TC_14431, ExcelUtils.SC_INTEGRATION_TC_14431);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SC_INTEGRATION_TC_14431, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
