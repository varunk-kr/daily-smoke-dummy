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
 * Scenario 8 Mobile Flows -
 * Perform Selecting by picking items (all temperature type items along with random weight item) As Ordered for Single-threaded Harris Teeter store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class EteNativeFlowHarrisTeeterTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete8_native_part1_harrisTeeter", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for Harris Teeter order")
    public void validateE2eNativeHarrisTeeterOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_8, testOutputData);
    }

    @Test(groups = {"ete8_native_part2_harrisTeeter", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for Harris Teeter order")
    public void validateE2eNativeHarrisTeeterOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_8, testOutputData);
    }

    @Test(groups = {"ete8_native_part3_harrisTeeter", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for Harris Teeter order")
    public void validateE2eNativeHarrisTeeterOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_8, ExcelUtils.SCENARIO_8);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_8, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}