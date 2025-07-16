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
 * Scenario FFILLSVCS-TC-7140 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup rush and normal orders in different time slots with OSFR in Kroger.com for Multi-threaded store and perform Batching >
 * Verify the rush order Label and count in cue.
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the rush and normal orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush and normal orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowMultipleRushOrdersAndNormalOrdersTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_web_part1_nonEbt", "ete_web_part1_multiThread_rushOrder_manualBatching_nonEbt", "ete_web_part1_rushOrder_manualBatching"}, description = "Verify multiple rush orders placed for Non pcl orders")
    public void validateMultipleRushAndNormalOrders() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, secondOrderTestData);
        baseCommands.webpageRefresh();
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        baseCommands.webpageRefresh();
        fourthOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
        baseCommands.webpageRefresh();
        baseCommands.wait(20);
        cueHomePage.multipleRushAndNormalOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders, KrogerSeamLessPortalOrderCreation.normalOrders);
        baseCommands.webpageRefresh();
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        baseCommands.browserBack();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, secondOrderTestData);
        baseCommands.browserBack();
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        baseCommands.browserBack();
        fourthOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_rushOrder_manualBatching"}, description = "Verify multiple rushOrders are picked")
    public void validateMultipleRushAndNormalOrdersOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(fourthOrderTestData.get(ExcelUtils.STORE_ID)));
        fourthOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple rush orders staged and checked-in")
    public void validateMultipleRushAndNormalOrdersOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(fourthOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(fourthOrderTestData);
    }

    @Test(groups = {"ete_rushOrder_manualBatching_tc_7140_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple rush orders picked up and paid")
    public void validateMultipleRushAndNormalOrdersOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3, ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(fourthOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_2);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(fourthOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_SCENARIO_TC_7140_3);
    }
}