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
 * Scenario 1 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class EteNativeFlowNonEbtTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete1_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded", "smokeNativePart1"}, description = "Verify selecting for non ebt order")
    public void validateE2eNonEbtOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_1, testOutputData);
    }

    @Test(groups = {"ete1_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded", "smokeNativePart2"}, description = "Verify staging for non ebt order")
    public void validateE2eNonEbtOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_1, testOutputData);
    }

    @Test(groups = {"ete1_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded", "smokeNativePart3"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eNonEbtOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_1, ExcelUtils.SCENARIO_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_1, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
