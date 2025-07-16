package com.krogerqa.mobile.cases.singleThreadWithPCL;

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
 * Scenario TC_52_NOTIFICATION_TC_9872 Non-Ebt Mobile Flows -
 * Validate notifications in Little Bird Application
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowNonEBTSingleThreadPclOrderTc52Test extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_tc52_notification_tc_9872_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Setting configuration App for TC52 Notification")
    public void validateConfigurationSetup() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }


    @Test(groups = {"ete_tc52_notification_tc_9872_native_part2", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify Selecting for Non EBT Rush order for TC52 Notification")
    public void validateE2eTC52NonEbtSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
    }

    @Test(groups = {"ete_tc52_notification_tc_9872_native_part3", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eTC52NonEbtPclStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
    }

    @Test(groups = {"ete_tc52_notification_tc_9872_native_part4", "ete_native_part5_nonEbt", "ete_native_part5_singleThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eTC52NonEbtCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}