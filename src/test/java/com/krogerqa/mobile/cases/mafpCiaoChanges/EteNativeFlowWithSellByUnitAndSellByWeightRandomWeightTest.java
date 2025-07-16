package com.krogerqa.mobile.cases.mafpCiaoChanges;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-10460
 * Perform selecting  for non-EBT non-Pcl store using Harvester Native >
 * By adding below UPC and Quantity "MAFP - CIAO changes add sell by weight item with 3 lbs quantity and fulfill it from Picking screen by putting 1 lb in 1 package each.
 * Add sell by unit item with 4 quantity and fulfill it from Order adjustment screen by placing 2 quantity in 2 packages each
 * Random weight item in different packages."
 * Stage non-Pcl  containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowWithSellByUnitAndSellByWeightRandomWeightTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_non_pcl_tc_10460_native_part1_mafp_ciao", "ete_native_part1_ebt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for nonPCL singleThread mafp order")
    public void validateE2eNonPclSelectingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_10460_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_singleThreaded"}, description = "Verify staging for nonPCL singleThread mafp order")
    public void validateE2eNonPclStagingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_10460_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for nonPCL singleThread mafp order")
    public void validateE2eNonPclPickOrderCheckoutMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10460, testOutputData);

    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
