package com.krogerqa.mobile.cases.bulkStagingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.littleBirdApp.TC52NotificationValidationPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11731 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Move SOME items from Picked Frozen to Existing Ambient Container through Order Lookup screen >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingTc52Test extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Setting configuration App for TC52 Notification")
    public void validateConfigurationSetup() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_native_part2_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify Selecting for Non EBT Rush order for TC52 Notification")
    public void validateE2eTC52NonEbtSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_native_part3_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eTC52NonEbtPclStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_native_part4_nonEbt", "ete_native_part5_nonEbt", "ete_native_part5_singleThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eTC52NonEbtCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
