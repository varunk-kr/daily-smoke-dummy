package com.krogerqa.web.cases.dynamicBatchingSingleThreadPCL;

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
 * Scenario DYNAMIC_BATCH_TC-12727 Web Flows -
 * Submit Multiple Non-EBT Pickup orders for different time slots in Kroger.com for Single-threaded store and perform dynamic Batching >
 * Verify New Non-EBT Orders Details in Cue >
 * After selecting, verify Picked order and container status in Cue for all the orders >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify orders displayed in Dash >
 * After checkout, verify orders dropped from Dash >
 */
public class EteWebFlowDbSingleThreadMultipleOrderDifferentSlotTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_db_tc_12727_singleThread_multipleOrder_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify multiple orders placed for Dynamic batching nonEBT singleThread Order")
    public void validateEteDynamicBatchingMultipleOrdersPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_12727_1);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        if (!Boolean.parseBoolean(firstOrderTestData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER))) {
            krogerSeamLessPortalOrderCreation.logoutAndLoginKCP();
        }
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_12727_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_12727_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.searchOrderId(secondOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderValidation.verifyTrolleysNotCreatedForCanceledOrder();
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_12727_2, secondOrderTestData);
        ExcelUtils.removeExistingDataFromExcel(ExcelUtils.DYNAMIC_BATCH_TC_12727_2);
    }


    @Test(groups = {"ete_db_tc_12727_singleThread_multipleOrder_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify multiple orders picked for Dynamic batching nonEBT singleThread Order")
    public void validateEteDynamicBatchingMultipleOrdersPicked() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_TC_12727_1, firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12727_singleThread_multipleOrder_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify multiple orders staged and checked-in for Dynamic batching nonEBT singleThread Order")
    public void validateEteDynamicBatchingMultipleOrdersStaged() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12727_singleThread_multipleOrder_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify multiple orders picked up and paid for Dynamic batching nonEBT singleThread Order")
    public void validateEteDynamicBatchingMultipleOrdersPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_TC_12727_1);
    }
}
