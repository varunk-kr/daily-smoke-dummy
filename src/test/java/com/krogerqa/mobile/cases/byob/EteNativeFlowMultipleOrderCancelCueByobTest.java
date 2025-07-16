package com.krogerqa.mobile.cases.byob;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.ciao.CiaoStoreLocationSetupPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.mobile.ui.pages.toggle.NativeTogglePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BYOB-TC-7459 Mobile Flows -
 * Perform Selecting for all 3 orders by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Validate Bag Icons while Picking
 * Stage Pcl containers using Harvester Native and Validate Bag Icon>
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowMultipleOrderCancelCueByobTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    CiaoStoreLocationSetupPage ciaoStoreSetupPage = CiaoStoreLocationSetupPage.getInstance();
    NativeTogglePage nativeTogglePage = NativeTogglePage.getInstance();

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db non ebt pcl order")
    public void validateE2eDbPclOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459, ExcelUtils.BYOB_PCL_TC_7459);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BYOB_PCL_TC_7459, firstOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db non ebt pcl order")
    public void validateE2eDbPclOrderStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.BYOB_PCL_TC_7459_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_7459_1, secondOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for db non ebt pcl order")
    public void validateE2eDbPclOrderStagingThirdOrder() {
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.BYOB_PCL_TC_7459_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_7459_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_native_part4", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl order")
    public void validateE2eDbPclOrderCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.BYOB_PCL_TC_7459_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.BYOB_PCL_TC_7459_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoStoreSetupPage.submitStoreLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        nativeTogglePage.handleToggle(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_7459_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_7459_2, thirdOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
