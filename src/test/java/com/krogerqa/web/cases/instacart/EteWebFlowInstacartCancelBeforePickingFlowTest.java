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
 * Scenario FFILLSVCS-TC-12845 Web Flows -
 * Submit an instacart order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and pick order through API>
 * Cancel order from Cue, before selecting >
 * Verify order is Cancelled in Cue
 */

public class EteWebFlowInstacartCancelBeforePickingFlowTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_instacart_tc_12845_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify new order placed for instacart order")
    public void validateE2eNewInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12845, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = instacartHelper.createAndVerifyOrderDetails(testOutputData);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyInstacartOrderInCue(testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue((testOutputData));
    }
}