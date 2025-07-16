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
 * Scenario 3 Mobile Flows -
 * Perform selecting with marking item as Out of Stock and Shorting for Multi-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers for EBT order using Ciao Native
 */
public class EteNativeFlowShortOutOfStockTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete3_native_part1_shortOutOfStock", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for partially fulfilled order")
    public void validateE2eNativeSelectingForShortOutOfStockOrder() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_3, testOutputData);
    }

    @Test(groups = {"ete3_native_part2_shortOutOfStock", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for partially fulfilled order")
    public void validateE2eNativeStagingForShortOutOfStockOrder() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_3, testOutputData);
    }

    @Test(groups = {"ete3_native_part3_shortOutOfStock", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for partially fulfilled order")
    public void validateE2eNativeDestagingForShortOutOfStockOrder() {
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(),PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_3, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
