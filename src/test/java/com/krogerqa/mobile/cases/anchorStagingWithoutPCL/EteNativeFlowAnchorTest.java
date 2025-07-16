package com.krogerqa.mobile.cases.anchorStagingWithoutPCL;

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
 * FFILLSVCS-TC-5308 Mobile Flows
 * Perform Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers using anchor staging >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native
 */

public class EteNativeFlowAnchorTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_anchor_tc_5308_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt anchor staging order")
    public void validateE2eNativeSelectingForNonEbtAnchorStagingOrder() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5308_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt anchor staging order")
    public void validateE2eNativeStagingForNonEbtAnchorStagingOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5308, ExcelUtils.ANCHOR_SCENARIO_TC_5308);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
        ExcelUtils.writeToExcel(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5308_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt anchor staging order")
    public void validateE2eNativeDeStagingForNonEbtAnchorStagingOrder() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
