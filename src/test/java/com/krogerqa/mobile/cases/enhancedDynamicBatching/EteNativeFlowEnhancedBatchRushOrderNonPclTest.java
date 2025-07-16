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
 * Scenario EB_NON_PCL_TC_10939 Mobile Flows -
 * Perform Selecting by picking items as Ordered for Multi-threaded DB Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowEnhancedBatchRushOrderNonPclTest extends BaseTest {

    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_enhancedBatchNonpcl_cancel_RushOrder_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db rush order non ebt pcl order with OSFR combiantion")
    public void validateE2eNativeEnhancedBatchingTrolleySelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10939_1, ExcelUtils.EB_NON_PCL_TC_10939_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.EB_NON_PCL_TC_10939_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatchNonpcl_cancel_RushOrder_native_part2", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eNativeEnhancedBatchingTrolleyStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10939_1, ExcelUtils.EB_NON_PCL_TC_10939_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.EB_NON_PCL_TC_10939_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatchNonpcl_cancel_RushOrder_native_part3", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eNativeEnhancedBatchingTrolleyCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10939_1, ExcelUtils.EB_NON_PCL_TC_10939_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_NON_PCL_TC_10939_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
