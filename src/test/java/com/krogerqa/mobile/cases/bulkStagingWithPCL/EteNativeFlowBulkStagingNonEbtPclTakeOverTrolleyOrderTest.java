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
 * Scenario BULK_STAGING_TC_11715 Non-Ebt Mobile Flows -
 * Perform PCL Label assigning and exit from trolley
 * Perform take over and continue Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete bulk de-staging and cancel the order from ciao for Pcl Order
 */
public class EteNativeFlowBulkStagingNonEbtPclTakeOverTrolleyOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for pcl bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715,ExcelUtils.BULK_STAGING_TC_11715);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11715, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify take over for pcl bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715,ExcelUtils.BULK_STAGING_TC_11715);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        harvesterSelectingAndStaging.takeOverRun(ExcelUtils.BULK_STAGING_TC_11715, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify staging for pcl bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715,ExcelUtils.BULK_STAGING_TC_11715);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11715, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11715_bulkStaging_take_over_trolley_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify de-staging and checkout for pcl bulk staging take over order")
    public void validateE2eNonEbtPclBulkStagingTakeOverOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11715,ExcelUtils.BULK_STAGING_TC_11715);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11715,testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
