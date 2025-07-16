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
 * Scenario DB_EXPRESS_ORDER_TC_14047 Mobile Flows -
 * For express and normal order - Perform Selecting by picking items As Ordered for DyB Pcl single thread store using Harvester Native >
 * For express and normal order - Stage Pcl containers using Harvester Native >
 * For express and normal order  - After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbExpressOrderPureSingleThreadTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_14047_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for express and normal order in DyB Single thread store")
    public void validateE2eDbPclExpressOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, ExcelUtils.DB_EXPRESS_ORDER_TC_14047);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14047_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for express and normal order in DyB Single thread store")
    public void validateE2eDbPclExpressOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, ExcelUtils.DB_EXPRESS_ORDER_TC_14047);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14047_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for express and normal order in DyB Single thread store")
    public void validateE2eDbPclExpressOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, ExcelUtils.DB_EXPRESS_ORDER_TC_14047);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14047, firstOrderTestData);
        ciaoDestagingAndCheckout.changeStoreSetup();
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14047_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}