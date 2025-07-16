package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario 3 Web Flows -
 * Submit EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * After selecting trolleys with Shorting and Out of Stock, verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After destaging, verify order is dropped from Dash >
 * Verify EBT order is Picked Up and Paid in Cue
 */
public class EteWebFlowShortOutOfStockTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete3_web_part1_shortOutOfStock", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for partially fulfilled order")
    public void validateE2eNewStatusForShortOutOfStockOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_3);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_3, testOutputData);
    }

    @Test(groups = {"ete3_web_part2_shortOutOfStock", "ete_web_part2_ebt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for partially fulfilled order")
    public void validateE2ePickedStatusForShortOutOfStockOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_3, testOutputData);
    }

    @Test(groups = {"ete3_web_part3_shortOutOfStock", "ete_web_part3_ebt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for partially fulfilledorder")
    public void validateE2eStagedStatusForShortOutOfStockOrder() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete3_web_part4_shortOutOfStock", "ete_web_part4_ebt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for partially fulfilled order")
    public void validateE2ePaidStatusForShortOutOfStockOrder() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_3);
    }
}
