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
 * Scenario FFILLSVCS-TC-7122 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup rush orders in Same time slots with CollapseTempAll in Kroger.com for Multi-threaded store and perform Batching >
 * Verify the rush order Label and count in cue.
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the rush orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowMultipleRushOrdersManualBatchingSameSlotTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String>  lastOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_tc_7122_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT rush orders with Same time slots and CollapseTempAll are placed")
    public void validateE2eMultipleRushOrdersWithSameTimeSlotPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7122, firstOrderTestData);
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7122, firstOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        lastOrderTestData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        lastOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        lastOrderTestData.put(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER, String.valueOf(true));
        lastOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData);
        baseCommands.webpageRefresh();
        lastOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7122_2, lastOrderTestData);
        cueHomePage.multipleOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders);
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData);
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
        lastOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7122_2, lastOrderTestData);
    }

    @Test(groups = {"ete_tc_7122_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are picked")
    public void validateE2eMultipleRushOrdersWithSameTimeSlotPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7122_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        lastOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7122_2, lastOrderTestData);
    }

    @Test(groups = {"ete_tc_7122_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are staged and checked-in")
    public void validateE2eMultipleRushOrdersWithSameTimeSlotStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(lastOrderTestData);
    }

    @Test(groups = {"ete_tc_7122_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple non-EBT rush orders with different time slots and CollapseTempAll are picked up and paid")
    public void validateE2eMultipleRushOrdersWithSameTimeSlotPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_1, ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        lastOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7122_2, ExcelUtils.ROMB_SCENARIO_TC_7122_2);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(lastOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7122_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7122_2);
    }
}
