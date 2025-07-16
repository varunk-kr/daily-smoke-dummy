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
 * FFILLSVCS-TC-10423
 * Perform selecting  for pcl Multi-threaded store using Harvester Native with Service Counter and Random weight items >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowCarryOverSellByUnitSellByWeightTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout =  CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_mafp_ciao_10423_native_part1", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for partially fulfilled pcl order")
    public void validateE2eNativePclSelectingStatusPclMAFPCiaoCarryOver() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, testOutputData);
    }
    @Test(groups = {"ete_mafp_ciao_10423_native_part2", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for partially fulfilled pcl order")
    public void validateE2eStagingStatusPclMAFPCiaoCarryOver() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, testOutputData);
    }

    @Test(groups = {"ete_mafp_ciao_10423_native_part3", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for partially fulfilled pcl order")
    public void validateE2ePclPickOrderCheckoutPclMAFPCiaoCarryOver() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10423, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
