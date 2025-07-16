package com.krogerqa.mobile.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10463
 * Perform selecting  for snap-EBT non-Pcl store using Harvester Native >
 * By adding items and quantity as below
 * Sell by Service counter sell by unit - 3 qty in 1 package and partially fulfill it.
 * Sell by Service counter sell by weight - 3 lbs in 2  packages.
 * Random weight - all in 1 package.>
 * Stage non-Pcl  containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowWithPartialSellByUnitAndSellByWeightRandomWeightTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10463_native_part1_mafp_ciao", "ete_native_part1_ebt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclSelectingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10463_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_singleThreaded"}, description = "Verify staging for partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclStagingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10463_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclPickOrderCheckoutMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
