package com.krogerqa.mobile.cases.dynamicBatchingSingleThreadPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.dynamicBatchingSingleThreadPCL.EteWebFlowDbSingleThreadModifyOrderTest;
import org.testng.annotations.Test;
import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-12729 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-thread Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowDbSingleThreadModifyOrderTest extends BaseTest{
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_tc_12729_singleThread_modifyOrder_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db non ebt modified pcl order")
    public void validateE2eItemMovementAndReStagePclOversizeSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbSingleThreadModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1_1, ExcelUtils.DB_SINGLE_THREAD_12729_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_SINGLE_THREAD_12729_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1, ExcelUtils.DB_SINGLE_THREAD_12729_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_SINGLE_THREAD_12729_1, testOutputData);
        }
    }
    @Test(groups = {"ete_db_tc_12729_singleThread_modifyOrder_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db non ebt modified pcl order")
    public void validateE2eItemMovementAndReStagePclOversizeStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbSingleThreadModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1_1, ExcelUtils.DB_SINGLE_THREAD_12729_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_SINGLE_THREAD_12729_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1, ExcelUtils.DB_SINGLE_THREAD_12729_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_SINGLE_THREAD_12729_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_tc_12729_singleThread_modifyOrder_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for db non-EBT modified pcl order")
    public void validateE2eItemMovementAndReStagePclOversizeCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowDbSingleThreadModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1_1, ExcelUtils.DB_SINGLE_THREAD_12729_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_SINGLE_THREAD_12729_1_1,testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12729_1, ExcelUtils.DB_SINGLE_THREAD_12729_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_SINGLE_THREAD_12729_1, testOutputData);
        }
    }
    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}