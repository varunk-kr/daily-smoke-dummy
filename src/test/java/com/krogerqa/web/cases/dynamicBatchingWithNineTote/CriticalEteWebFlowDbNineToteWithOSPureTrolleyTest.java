package com.krogerqa.web.cases.dynamicBatchingWithNineTote;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario  FFILLSVCS-TC-15067 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching-Enhanced Batching using request trolleys >
 * Validate 9 tote and DB OS + batching with more than 18 OS containers
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class CriticalEteWebFlowDbNineToteWithOSPureTrolleyTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_dyb_9tote_tc_15067_web_part1"}, description = "Verify order placed for db PCL order with OS and SM OS")
    public void validateE2eDbPclSMOSOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_SMALL_OS_ITEM_PRESENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AMRE_OSFR_SMOS);
        testOutputData.put(ExcelUtils.IS_DB_OS_ENABLED, String.valueOf(true));
        testOutputData.put(ExcelUtils.MAX_SMOS_COUNT, "8");
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067, testOutputData);
        CueOrderDetailsPage.verifyDBOSBatchingValidation(testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15067_web_part2"}, description = "Verify order picked for db PCL order with OS and SM OS")
    public void validateE2eDbPclSMOSOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067, testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15067_web_part3"}, description = "Verify order staged and checked-in for db PCL order with OS and SM OS")
    public void validateE2eDbPclSMOSOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15067_web_part4"}, description = "Verify order picked up and paid for db PCL order with OS and SM OS")
    public void validateE2eDbPclSMOSOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067, ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_9TOTE_TC_15067);
    }
}
