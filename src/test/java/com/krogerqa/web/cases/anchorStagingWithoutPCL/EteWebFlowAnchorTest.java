package com.krogerqa.web.cases.anchorStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-5308 web flows
 * Submit EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue and validate the details for Not Ready container>
 * After Anchor Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowAnchorTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_anchor_tc_5308_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for non-EBT anchor staging order")
    public void validateE2eNewNonEbtAnchorStagingOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_SCENARIO_TC_5308);
        testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_INVALID_STAGING_LOCATION_VALIDATION, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_CUE_STAGING_ZONE_VALIDATION, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5308_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT anchor staging order")
    public void validateE2eNonEbtAnchorStagingOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5308, testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5308_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT anchor staging order")
    public void validateE2eNonEbtAnchorStagingOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5308, ExcelUtils.ANCHOR_SCENARIO_TC_5308);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_anchor_tc_5308_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT anchor staging order")
    public void validateE2eNonEbtAnchorStagingOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5308);
    }
}
