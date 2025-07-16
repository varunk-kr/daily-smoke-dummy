package com.krogerqa.mobile.cases.rushOrderWithPCL;

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
 * Scenario RUSH_ORDER_SCENARIO_TC_6559 - NR Item Mobile Flows -
 * Perform Selecting by marking service counter item as not ready and picking rest of the items As Ordered using Harvester Native
 * Stage containers using Harvester Native
 * Pick the not ready item in order adjustment flow using Harvester Native
 * Stage the not ready container using Harvester Native
 * After Customer Check-in, DeStage containers and complete Checkout for NonEBT Order using Ciao Native
 */

public class EteNativeFlowRushOrderNotReadyPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_rush_order_tc_6559_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt rush order")
    public void validateE2eNonEbtPclNotReadyRushOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6559, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6559_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt rush order")
    public void validateE2eNonEbtPclNotReadyRushOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.IS_NOT_READY_SCENARIO, String.valueOf(true));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6559, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6559, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6559, testOutputData);
    }

    @Test(groups = {"ete_rush_order_tc_6559_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt rush order")
    public void validateE2eNonEbtPclNotReadyRushOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6559, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
