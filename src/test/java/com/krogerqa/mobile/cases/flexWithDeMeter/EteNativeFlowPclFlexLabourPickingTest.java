package com.krogerqa.mobile.cases.flexWithDeMeter;

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
 * Scenario FLEX_SCENARIO_TC_4863 Mobile Flows
 * Perform selecting for FR and OS items in Multi-threaded store using Harvester Native >
 * * Stage containers using Harvester Native >
 * * After Customer Check-in, Destage containers and complete Checkout for Non-EBT Order using Ciao Native
 */
public class EteNativeFlowPclFlexLabourPickingTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvester = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_flexPcl_tc_4863_native_part1", "ete_native_part1_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for flex job order")
    public void validateE2eFlexJobHarvesterStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvester.verifyHarvesterStagingPcl(ExcelUtils.FLEX_SCENARIO_TC_4863, testOutputData);
    }

    @Test(groups = {"ete_flexPcl_tc_4863_native_part2", "ete_native_part2_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for flex job order")
    public void validateE2eFlexJobCiaoCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.FLEX_SCENARIO_TC_4863, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
