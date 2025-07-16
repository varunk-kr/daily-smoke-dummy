package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11731 Web Flows -
 * Submit Non-EBT Pcl rush Pickup order in Kroger.com for Single-threaded store and perform Batching Order level batching>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowBulkStagingTc52Test extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify Non EBT rush order creation for TC 52 Notification")
    public void validateE2ETC52NonEBTOrderCreated() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
        baseCommands.browserBack();
        testOutputData = cueOrderValidation.getItemCount(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify Non EBT rush order picked for TC 52 Notification")
    public void validateE2eTC52NonEbtPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11731, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify Non EBT Rush order staged for TC52 Notification")
    public void validateE2eTC52NonEbtStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11731_bulkStaging_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify Non EBT rush order picked up and paid for TC52 Notification")
    public void validateE2eTC52NonEbtPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11731, ExcelUtils.BULK_STAGING_TC_11731);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11731);
    }
}
