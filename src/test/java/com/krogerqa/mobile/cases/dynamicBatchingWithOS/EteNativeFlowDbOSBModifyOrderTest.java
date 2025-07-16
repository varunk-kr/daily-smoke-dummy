package com.krogerqa.mobile.cases.dynamicBatchingWithOS;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.dynamicBatchingWithoutPCL.EteWebFlowDynamicBatchingModifyOrderPclTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-13624 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Validate Bag Icons while Picking
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbOSBModifyOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_dybOS_tc_13624_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Enhanced Batching db non-EBT pcl DB OS Plus modify order")
    public void validateE2eDBOSModifyOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624, testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13624_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Enhanced Batching db non-EBT pcl DB OS Plus modify order")
    public void validateE2eDBOSModifyOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDynamicBatchingModifyOrderPclTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1, testOutputData);
        }
    }

    @Test(groups = {"ete_dybOS_tc_13624_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Enhanced Batching db non-EBT pcl DB OS Plus modify order")
    public void validateE2eDBOSModifyOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowDynamicBatchingModifyOrderPclTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1, ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_OS_TC_13624_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
