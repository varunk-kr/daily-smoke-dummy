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
 * Scenario BULK_STAGING_TC_11733 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers, perform item movement from Multiple Frozen to new, reStage using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingPclItemMovementMultipleFrozenToNewAnchorGranularTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_native_part1_nonEbt_itemMovement", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11733, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_native_part2_nonEbt_itemMovement", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11733, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11733_bulkStaging_native_part3_nonEbt_itemMovement", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingItemMovementAnchorGranularOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11733, ExcelUtils.BULK_STAGING_TC_11733);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11733, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
