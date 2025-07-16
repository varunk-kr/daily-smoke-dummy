package com.krogerqa.mobile.cases.byob;

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
 * Scenario  BYOB_TC_7461  Non-Ebt Mobile Flows BYOB functionality-
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native and also move items via Picking screen >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowSubstitutionAndNRItemBYOBTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_byob_tc_7461_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BYOB_PCL_TC_7461, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7461_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, testOutputData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_7461, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7461_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_7461, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
