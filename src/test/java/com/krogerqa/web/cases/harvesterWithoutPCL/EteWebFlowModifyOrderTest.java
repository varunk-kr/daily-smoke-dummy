package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 11 Web Flows -
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store>
 * Modify the order by adding one more item into Order and perform batching
 * Verify New Non-EBT modified Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue  for the modified order>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowModifyOrderTest extends BaseTest {

    public static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete11_web_part1_nonEbt_modifyOrder", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed and modfied it by adding 1 new item")
    public void validateE2eNewNonEbtModifyOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_11);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_11, testOutputData);
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.SCENARIO_11, ExcelUtils.SCENARIO_11_1, ExcelUtils.SCENARIO_11_1_1, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete11_web_part2_nonEbt_modifyOrder", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT-Modify order")
    public void validateE2eNonEbtModifyOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete11_web_part3_nonEbt_modifyOrder", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT-Modify order")
    public void validateE2eNonEbtModifyOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete11_web_part4_nonEbt_modifyOrder", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT-Modify order")
    public void validateE2eNonEbtModifyOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME));
    }
}