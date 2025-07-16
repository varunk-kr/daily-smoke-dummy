package com.krogerqa.web.cases.rushOrderWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;

import java.util.HashMap;

/**
 * Scenario RUSH_ORDER_SCENARIO_TC_6693 Web Flows -
 * Submit Rush order from Kroger Seamless Portal
 * Validate Rush order label, Rush order count and order details on Cue
 * Cancel the order from Cue
 * Validate cancelled status on Cue Dashboard
 */
public class EteWebFlowRushOrderCancelFromCueTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_rush_order_tc_6693_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify rush order placed for non-EBT pcl order and cancel the order from Cue")
    public void validateE2eCancelFromCuePclRushOrder() {
        if(!(testOutputData ==null) ){
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6693, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6693, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_6693, testOutputData);
        baseCommands.browserBack();
        cueOrderValidation.verifyCancelOrderFromCue(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        cueHomePage.verifyRushOrderInitialCount();
    }
}