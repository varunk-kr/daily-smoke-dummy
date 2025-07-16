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
 * FFILLSVCS-TC-10462
 * Perform selecting  for Non PCL Multi-threaded store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Non PCL Order
 */

public class EteNativeFlowWithSellByWeightPartialFulfillTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_nonPcl_tc_10462_native_part1_mafp_ciao", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for partially fulfilled pcl order")
    public void validateE2eNativePclSelectingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_10462_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for partially fulfilled pcl order")
    public void validateE2eStagingStatusPclMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_10462_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for partially fulfilled pcl order")
    public void validateE2ePclPickOrderCheckoutPclMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10462, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
