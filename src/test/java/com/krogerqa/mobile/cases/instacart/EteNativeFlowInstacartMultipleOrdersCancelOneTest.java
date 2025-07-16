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
 * Scenario FFILLSVCS-TC-12862 Non-Ebt Mobile Flows >
 * Stage Instacart containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for instacart Order
 */
public class EteNativeFlowInstacartMultipleOrdersCancelOneTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_instacart_tc_12862_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify staging for instacart order")
    public void validateE2eInstacartMultipleOrdersCancelOneOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12862, ExcelUtils.INSTACART_SCENARIO_TC_12862);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12862_1, ExcelUtils.INSTACART_SCENARIO_TC_12862_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.INSTACART_SCENARIO_TC_12862, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.INSTACART_SCENARIO_TC_12862_1, secondOrderTestData);
    }

    @Test(groups = {"ete_instacart_tc_12862_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify de-staging and checkout for instacart order")
    public void validateE2eMultipleOrdersCancelOneInstacartOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12862, ExcelUtils.INSTACART_SCENARIO_TC_12862);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12862_1, ExcelUtils.INSTACART_SCENARIO_TC_12862_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.INSTACART_SCENARIO_TC_12862, firstOrderTestData);
        ciaoDestagingAndCheckout.verifyCancelOrderInCiao(secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}