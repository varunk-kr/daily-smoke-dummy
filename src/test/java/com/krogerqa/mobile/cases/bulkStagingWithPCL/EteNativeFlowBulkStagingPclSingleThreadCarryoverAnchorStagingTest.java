package com.krogerqa.mobile.cases.bulkStagingWithPCL;

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
 * Scenario BULK_STAGING_TC_11723_ANCHOR Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered  for Single-threaded Pcl store using Harvester Native >
 * Mark 1 container as OOS during picking >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete bulk de-staging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingPclSingleThreadCarryoverAnchorStagingTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDeStagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11723_bulkStaging_anchor_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for bulk staging carry over order and mark container as OOS")
    public void validateE2eNonEbtPclBulkStagingCarryOverOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, ExcelUtils.BULK_STAGING_TC_11723_ANCHOR);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11723_bulkStaging_anchor_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for bulk staging carry over order")
    public void validateE2eNonEbtPclCarryOverOrderBulkStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, ExcelUtils.BULK_STAGING_TC_11723_ANCHOR);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11723_bulkStaging_anchor_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for bulk staging carry over order")
    public void validateE2eNonEbtPclBulkStagingCarryOverOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, ExcelUtils.BULK_STAGING_TC_11723_ANCHOR);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDeStagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11723_ANCHOR, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
