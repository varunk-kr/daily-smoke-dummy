package com.krogerqa.mobile.cases.enhancedDynamicBatching;

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
 * Scenario FFILLSVCS-TC-10902 mobile flows
 * Perform Selecting by marking service counter item as not ready and picking rest of the items As Ordered using Harvester Native.
 * Stage the containers for all the orders
 * Perform ciao checkout and validate paid status
 */
public class EteNativeFlowDbPclEnhancedBatchMultipleOrderNRTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eSelectingMultipleOrdersWithDifferentTimeSlot() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eMultipleOrdersWithDifferentTimeSlotOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eMultipleOrdersWithDifferentTimeSlotOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
