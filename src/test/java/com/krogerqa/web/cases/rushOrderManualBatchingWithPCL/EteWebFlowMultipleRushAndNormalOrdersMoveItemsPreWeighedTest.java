package com.krogerqa.web.cases.rushOrderManualBatchingWithPCL;

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
 * Scenario FFILLSVCS-TC-7142 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup rush orders in different time slots with CollapseTemp as AM_RE and 2 Non-EBT Pcl orders in Kroger.com for Multi-threaded store and perform manual Batching for all 4 orders>
 * Verify the rush order Label and count in cue.
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Staging for the orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify orders are displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowMultipleRushAndNormalOrdersMoveItemsPreWeighedTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_tc_7142_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT rush orders and multiple non-EBT orders with CollapseTemp as AM_RE are placed")
    public void validateE2eMultipleRushOrdersAndNormalOrdersPlaced() {
        getTestdataSheet(ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        fourthOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        fourthOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_STAGING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER, String.valueOf(true));
        fourthOrderTestData.put(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7142_1, secondOrderTestData);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
        fourthOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.ROMB_SCENARIO_TC_7142_3, fourthOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.multipleRushAndNormalOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders, KrogerSeamLessPortalOrderCreation.normalOrders);
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        browserRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_1, secondOrderTestData);
        browserRefresh();
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
        browserRefresh();
        fourthOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_3, fourthOrderTestData);
        browserRefresh();
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData, secondOrderTestData);
        thirdOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData, fourthOrderTestData);

    }

    @Test(groups = {"ete_tc_7142_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple non-EBT rush orders and multiple non-EBT orders with CollapseTemp as AM RE are picked")
    public void validateE2eMultipleRushOrdersAndNormalOrdersPicked() {
        getTestdataSheet(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        fourthOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ROMB_SCENARIO_TC_7142_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_tc_7142_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple non-EBT rush orders and multiple non-EBT orders with CollapseTemp as AM RE are staged and checked-in")
    public void validateE2eMultipleRushOrdersAndNormalOrdersStaged() {
        getTestdataSheet(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.ROMB_SCENARIO_TC_7142_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(fourthOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        fourthOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.ROMB_SCENARIO_TC_7142_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_tc_7142_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple non-EBT rush orders and multiple non-EBT orders with CollapseTemp as AM RE are picked up and paid")
    public void validateE2eMultipleRushOrdersAndNormalOrdersPaid() {
        getTestdataSheet(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(fourthOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7142);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7142_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7142_2);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ROMB_SCENARIO_TC_7142_3);
    }

    public void getTestdataSheet(String sheetName1, String sheetName2, String sheetName3, String sheetName4) {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142, sheetName1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_1, sheetName2);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_2, sheetName3);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_3, sheetName4);
    }

    public void browserRefresh() {
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
    }
}
