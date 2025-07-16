package com.krogerqa.mobile.cases.dynamicBatchingExpressOrder;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.dynamicBatchingExpressOrder.EteWebFlowDbSingleThreadExpressModifyOrderTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario Dyb Express Order FFILLSVCS-TC-14041 Mobile Flows -
 * Perform Selecting for Express Modify Order by picking items As Ordered for Single-threaded store using Harvester Native >
 * Stage containers for Express Modified Order using Harvester Native >
 * After Customer Check-in,tender the order from Ciao.
 */
public class EteNativeFlowDbSingleThreadExpressModifyOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for Dyb Express Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbSingleThreadExpressModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4563_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for Dyb Express Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowDbSingleThreadExpressModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, testOutputData);
        }
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for Dyb Express Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderCiao() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowDbSingleThreadExpressModifyOrderTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
