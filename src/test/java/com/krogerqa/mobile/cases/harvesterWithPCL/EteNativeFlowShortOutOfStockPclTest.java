package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * Perform selecting with marking item as Out of Stock and Shorting for pcl Multi-threaded store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowShortOutOfStockPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4552_native_part1_shortOutOfStock", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for partially fulfilled pcl order")
    public void validateE2eNativeSelectingForShortOutOfStockPclOrder() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4552, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4552_native_part2_shortOutOfStock", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for partially fulfilled pcl order")
    public void validateE2eStagingForShortOSSPclOrder() {
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4552, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4552_native_part3_shortOutOfStock", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for partially fulfilled pcl order")
    public void validateE2ePclShortOutOfStockPickOrderCheckout() {
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4552, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
