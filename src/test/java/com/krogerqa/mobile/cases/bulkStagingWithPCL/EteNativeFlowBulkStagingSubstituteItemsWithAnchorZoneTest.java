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
 * Scenario BULK_STAGING_TC_11719 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage rejected containers and complete bulk de-staging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingSubstituteItemsWithAnchorZoneTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_SingleThreadedWithBulkStaging"}, description = "Verify selecting for substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingSubsItemsAndSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_SingleThreadedWithBulkStaging"}, description = "Verify staging  for substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingSubsItemAndStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_SingleThreadedWithBulkStaging"}, description = "Verify checkout  for substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingSubsItemAndCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
