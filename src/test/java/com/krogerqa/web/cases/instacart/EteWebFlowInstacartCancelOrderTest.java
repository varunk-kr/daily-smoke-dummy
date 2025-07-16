package com.krogerqa.web.cases.instacart;

import com.krogerqa.api.InstacartHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12846 Web Flows -
 * Submit an instacart order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and pick order through API>
 *Cancel order from Cue and validated cancelled status on Cue Order Dashboard >
 */
public class EteWebFlowInstacartCancelOrderTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_instacart_tc_12846_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for instacart order and cancelled from Cue validated")
    public void validateE2eNewInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12846, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = instacartHelper.createAndVerifyOrderDetails(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyInstacartOrderInCue(testOutputData);
        testOutputData = instacartHelper.pickInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyPickedStatusInCueForInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyReadyForStagingInDash(testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue((testOutputData));
    }
}