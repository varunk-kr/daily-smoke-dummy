package com.krogerqa.mobile.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14051 Mobile Flows -
 * For express - Perform Selecting by picking items As Ordered for DyB Pcl single thread store using Harvester Native >
 * For express - Stage Pcl containers using Harvester Native >
 * For express - After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbExpressOrderTC52Test extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_tc14051_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Install and Validate TC52 Notifications in Little Bird App")
    public void validateTC52NotificationForExpressOrder() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        loginUsingOktaSignInWithoutChrome(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }
    @Test(groups = {"ete_db_expressOrder_singleThread_tc14051_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify selecting for db non ebt pcl express order")
    public void validateE2eDbPclExpressOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_tc14051_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify staging for db non ebt pcl express order")
    public void validateE2eDbPclExpressOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
    }
    @Test(groups = {"ete_db_expressOrder_singleThread_tc14051_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl express order")
    public void validateE2eDbPclExpressOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
    private void loginUsingOktaSignInWithoutChrome(String userName, String password) {
        loginNative.LoginOktaSignInForTC52(userName, password);
    }
}
