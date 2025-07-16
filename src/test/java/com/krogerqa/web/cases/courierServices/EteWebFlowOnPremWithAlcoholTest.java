package com.krogerqa.web.cases.courierServices;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-9593 Web Flows -
 * Submit Non-EBT Shipt Delivery order in Kroger.com and perform Batching
 * Verify New Non-EBT Shipt Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up, Paid and Delivered in Cue and validate K-log
 */
public class EteWebFlowOnPremWithAlcoholTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_web_part1_nonEbt_shiptDeliveryOnPrem_9593", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify Shipt Delivery order placed")
    public void validateE2eNewNonEbtOnPremOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9593, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.COURIER_SERVICES_9593);
        testOutputData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.COURIER_SERVICES_9593, testOutputData);
    }

    @Test(groups = {"ete_web_part2_nonEbt_shiptDeliveryOnPrem_9593", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify Shipt Delivery order picked")
    public void validateE2eNonEbtOnPremOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9593, ExcelUtils.COURIER_SERVICES_9593);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.COURIER_SERVICES_9593, testOutputData);
    }

    @Test(groups = {"ete_web_part3_nonEbt_shiptDeliveryOnPrem_9593", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for Shipt Delivery order")
    public void validateE2eNonEbtOnPremOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9593, ExcelUtils.COURIER_SERVICES_9593);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_web_part4_nonEbt_shiptDeliveryOnPrem_9593", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E Shipt Delivery order fulfillment (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtOnPremOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9593, ExcelUtils.COURIER_SERVICES_9593);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.COURIER_SERVICES_9593);
    }
}
