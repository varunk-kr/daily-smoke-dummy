package com.krogerqa.web.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14041 Web Flows -
 * Submit Non-EBT Pickup Express order in Kroger.com for Single-threaded store>
 * Modify the Express order by adding one more item into Order and perform batching
 * Verify New Non-EBT modified Order Details in Cue >
 * After selecting, verify Picked Express order and container status in Cue  for the modified order>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify Express order is displayed in Dash >
 * After checkout, verify Express order is dropped from Dash >
 * Verify order is tendered from Ciao
 */
public class EteWebFlowDbSingleThreadExpressModifyOrderTest extends BaseTest {

    public static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify new Express order placed and modified it by adding 1 new item")
    public void validateE2eNewNonEbtModifyExpressOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_EXPRESS_ORDER_TC_14041);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14041, testOutputData);
        baseCommands.wait(3);
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.DB_EXPRESS_ORDER_TC_14041, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14041_1_1, testOutputData);
        baseCommands.wait(5);
        testOutputData.put(ExcelUtils.BATCH_ORDER, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify express order picked for non-EBT-Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify express order staged and checked-in for non-EBT-Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14041_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Validate E2E fulfillment for Express Modify order")
    public void validateE2eNewNonEbtModifyExpressOrderCancelled() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME));
    }
}
