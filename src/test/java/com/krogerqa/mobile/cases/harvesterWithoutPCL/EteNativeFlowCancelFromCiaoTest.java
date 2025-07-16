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
 * Scenario 7 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, cancel EBT Order using Ciao Native
 */
public class EteNativeFlowCancelFromCiaoTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete7_native_part1_cancelFromCiao", "ete_native_part1_ebt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for EBT order")
    public void validateE2eNativeSelectingForCancelFromCiaoOrder() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_7, testOutputData);
    }

    @Test(groups = {"ete7_native_part2_cancelFromCiao", "ete_native_part2_ebt", "ete_native_part3_singleThreaded"}, description = "Verify staging for EBT order")
    public void validateE2eNativeStagingForCancelFromCiaoOrder() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_7, testOutputData);
    }

    @Test(groups = {"ete7_native_part3_cancelFromCiao", "ete_native_part2_ebt", "ete_native_part3_singleThreaded"}, description = "Verify canceling EBT order")
    public void validateE2eNativeCancelingOrderFromCiao() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
