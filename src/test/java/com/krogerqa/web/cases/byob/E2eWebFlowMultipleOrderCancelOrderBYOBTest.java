package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Scenario BYOB-TC-7459 Web Flows -
 * Submit 3 Non-EBT Pcl Pickup orders in Kroger.com for multi Threaded store>
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting for all the orders.
 * Stage one order and Cancel one order with kroger bag.
 * After Bulk Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class E2eWebFlowMultipleOrderCancelOrderBYOBTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static List<HashMap<String, String>> list = new ArrayList<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();


    @Test(groups = {"ete_byob_tc_7459_multipleOrder_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple orders placed for non-EBT BYOB order")
    public void validateE2eByobMultipleOrdersPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.SHEET_NAME_TEST_DATA);
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7459);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        if (!Boolean.parseBoolean(firstOrderTestData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER))) {
            krogerSeamLessPortalOrderCreation.logoutAndLoginKCP();
        }
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7459_1);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        if (!Boolean.parseBoolean(firstOrderTestData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER))) {
            krogerSeamLessPortalOrderCreation.logoutAndLoginKCP();
        }
        firstOrderTestData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG, String.valueOf(true));
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7459_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7459, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7459_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7459_2, thirdOrderTestData);
        list.add(firstOrderTestData);
        list.add(secondOrderTestData);
        list.add(thirdOrderTestData);
        firstOrderTestData = cueOrderValidation.getMultipleOrderBagCount(ExcelUtils.BYOB_PCL_TC_7459, list, firstOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.BYOB_PCL_TC_7459, firstOrderTestData, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.BYOB_PCL_TC_7459, firstOrderTestData, thirdOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple orders picked for non-EBT BYOB order")
    public void validateE2eByobMultipleOrdersPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.BYOB_PCL_TC_7459_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.BYOB_PCL_TC_7459_2);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7459_1, secondOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BYOB_PCL_TC_7459_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7459_2, thirdOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BYOB_PCL_TC_7459_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify one order staged and one cancelled for non-EBT BYOB order")
    public void validateE2eByobOrdersStagedAndCancelled() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459, ExcelUtils.BYOB_PCL_TC_7459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.BYOB_PCL_TC_7459_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged for non-EBT BYOB order")
    public void validateE2eByobMultipleOrdersStaged() {
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.BYOB_PCL_TC_7459_2);
        loginWeb.loginCue(thirdOrderTestData.get(ExcelUtils.STORE_ID), thirdOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(thirdOrderTestData);
    }

    @Test(groups = {"ete_byob_tc_7459_multipleOrder_web_part5", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT BYOB order")
    public void validateE2eByobMultipleOrdersOrdersPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_1, ExcelUtils.BYOB_PCL_TC_7459_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7459_2, ExcelUtils.BYOB_PCL_TC_7459_2);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7459_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7459_2);
    }
}
