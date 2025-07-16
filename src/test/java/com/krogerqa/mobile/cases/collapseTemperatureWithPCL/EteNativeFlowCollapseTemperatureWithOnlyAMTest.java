package com.krogerqa.mobile.cases.collapseTemperatureWithPCL;

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
 * Scenario pcl_with_collapse_temp_tc_5500 Mobile Flows -
 * Pick all other items as ordered>
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowCollapseTemperatureWithOnlyAMTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_collapseTemp_tc_5500_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl order placed collapsed temperature with only AM items non-EBT")
    public void validatePclCollapseTempWithAMRegularOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5500, ExcelUtils.PCL_SCENARIO_TC_5500);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_5500, testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5500_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for pcl order placed collapsed temperature with only AM items non-EBT")
    public void validatePclCollapseTempWithAMRegularOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5500, ExcelUtils.PCL_SCENARIO_TC_5500);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_5500, testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5500_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non-EBT pcl order with collapse temperature")
    public void validatePclCollapseTempWithAMRegularOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5500, ExcelUtils.PCL_SCENARIO_TC_5500);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_5500, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
