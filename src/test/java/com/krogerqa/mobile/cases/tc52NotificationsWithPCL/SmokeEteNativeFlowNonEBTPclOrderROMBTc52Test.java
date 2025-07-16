package com.krogerqa.mobile.cases.tc52NotificationsWithPCL;

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
 * Scenario RUSH_ORDER_TC52_TC_7114 Non-Ebt Mobile Flows -
 * Validate notifications in Little Bird Application
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class SmokeEteNativeFlowNonEBTPclOrderROMBTc52Test extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_native_part1", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Install and Validate TC52 Notifications in Little Bird App")
    public void validateTC52Notification() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_native_part2", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify Selecting for Non EBT Rush order for TC52 Notification")
    public void validateE2eTC52NonEbtSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_native_part3", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eTC52NonEbtPclStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_native_part4", "ete_native_part5_nonEbt", "ete_native_part5_multiThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eTC52NonEbtCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}