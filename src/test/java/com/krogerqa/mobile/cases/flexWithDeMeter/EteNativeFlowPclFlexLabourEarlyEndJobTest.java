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
 * Scenario FLEX_SCENARIO_TC_4867 Mobile Flows
 * Perform selecting for FR & OS items in Multi-threaded store using Harvester Native with items not picked from Demeter >
 * * Stage containers using Harvester Native >
 * * After Customer Check-in, Destage containers and complete Checkout for Non-EBT Order using Ciao Native
 */
public class EteNativeFlowPclFlexLabourEarlyEndJobTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvester = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_flexPcl_tc_4867_native_part1", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for flex job order")
    public void validateE2eFlexEarlyEndJobHarvesterSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.FLEX_SCENARIO_TC_4867, ExcelUtils.FLEX_SCENARIO_TC_4867);
        harvester.verifyHarvesterSelectingPcl(ExcelUtils.FLEX_SCENARIO_TC_4867, testOutputData);
    }

    @Test(groups = {"ete_flexPcl_tc_4867_native_part2", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for flex job order")
    public void validateE2eFlexEarlyEndJobHarvesterStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.FLEX_SCENARIO_TC_4867, ExcelUtils.FLEX_SCENARIO_TC_4867);
        harvester.verifyHarvesterStagingPcl(ExcelUtils.FLEX_SCENARIO_TC_4867, testOutputData);
    }

    @Test(groups = {"ete_flexPcl_tc_4867_native_part3", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for flex job order")
    public void validateE2eFlexEarlyEndJobCiaoCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.FLEX_SCENARIO_TC_4867, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
