package com.krogerqa.web.cases.dynamicBatchingWithOS;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario  FFILLSVCS-TC-13618 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store for Db os+ order>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging and item movement validate Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbOSBaseItemMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_dybOS_tc_13618_web_part1_ItemMovement", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for DyB Os+ large order")
    public void validateOrderPlacedForDyBOsItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618);
        testOutputData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, testOutputData);
        CueOrderDetailsPage.verifyDBOSBatchingValidation(testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13618_web_part2_ItemMovement", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for DyB Os+ large order")
    public void validatePickedValidationForDyBOSItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, ExcelUtils.DYNAMIC_BATCH_OS_TC_13618);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13618_web_part3_ItemMovement", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for DyB Os+ large order")
    public void validateStagedStatusValidationForDyBOSItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, ExcelUtils.DYNAMIC_BATCH_OS_TC_13618);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13618_web_part4_ItemMovement", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for DyB Os+ large order")
    public void validatePickedAnPaidForDyBOSItemMovement() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618, ExcelUtils.DYNAMIC_BATCH_OS_TC_13618);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_OS_TC_13618);
    }
}
