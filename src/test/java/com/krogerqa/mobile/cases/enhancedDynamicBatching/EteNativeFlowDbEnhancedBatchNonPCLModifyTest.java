package com.krogerqa.mobile.cases.enhancedDynamicBatching;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.enhancedDynamicBatching.EteWebFlowDbEnhancedBatchNonPCLModifyTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario EB_NON_PCL_TC-10938 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Non Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for Snap-EBT Order using Ciao Native for Non Pcl Order
 */
public class EteNativeFlowDbEnhancedBatchNonPCLModifyTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_native_part1_snapEbt", "ete_native_part1_snapEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db snap ebt non pcl order and modify")
    public void validateE2eDBEnhancedSnapEbtModifyOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbEnhancedBatchNonPCLModifyTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1_1, ExcelUtils.EB_NON_PCL_TC_10938_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.EB_NON_PCL_TC_10938_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1, ExcelUtils.EB_NON_PCL_TC_10938_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.EB_NON_PCL_TC_10938_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_native_part2_snapEbt", "ete_native_part2_snapEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db snap ebt non pcl order")
    public void validateE2eDBEnhancedSnapEbtModifyOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbEnhancedBatchNonPCLModifyTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1_1, ExcelUtils.EB_NON_PCL_TC_10938_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.EB_NON_PCL_TC_10938_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1, ExcelUtils.EB_NON_PCL_TC_10938_1);
            harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.EB_NON_PCL_TC_10938_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_native_part3_snapEbt", "ete_native_part3_snapEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for db snap-EBT non pcl order")
    public void validateE2eDBEnhancedSnapEbtModifyOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowDbEnhancedBatchNonPCLModifyTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1_1, ExcelUtils.EB_NON_PCL_TC_10938_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_NON_PCL_TC_10938_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10938_1, ExcelUtils.EB_NON_PCL_TC_10938_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_NON_PCL_TC_10938_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
