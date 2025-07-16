package com.krogerqa.web.cases.dynamicBatchingSingleThreadPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario DYNAMIC_BATCH_PCL_TC_12731 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store and cancel one order from cue
 * Perform Batching using request trolleys for remaining order
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbSingleThreadMultipleOrderTrolleyCancelCueTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();

    @Test(groups = {"ete_db_tc_12731_singleThread_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify multiple orders placed for PCL Dynamic batching nonEBT singleThread Order and one order cancelled from CUE")
    public void validateE2eNewMultipleOrdersPlaced() {
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCancelOrderFromCue(secondOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(secondOrderTestData);
        cueOrderDetailsPage.verifyTrolleysNotCreated();
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, firstOrderTestData);
    }


    @Test(groups = {"ete_db_tc_12731_singleThread_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify multiple orders picked for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eMultipleOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12731_singleThread_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify multiple orders staged and checked-in for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eMultipleOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12731_singleThread_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify multiple orders picked up and paid for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eMultipleOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
    }
}
