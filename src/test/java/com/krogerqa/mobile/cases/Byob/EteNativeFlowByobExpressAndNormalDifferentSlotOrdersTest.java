package com.krogerqa.mobile.cases.byob;

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
 * Scenario BYOB-TC-9083 Mobile Flows -
 * Perform Selecting for all 2 orders by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Validate Bag Icons for common trolley while Picking
 * Stage Pcl containers using Harvester Native and Validate Bag Icon>
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowByobExpressAndNormalDifferentSlotOrdersTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_byob_express_and_normal_order_9083_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non ebt byob order")
    public void validateE2eDbByobExpressPclOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG,String.valueOf(true));
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
    }

    @Test(groups = {"ete_byob_express_and_normal_order_9083_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non ebt pcl byob order")
    public void validateE2eDbByobExpressPclOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.BYOB_PCL_TC_9083_1);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_9083_1, secondOrderTestData);
    }

    @Test(groups = {"ete_byob_express_and_normal_order_9083_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT byob order")
    public void validateE2eDbByobExpressPclOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.BYOB_PCL_TC_9083_1);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
        ciaoDestagingAndCheckout.changeStoreSetup();
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_9083_1, secondOrderTestData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
