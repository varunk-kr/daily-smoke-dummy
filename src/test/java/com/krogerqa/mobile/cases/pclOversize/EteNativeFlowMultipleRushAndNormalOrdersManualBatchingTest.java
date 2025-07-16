package com.krogerqa.mobile.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12132 Non-Ebt Mobile Flows -
 * For all 2 normal and 2 rush orders created, Perform Selecting by assigning pcl >
 * Perform picking As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native in staging zones >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowMultipleRushAndNormalOrdersManualBatchingTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12132_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderSelecting() {
        getTestData(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        thirdOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12132_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderStaging() {
        getTestData(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12132_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderCheckout() {
        getTestData(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12132_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(thirdOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(fourthOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12132_3, fourthOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }

    public void getTestData(String sheetName1, String sheetName2, String sheetName3, String sheetName4) {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132, sheetName1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_1, sheetName2);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_2, sheetName3);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_3, sheetName4);
    }
}
