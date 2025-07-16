package com.krogerqa.web.cases.bulkStagingWithPCL;

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
 * Scenario TC-11755 Web Flows -
 * Submit 3 Non-EBT Pcl Pickup rush orders and normal order in Kroger.com for single threaded store and cancel one rush order >
 * Verify the rush order Label and count in cue.
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting and Stage the rush orders.
 * After Bulk Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class E2eWebFlowMultipleRushOrderRemoveTC52NotificationForOneOrderBulkStagingTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple rush orders placed and cancel one order validate TC52 remove notifications")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalNewAndCancelOrdersPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BULK_STAGING_TC_11755_1, secondOrderTestData);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(secondOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        firstOrderTestData = cueOrderValidation.getItemCount(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_web_part2", "ete_web_part2_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify  multiple rush orders picked for non-EBT order bulk staging TC52 Remove Notifications in Little Bird App")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BULK_STAGING_TC_11755, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BULK_STAGING_TC_11755_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_web_part3", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order bulk staging TC52 Remove Notifications in Little Bird App")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(thirdOrderTestData);
    }

    @Test(groups = {"ete_bulkStaging_tc_11755_multipleRush_normalOrder_TC52_web_part4", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in bulk staging TC52 Remove Notifications in Little Bird App")
    public void validateE2eBulkStagingTC52MultipleRushAndNormalOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755, ExcelUtils.BULK_STAGING_TC_11755);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11755_2, ExcelUtils.BULK_STAGING_TC_11755_2);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11755);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11755_2);
    }
}
