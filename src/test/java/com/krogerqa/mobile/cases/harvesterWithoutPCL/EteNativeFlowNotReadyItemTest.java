package com.krogerqa.mobile.cases.harvesterWithoutPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 10 Mobile Flows -
 * Perform Selecting by marking service counter item as not ready and picking rest of the items As Ordered using Harvester Native >
 * Stage containers using Harvester Native >
 * Pick the not ready item in order adjustment flow using Harvester Native
 * Stage the not ready container using Harvester Native
 * After Customer Check-in, DeStage containers and complete Checkout for EBT Order using Ciao Native
 */
public class EteNativeFlowNotReadyItemTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete1_native_part1_NotReadyItem", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Not Ready EBT order")
    public void validateE2eNativeSelectingForNotReadyEbtOrder() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.SCENARIO_10, testOutputData);
    }

    @Test(groups = {"ete1_native_part2_NotReadyItem", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging and order adjustment for Not Ready EBT order")
    public void validateE2eNativeStagingAndOrderAdjustmentForNotReadyEbtOrder() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.SCENARIO_10,testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.SCENARIO_10,testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.SCENARIO_10,testOutputData);
    }

    @Test(groups = {"ete1_native_part3_NotReadyItem", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging for not Ready EBT order")
    public void validateE2eNativeDeStagingForNotReadyEbtOrder() {
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(),PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SCENARIO_10, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
