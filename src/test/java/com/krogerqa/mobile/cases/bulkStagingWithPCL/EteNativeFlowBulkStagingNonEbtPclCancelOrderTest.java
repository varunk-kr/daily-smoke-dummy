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
 * Scenario BULK_STAGING_TC_11725 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging and cancel the order from ciao for Pcl Order
 */
public class EteNativeFlowBulkStagingNonEbtPclCancelOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11725_bulkStaging_cancel_order_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for pcl bulk staging cancel order")
    public void validateE2eNonEbtPclBulkStagingCancelOrderOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11725,ExcelUtils.BULK_STAGING_TC_11725);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11725, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11725_bulkStaging_cancel_order_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for pcl bulk staging cancel order")
    public void validateE2eNonEbtPclBulkStagingCancelOrderOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11725,ExcelUtils.BULK_STAGING_TC_11725);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11725, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11725_bulkStaging_cancel_order_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for pcl bulk staging cancel order")
    public void validateE2eNonEbtPclBulkStagingCancelOrderOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11725,ExcelUtils.BULK_STAGING_TC_11725);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
