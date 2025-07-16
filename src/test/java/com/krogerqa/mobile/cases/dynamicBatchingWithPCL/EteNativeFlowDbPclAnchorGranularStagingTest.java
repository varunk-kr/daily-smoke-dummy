package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

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
 * Scenario DYNAMIC_BATCH_TC_9854 Mobile Flows
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native for dynamic batching enabled store >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Non-Pcl Order
 */

public class EteNativeFlowDbPclAnchorGranularStagingTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Dynamic batching non-EBT anchor granular pcl order")
    public void validateE2eDbPclAnchorGranularStagingOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for for Dynamic batching non-EBT anchor granular pcl order")
    public void validateE2eDbPclAnchorGranularStagingOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9854_anchorGranularStaging_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Dynamic batching non-EBT anchor granular pcl order")
    public void validateE2eDbPclAnchorGranularStagingOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9854, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
