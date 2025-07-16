package com.krogerqa.mobile.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14046 Mobile Flows -
 * Perform Selecting by picking items As Ordered for DyB Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbSingleThreadExpressOrderCancelOrdersTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_cancelOrder_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for db non ebt pcl express order")
    public void validateE2eDbPclExpressOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, ExcelUtils.DB_EXPRESS_ORDER_TC_14046);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, firstOrderTestData);
     }

    @Test(groups = {"ete_db_expressOrder_singleThread_cancelOrder_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for db non ebt pcl express order")
    public void validateE2eDbPclExpressOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, ExcelUtils.DB_EXPRESS_ORDER_TC_14046);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, firstOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_cancelOrder_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl express order")
    public void validateE2eDbPclExpressOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, ExcelUtils.DB_EXPRESS_ORDER_TC_14046);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14046, firstOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
