package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-9499 Mobile Flows -
 * Perform Selecting by picking items for both orders as Ordered for Multi-threaded DB Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDBPclAllFulfilmentTypeTest extends BaseTest {

    static HashMap<String, String> firstOrderData = new HashMap<>();
    static HashMap<String, String> secondOrderData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();


    @Test(groups = {"ete_db_pcl_tc_9499_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db non ebt pcl order")
    public void validateE2eNativeDbAllFulfilmentTypeSelecting() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db non ebt pcl order")
    public void validateE2eNativeDbAllFulfilmentTypeStaging() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, secondOrderData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl order")
    public void validateE2eNativeDbAllFulfilmentTypeCheckout() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, secondOrderData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
