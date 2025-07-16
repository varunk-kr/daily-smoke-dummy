package com.krogerqa.web.cases.instacart;

import com.krogerqa.api.InstacartHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12963 Web Flows - * Submit an 2 instacart order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and pick order through API>
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash > * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowInstacartMultipleOrderTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    InstacartHelper instacartHelper = InstacartHelper.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_instacart_tc_12963_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for instacart order")
    public void validateE2eNewMultipleInstacartOrder() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963, ExcelUtils.SHEET_NAME_TEST_DATA);
        firstOrderTestData = instacartHelper.createAndVerifyOrderDetails(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyInstacartOrderInCue(firstOrderTestData);
        firstOrderTestData = instacartHelper.pickInstacartOrder(firstOrderTestData);
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCueForInstacartOrder(firstOrderTestData);
        firstOrderTestData = cueOrderValidation.verifyReadyForStagingInDash(firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = instacartHelper.createAndVerifyOrderDetails(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyInstacartOrderInCue(secondOrderTestData);
        secondOrderTestData = instacartHelper.pickInstacartOrder(secondOrderTestData);
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCueForInstacartOrder(secondOrderTestData);
        secondOrderTestData = cueOrderValidation.verifyReadyForStagingInDash(secondOrderTestData);
    }

    @Test(groups = {"ete_instacart_tc_12963_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order staged for instacart order")
    public void validateE2eStagedMultipleOrdersInstacartOrder() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963, ExcelUtils.INSTACART_SCENARIO_TC_12963);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCueForInstacartOrder(firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963_1, ExcelUtils.INSTACART_SCENARIO_TC_12963_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCueForInstacartOrder(secondOrderTestData);
    }

    @Test(groups = {"ete_instacart_tc_12963_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order picked up and paid for instacart order")
    public void validateE2ePickedUpAndPaidMultipleInstacartOrder() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963, ExcelUtils.INSTACART_SCENARIO_TC_12963);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaidForInstacartOrder(firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.INSTACART_SCENARIO_TC_12963_1, ExcelUtils.INSTACART_SCENARIO_TC_12963_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaidForInstacartOrder(secondOrderTestData);
    }
}
