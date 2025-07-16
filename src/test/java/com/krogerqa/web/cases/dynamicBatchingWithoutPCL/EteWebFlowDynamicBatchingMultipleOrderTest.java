package com.krogerqa.web.cases.dynamicBatchingWithoutPCL;

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
 * Scenario DYNAMIC_BATCH_TC_5583 Web Flows -
 * Submit Multiple Non-EBT Pickup orders for different time slots in Kroger.com for Multi-threaded store and perform dynamic Batching >
 * Verify New Non-EBT Orders Details in Cue >
 * After selecting, verify Picked order and container status in Cue for all the orders >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify orders displayed in Dash >
 * After checkout, verify orders dropped from Dash >
 */
public class EteWebFlowDynamicBatchingMultipleOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_db_tc_5583_dynamicBatching_multipleOrder_web_part1_snapEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple orders placed for Dynamic batching snapEBT Order")
    public void validateEteDynamicBatchingMultipleOrdersPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        } if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        } if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewDynamicBatchingOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_5583_1);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewDynamicBatchingOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_5583_2);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewDynamicBatchingOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_5583_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyTrolley(firstOrderTestData, secondOrderTestData, thirdOrderTestData);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5583_multipleOrder_web_part2_snapEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple orders picked for Dynamic batching snapEBT Order")
    public void validateEteDynamicBatchingMultipleOrdersPicked() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_2, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_TC_5583_3, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5583_multipleOrder_web_part3_snapEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders staged and checked-in for Dynamic batching snapEBT Order")
    public void validateEteDynamicBatchingMultipleOrdersStaged() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(thirdOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5583_multipleOrder_web_part4_snapEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders picked up and paid for Dynamic batching snapEBT Order")
    public void validateEteDynamicBatchingMultipleOrdersPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_TC_5583_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_TC_5583_2);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_TC_5583_3);
    }
}