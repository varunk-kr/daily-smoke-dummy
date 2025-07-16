package com.krogerqa.mobile.cases.anchorStagingWithPCL;

import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario ANCHOR_STAGING_TC_5314 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items Mark one full container as OOS in Single thread  Pcl store using Harvester Native >
 * Move SOME items from Picked Frozen to Existing Ambient Container through Order Lookup screen >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, De stage containers and complete bulk de staging checkout for non EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowAnchorStagingPCLCancelOrderAfterStagingOneContainerTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_5314_anchor_staging_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify anchor staging during selecting")
    public void validateE2eNonEbtPclAnchorStagingCancelAfterStagingOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_STAGING_TC_5314, ExcelUtils.ANCHOR_STAGING_TC_5314);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.ANCHOR_STAGING_TC_5314, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5314_anchor_staging_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging is done using anchor zones for the order")
    public void validateE2eNonEbtPclAnchorStagingCancelAfterStagingOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_STAGING_TC_5314, ExcelUtils.ANCHOR_STAGING_TC_5314);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.ANCHOR_STAGING_TC_5314, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}