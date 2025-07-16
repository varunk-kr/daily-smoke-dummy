package com.krogerqa.mobile.cases.enhancedDynamicBatching;

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
 * Scenario FFILLSVCS-TC-10903 mobile flows
 * Perform Selecting for multiple non ebt orders by marking few items as OOS and shorting few items.
 * Stage the containers for all the orders
 * Perform ciao checkout and validate paid status
 */
public class EteNativeFlowDbPclEnhancedBatchMultipleOrderShortOOSOSFRTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eSelectingMultipleOrdersWithDifferentTimeSlot() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eMultipleOrdersWithDifferentTimeSlotOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR")
    public void validateEBE2eMultipleOrdersWithDifferentTimeSlotOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
