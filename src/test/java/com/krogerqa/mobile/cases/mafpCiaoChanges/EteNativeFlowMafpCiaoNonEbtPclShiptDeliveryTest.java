package com.krogerqa.mobile.cases.mafpCiaoChanges;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
/**
 * Scenario FFILLSVCS-TC-10414 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Non Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-Stage containers and complete Checkout for Snap-EBT Order using Ciao Native for Non Pcl Order
 */

public class EteNativeFlowMafpCiaoNonEbtPclShiptDeliveryTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10414_native_part1_mafp_ciao", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Shipt Delivery order")
    public void validateE2eShiptDeliveryOrderSelecting() {
        testOutputData= ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414,ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414, testOutputData);
    }
    @Test(groups = {"ete_pcl_tc_10414_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for the pcl order")
    public void validateE2eStagingPclMafpCiaoChangesShiptDeliveryOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10414_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl order")
    public void validateE2ePclPickOrderCheckoutPclMafpCiaoChangesShiptDeliveryOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10414, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
