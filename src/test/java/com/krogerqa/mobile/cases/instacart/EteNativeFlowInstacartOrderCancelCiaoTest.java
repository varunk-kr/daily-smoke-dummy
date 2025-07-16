package com.krogerqa.mobile.cases.instacart;

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
 * Scenario FFILLSVCS-TC-12863 Non-Ebt Mobile Flows >
 * Stage Instacart containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for instacart Order
 */
public class EteNativeFlowInstacartOrderCancelCiaoTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_instacart_cancel_ciao_tc_12863_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify staging for instacart order")
    public void validateE2eInstacartOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12863, ExcelUtils.INSTACART_SCENARIO_TC_12863);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.INSTACART_SCENARIO_TC_12863, testOutputData);

    }

    @Test(groups = {"ete_instacart_cancel_ciao_tc_12863_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify de-staging and cancel for instacart order")
    public void validateE2eInstacartOrderCancel() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12863, ExcelUtils.INSTACART_SCENARIO_TC_12863);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);

    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
