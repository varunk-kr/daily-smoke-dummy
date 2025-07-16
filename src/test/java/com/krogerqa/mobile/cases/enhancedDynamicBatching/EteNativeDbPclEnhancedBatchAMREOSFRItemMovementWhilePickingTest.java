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
 * Scenario FFILLSVCS-TC-10943 Mobile Flows -
 * Perform Selecting by picking items As Ordered and move items to new container
 * while picking for modify order for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeDbPclEnhancedBatchAMREOSFRItemMovementWhilePickingTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10943_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for enhanced DB modify non-EBT order")
    public void validateE2eEnhancedDBModifyOrderPicking() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10943, testOutputData);
        } else {
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10943_1_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10943_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for enhanced DB modify non-EBT orders")
    public void validateE2eEnhancedDBModifyOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10943, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10943_1_1, ExcelUtils.EB_PCL_TC_10943_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10943_1_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_enhancedBatching_pcl_tc_10943_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for enhanced DB modify non-EBT orders")
    public void validateE2eEnhancedDBModifyOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10943, ExcelUtils.EB_PCL_TC_10943);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10943, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10943_1_1, ExcelUtils.EB_PCL_TC_10943_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10943_1_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
