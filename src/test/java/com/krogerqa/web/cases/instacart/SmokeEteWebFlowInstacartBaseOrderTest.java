package com.krogerqa.web.cases.instacart;

import com.krogerqa.api.InstacartHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12847 Web Flows -
 * Submit an instacart order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and pick order through API>
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class SmokeEteWebFlowInstacartBaseOrderTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_instacart_tc_12864_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for instacart order")
    public void validateE2eNewInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12864, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = instacartHelper.createAndVerifyOrderDetails(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyInstacartOrderInCue(testOutputData);
        testOutputData = instacartHelper.pickInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyPickedStatusInCueForInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyReadyForStagingInDash(testOutputData);
    }

    @Test(groups = {"ete_instacart_tc_12864_web_part2_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order staged for instacart order")
    public void validateE2eStagedInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12864, ExcelUtils.INSTACART_SCENARIO_TC_12864);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCueForInstacartOrder(testOutputData);
    }

    @Test(groups = {"ete_instacart_tc_12864_web_part3_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Validate E2E Instacart order fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2ePickedUpAndPaidInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12864, ExcelUtils.INSTACART_SCENARIO_TC_12864);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaidForInstacartOrder(testOutputData);
    }
}
