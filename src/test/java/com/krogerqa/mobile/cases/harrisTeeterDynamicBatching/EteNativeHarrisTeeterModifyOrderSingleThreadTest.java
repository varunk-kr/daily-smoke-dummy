package com.krogerqa.mobile.cases.harrisTeeterDynamicBatching;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.cases.harrisTeeterDynamicBatching.EteWebFlowHarrisTeeterModifyOrderSingleThreadTest;

import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_HT_TC_14376 Mobile Flows -
 * Perform Selecting for Modify Order by picking items As Ordered for Single-threaded store using Harvester Native >
 * Stage containers for Modified Order using Harvester Native >
 * After Customer Check-in,tender the order from Ciao.
 */

public class EteNativeHarrisTeeterModifyOrderSingleThreadTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_14376_native_part1_dyb_ht_modify_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for Modify order")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrderSelecting() {
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowHarrisTeeterModifyOrderSingleThreadTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1);
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.HARRIS_TEETER_DYB_14376_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_14376_native_part2_dyb_ht_modify_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for Modify order")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrderStaging() {
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteWebFlowHarrisTeeterModifyOrderSingleThreadTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.HARRIS_TEETER_DYB_14376_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_14376_native_part3_dyb_ht_modify_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for Modify order")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrderCiao() {
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteWebFlowHarrisTeeterModifyOrderSingleThreadTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HARRIS_TEETER_DYB_14376_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14376_1, ExcelUtils.HARRIS_TEETER_DYB_14376_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HARRIS_TEETER_DYB_14376_1, testOutputData);
        }
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}