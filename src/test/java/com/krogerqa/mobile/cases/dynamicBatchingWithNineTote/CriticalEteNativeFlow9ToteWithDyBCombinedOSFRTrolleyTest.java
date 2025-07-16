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
 * Scenario FFILLSVCS-TC-13617 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native>
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class CriticalEteNativeFlow9ToteWithDyBCombinedOSFRTrolleyTest extends BaseTest {


    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_dyb_9tote_tc_15068_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 9 tote batching")
    public void validateE2eDyBNineToteOrderCombinedOSFRTrolleySelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15068_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 9 tote batching")
    public void validateE2eDyBNineToteOrderCombinedOSFRTrolleyStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15068_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for 9 tote batching")
    public void validateE2eDyBNineToteOrderCombinedOSFRTrolleyCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15068, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
