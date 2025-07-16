package com.krogerqa.web.cases.rushOrderManualBatchingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-7123 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup rush orders in different time slots with CollapseTempAll in Kroger.com for Multi-threaded store and perform Batching >
 * Verify the rush order Label and count in cue.
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the rush orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowMultipleRushOrdersManualBatchingTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_tc_7123_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are placed")
    public void validateE2eMultipleRushOrdersWithDifferentTimeSlotPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7123_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.multipleOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders);
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7123_1, secondOrderTestData);
    }

    @Test(groups = {"ete_tc_7123_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are picked")
    public void validateE2eMultipleRushOrdersWithDifferentTimeSlotPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7123, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7123_1, secondOrderTestData);
    }

    @Test(groups = {"ete_tc_7123_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are staged and checked-in")
    public void validateE2eMultipleRushOrdersWithDifferentTimeSlotStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_tc_7123_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are picked up and paid")
    public void validateE2eMultipleRushOrdersWithDifferentTimeSlotPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123, ExcelUtils.ROMB_SCENARIO_TC_7123);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7123_1, ExcelUtils.ROMB_SCENARIO_TC_7123_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7123);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7123_1);
    }
}
