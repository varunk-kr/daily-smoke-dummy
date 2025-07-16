package com.krogerqa.mobile.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14050 Mobile Flows -
 * For Express orders - only one trolley will there for the order
 * Perform Selecting by picking items As Ordered for DyB Pcl store using Harvester Native. During picking substitute 2 items, mark 1 item as NR >
 * Stage Pcl containers using Harvester Native except the NR item container>
 * Perform order adjustment for NR item using harvester Native>
 * Stage the NR item container>
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowExpressOrderTwoWaySubsNRItemTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for express order, substitute 2 items and mark 1 item as NR")
    public void validateE2eTwoWaysSubsNRItemExpressOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging and order adjustment")
    public void validateE2eTwoWaysSubsNRItemExpressOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.STAGING_STATUS, Constants.PickCreation.NOT_STAGED);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_native_part3_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for NR item container")
    public void validateE2eTwoWaysSubsNRItemExpressOrderAdjustment() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_native_part4_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for db non-EBT pcl express order")
    public void validateE2eTwoWaysSubsNRItemExpressOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}