package com.krogerqa.mobile.cases.dynamicBatchingWithoutPCL;


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
 * Scenario DYNAMIC_BATCH_TC_5583 Mobile Flows
 * Perform Selecting for multiple orders by picking items As Ordered for Multi-threaded Non-Pcl store using Harvester Native for dynamic batching enabled store >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Non-Pcl Order
 */
public class EteNativeFlowDynamicBatchingMultipleOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_tc_5583_multipleOrder_native_part1_snapEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple orders for Dynamic batching snapEBT Orders")
    public void validateEteDynamicBatchingMultipleOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, ExcelUtils.DYNAMIC_BATCH_TC_5583_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, ExcelUtils.DYNAMIC_BATCH_TC_5583_2);
        secondOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, secondOrderTestData);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, ExcelUtils.DYNAMIC_BATCH_TC_5583_3);
        thirdOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5583_multipleOrder_native_part2_snapEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple orders for Dynamic batching snapEBT Orders")
    public void validateEteDynamicBatchingMultipleOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, ExcelUtils.DYNAMIC_BATCH_TC_5583_2);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, secondOrderTestData);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, ExcelUtils.DYNAMIC_BATCH_TC_5583_3);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5583_multipleOrder_native_part3_snapEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple orders for Dynamic batching snapEBT Orders")
    public void validateEteDynamicBatchingMultipleOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, thirdOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
