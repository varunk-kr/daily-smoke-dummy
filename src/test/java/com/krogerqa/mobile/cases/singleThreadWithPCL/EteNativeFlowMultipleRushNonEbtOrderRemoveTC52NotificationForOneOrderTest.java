package com.krogerqa.mobile.cases.singleThreadWithPCL;

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
 * Scenario TC-9873 Mobile Flows -
 * Submit 2 Non-EBT Pcl Pickup rush and normal order in Kroger.com for single threaded store and cancel one rush order >
 * Verify the rush order Label and count in cue.
 * Verify  tc52 notifications received and removed notification for cancelled order in little bird application.
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting and Stage the rush orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class EteNativeFlowMultipleRushNonEbtOrderRemoveTC52NotificationForOneOrderTest extends BaseTest {
    static MobileCommands mobileCommands = new MobileCommands();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Install and Validate non ebt single Thread pcl TC52 Remove Notifications in Little Bird App")
    public void validateRemoveTC52Notification() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_1, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_1);
        tc52NotificationValidationPage.validateTC52Notification(firstOrderTestData);
        tc52NotificationValidationPage.validateRemoveTC52Notification(secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify Selecting for non ebt single Thread pcl Rush order for TC52 remove Notification")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        mobileCommands.browserBack();
        harvesterSelectingAndStaging.changeStoreSetup();
        thirdOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for non ebt single Thread pcl rush orderTC52 remove Notification")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part4", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt single Thread pcl rush order TC52 remove Notification")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersCheckOut() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(thirdOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
