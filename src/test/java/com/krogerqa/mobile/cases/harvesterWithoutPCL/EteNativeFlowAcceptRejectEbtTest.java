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
 * Perform selecting with marking item as Accept and Reject  using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers for Ebt order using Ciao Native
 */
public class EteNativeFlowAcceptRejectEbtTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvester = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete5_native_part1_substituteAcceptReject", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for EbtAcceptReject order")
    public void validateE2eEbtAcceptRejectHarvesterSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvester.verifyHarvesterSelecting(ExcelUtils.SCENARIO_5, testOutputData);
    }

    @Test(groups = {"ete5_native_part2_substituteAcceptReject", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for EbtAcceptReject order")
    public void validateE2eEbtAcceptRejectHarvesterStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvester.verifyHarvesterStaging(ExcelUtils.SCENARIO_5, testOutputData);
    }

    @Test(groups = {"ete5_native_part3_substituteAcceptReject", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for EbtAcceptReject  order")
    public void validateE2eEbtAcceptRejectCiaoCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemInCiao(ExcelUtils.SCENARIO_5, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
