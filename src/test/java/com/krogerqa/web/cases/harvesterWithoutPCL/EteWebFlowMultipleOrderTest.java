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
 * Scenario 6 Web Flows -
 * Submit Multiple Non-EBT Pickup orders in Kroger.com for the same Multi-threaded store and perform Batching >
 * Verify Multiple New Non-EBT Order Details in Cue for the same store >
 * After selecting, verify Picked order and container status for Multiple orders in the same store in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in for Multiple orders in the same store in Cue >
 * Verify Multiple orders are displayed in Dash for the same store order >
 * After checkout, verify Multiple orders are dropped from Dash for the same store order >
 * Verify Multiple non-EBT orders for the same store are Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowMultipleOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete6_web_part1_multipleOrders", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple orders placed for non-EBT order in same store")
    public void validateE2eNewMultipleOrders() {
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_6_1);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        if(!Boolean.parseBoolean(firstOrderTestData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER))){
            krogerSeamLessPortalOrderCreation.logoutAndLoginKCP();
        }
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_6_2);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(true));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_6_2, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_6_1, firstOrderTestData);
    }

    @Test(groups = {"ete6_web_part2_multipleOrders", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple orders picked for non-EBT order in same store")
    public void validateE2eMultipleOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1, ExcelUtils.SCENARIO_6_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2, ExcelUtils.SCENARIO_6_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_6_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_6_2, secondOrderTestData);
    }

    @Test(groups = {"ete6_web_part3_multipleOrders", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order in same store")
    public void validateE2eMultipleOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1, ExcelUtils.SCENARIO_6_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2, ExcelUtils.SCENARIO_6_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete6_web_part4_multipleOrders", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E Multiple Order Fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2eMultipleOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_1, ExcelUtils.SCENARIO_6_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SCENARIO_6_2, ExcelUtils.SCENARIO_6_2);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_6_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_6_2);
    }
}
