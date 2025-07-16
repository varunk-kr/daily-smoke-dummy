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
 * FFILLSVCS-TC-10435
 * Perform selecting  for pcl single-threaded store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowShortNotReadyItemTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
   CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10435_native_part1_mafp_ciao", "ete_native_part1_ebt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for partially fulfilled pcl order")
    public void validateE2eNativePclSelectingStatusMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10435_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_singleThreaded"}, description = "Verify staging for partially fulfilled pcl order")
    public void validateE2eStagingStatusPclMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10435_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for partially fulfilled pcl order")
    public void validateE2ePclPickOrderCheckoutPclMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10435, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
