package com.krogerqa.mobile.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12139 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl OS store using Harvester Native>
 * Stage Pcl containers using Harvester Native using bulk staging >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowRushOrderBulkStagingTc52Test extends BaseTest {

    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    static HashMap<String, String> testOutputData = new HashMap<>();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12139_native_part1_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Install and Validate TC52 Notifications in Little Bird App")
    public void validateTC52NotificationForPclOversize() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_native_part2_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify Selecting for Non EBT pcl oversize Rush order for TC52 Notification")
    public void validateE2eTC52NonEbtSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_native_part3_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify staging for non ebt pcl oversize rush order")
    public void validateE2eTC52NonEbtPclStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_native_part4_nonEbt", "ete_native_part5_nonEbt", "ete_native_part5_multiThreaded"}, description = "Verify de-staging and checkout for non ebt pcl oversize rush order")
    public void validateE2eTC52NonEbtCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
    }

    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}