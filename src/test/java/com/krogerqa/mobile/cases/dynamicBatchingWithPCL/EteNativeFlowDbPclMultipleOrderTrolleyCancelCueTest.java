package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.ciao.CiaoStoreLocationSetupPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-9514 Mobile Flows -
 * Perform Selecting by picking items As Ordered for not cancelled order in Multi-threaded Pcl store using Harvester Native >
 * Move few items from picked to new container through container look-up screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbPclMultipleOrderTrolleyCancelCueTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    CiaoStoreLocationSetupPage ciaoStoreSetupPage = CiaoStoreLocationSetupPage.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9514_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eSelectingFirstTrolley() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9514_native_part2_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9514_native_part3_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoStoreSetupPage.submitStoreLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9514_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
