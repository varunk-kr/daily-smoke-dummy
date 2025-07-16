package com.krogerqa.mobile.cases.harvesterWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario PCL_FULFILLMENT_TYPES_SMOKE Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for snap-EBT Order using Ciao Native for Pcl Order
 */
public class SmokeEteNativeFlowAllFulfillmentTypeSmokeTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_pcl_tc_fulfillment_smokeTest_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for various fulfillment types snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_fulfillment_smokeTest_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for various fulfillment types snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.STAGING_STATUS, Constants.PickCreation.NOT_STAGED);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_fulfillment_smokeTest_native_part3_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for various fulfillment types snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderNrContainerStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_fulfillment_smokeTest_native_part4_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for various fulfillment types snap-EBT pcl order")
    public void validatePclFulfillmentTypesSmokeTestOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
