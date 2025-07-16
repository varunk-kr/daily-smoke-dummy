package com.krogerqa.mobile.cases.pclOversize;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeSelectingPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_OS_PROD_DEFECT_MULTI_ORDERS Non-Ebt Mobile Flows -
 * Perform Selecting of one container by assigning pcl and performing picking for 1st trolley for Multi-threaded Pcl store using Harvester Native >
 * reuse the same pcl label for picking 2nd trolley
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPclOSProdDefectMultipleOrdersTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeSelectingPage harvesterNativeSelectingPage = HarvesterNativeSelectingPage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 1st trolley for pcl oversize multiOrder trolley")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderFirstTrolleySelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, firstOrderTestData);
        harvesterNativeSelectingPage.updateMapsForSecondOrderPCLOS(firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 1st pcl oversize multiOrder trolley")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderFirstTrolleyStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, firstOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_native_part3_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 2nd pcl oversize multiOrder trolley")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderSecondTrolleySelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_native_part4_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 2nd pcl oversize multiOrder trolley")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderSecondOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_native_part5_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize for all containers")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
