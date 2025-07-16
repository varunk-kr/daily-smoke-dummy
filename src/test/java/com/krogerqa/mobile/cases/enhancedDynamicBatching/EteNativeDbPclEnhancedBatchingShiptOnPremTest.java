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
 * Scenario FFILLSVCS-TC-10937 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Non Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for Snap-EBT Order using Ciao Native for Non Pcl Order
 */

public class EteNativeDbPclEnhancedBatchingShiptOnPremTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10937_delivery_onPrem_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10937_delivery_onPrem_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10937_delivery_onPrem_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}