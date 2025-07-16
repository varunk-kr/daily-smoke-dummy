package com.krogerqa.mobile.cases.dynamicBatchingWithNineTote;

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
 * Scenario FFILLSVCS-TC-15083 Mobile Flows -
 * Validate when 18+ OS items are present and DB OS+ is disabled in 9 tote plus store.
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native and Validate Bag Icon>
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbNineToteWithOSPlusDisabledTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_dyb_9tote_tc_15083_native_part1"}, description = "Verify selecting for 9 tote batching when OS plus is disabled")
    public void validateE2eDbPclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15083_native_part2"}, description = "Verify staging for 9 tote batching when OS plus is disabled")
    public void validateE2eDbPclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15083_native_part3"}, description = "Verify de-staging and checkout for 9 tote batching when OS plus is disabled")
    public void validateE2eDbPclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15083, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
