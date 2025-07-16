package com.krogerqa.mobile.cases.harvesterWithoutPCL;

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
 * Scenario 4 Mobile Flows -
 * Perform selecting with marking item as substitute and Shorting for Multi-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers for Non-ebt order using Ciao Native
 */
public class EteNativeFlowSubstitutionShortTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete4_native_part1_substituteShort", "ete4_native_part1_nonEbt", "ete4_native_part1_multiThreaded"}, description = "Verify selecting for substituteShort order")
    public void validateE2eSubstituteShortOrderHarvesterSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_4, testOutputData);
    }

    @Test(groups = {"ete4_native_part2_substituteShort", "ete4_native_part2_nonEbt", "ete4_native_part2_multiThreaded"}, description = "Verify staging for non ebt order")
    public void validateE2eSubstituteShortOrderHarvesterStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_4, testOutputData);
    }

    @Test(groups = {"ete4_native_part3_substituteShort", "ete4_native_part3_nonEbt", "ete4_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eSubstituteShortOrderCiaoCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(),PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_4, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
