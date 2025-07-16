package com.krogerqa.web.cases.anchorStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario TC_5323 Web Flows -
 * Submit Non-EBT Pcl shipt delivery order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowAnchorStaginglShiptDeliveryTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify shipt delivery pcl order placed for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryNewOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(true));
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify shipt delivery pcl order picked for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323, ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify shipt delivery pcl order staged and checked-in for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323, ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        testOutputData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(false));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify shipt delivery pcl order picked up and paid for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323, ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5323);
    }
}
