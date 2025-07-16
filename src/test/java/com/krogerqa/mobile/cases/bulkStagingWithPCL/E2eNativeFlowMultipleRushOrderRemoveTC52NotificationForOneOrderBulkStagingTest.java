package com.krogerqa.mobile.cases.bulkStagingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.littleBirdApp.TC52NotificationValidationPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario TC-11755 Mobile Flows -
 * Submit 2 Non-EBT Pcl Pickup rush and normal order in Kroger.com for single threaded store and cancel one rush order >
 * Verify the rush order Label and count in cue.
 * Verify  tc52 notifications received and removed notification for cancelled order in little bird application.
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting and Stage the rush orders.
 * After Bulk Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class E2eNativeFlowMultipleRushOrderRemoveTC52NotificationForOneOrderBulkStagingTest extends BaseTest {
    static MobileCommands mobileCommands = new MobileCommands();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Install and Validate non ebt bulk staging order TC52 Remove Notifications in Little Bird App")
    public void validateRemoveTC52Notification() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_1, ExcelUtils.BULK_STAGING_TC_11755_1);
        tc52NotificationValidationPage.validateTC52Notification(firstOrderTestData);
        tc52NotificationValidationPage.validateRemoveTC52Notification(secondOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify Selecting for non ebt bulk staging Rush order for TC52 remove Notification")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        mobileCommands.browserBack();
        harvesterSelectingAndStaging.changeStoreSetup();
        thirdOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for non ebt bulk staging rush orderTC52 remove Notification")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_native_part4", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt bulk staging rush order TC52 remove Notification")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersCheckOut() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(thirdOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
