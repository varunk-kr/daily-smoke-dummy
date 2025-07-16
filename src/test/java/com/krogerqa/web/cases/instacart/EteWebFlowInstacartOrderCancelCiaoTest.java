package com.krogerqa.web.cases.instacart;

import com.krogerqa.api.InstacartHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12863 Web Flows -
 * Submit an instacart order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and pick order through API>
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After De-stage >
 * Verify order is Cancelled.
 */
public class EteWebFlowInstacartOrderCancelCiaoTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_instacart_cancel_ciao_tc_12863_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for instacart order")
    public void validateE2eNewInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12863, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = instacartHelper.createAndVerifyOrderDetails(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyInstacartOrderInCue(testOutputData);
        testOutputData = instacartHelper.pickInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyPickedStatusInCueForInstacartOrder(testOutputData);
        testOutputData = cueOrderValidation.verifyReadyForStagingInDash(testOutputData);
    }

    @Test(groups = {"ete_instacart_cancel_ciao_tc_12863_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order staged for instacart order")
    public void validateE2eStagedInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12863, ExcelUtils.INSTACART_SCENARIO_TC_12863);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCueForInstacartOrder(testOutputData);
    }

    @Test(groups = {"ete_instacart_cancel_ciao_tc_12863_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order cancelled status")
    public void validateCancelledInstacartOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12863, ExcelUtils.INSTACART_SCENARIO_TC_12863);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCancelledStatusForInstacartOrder(testOutputData);
    }
}
