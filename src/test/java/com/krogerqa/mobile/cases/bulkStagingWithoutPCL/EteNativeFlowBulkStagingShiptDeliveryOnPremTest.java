package com.krogerqa.mobile.cases.bulkStagingWithoutPCL;

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
 * Scenario BULK_STAGING_TC_11776 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging checkout for non EBT Order using Ciao Native for Order
 */
public class EteNativeFlowBulkStagingShiptDeliveryOnPremTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for shipt delivery on prem bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for shipt delivery on prem bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for shipt delivery on prem bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
