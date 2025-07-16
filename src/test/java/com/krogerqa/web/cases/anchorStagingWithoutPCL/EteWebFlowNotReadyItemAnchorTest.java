package com.krogerqa.web.cases.anchorStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
/**
 * FFILLSVCS-TC-5309
 * Submit Snap EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Snap EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue and validate the details for Not Ready container>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowNotReadyItemAnchorTest extends BaseTest{
        static HashMap<String, String> testOutputData = new HashMap<>();
        KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
        CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
        LoginWeb loginWeb = LoginWeb.getInstance();

        @Test(groups = {"ete_web_part1_NotReadyItemWithAnchor", "ete_web_part1_snapEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Not Ready Snap EBT")
        public void validateE2eNewStatusForNotReadyEbtOrder() {
            testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_SCENARIO_TC_5309);
            testOutputData.put(ExcelUtils.IS_ANCHOR_STAGING_ZONE, String.valueOf(true));
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5309, testOutputData);
        }

        @Test(groups = {"ete_web_part2_NotReadyItemWithAnchor", "ete_web_part2_snapEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked in Cue marking item as Not Ready")
        public void validateE2ePickedStatusForNotReadyEbtOrder() {
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.ANCHOR_SCENARIO_TC_5309, testOutputData);
        }

        @Test(groups = {"ete_web_part3_NotReadyItemWithAnchor", "ete_web_part3_snapEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged with Anchor Staging and checked-in for not ready Snap EBT order")
        public void validateE2eStagedStatusForNotReadyEbtOrder() {
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyStagedStatusInCue(testOutputData);
        }

        @Test(groups = {"ete_web_part4_NotReadyItemWithAnchor", "ete_web_part4_snapEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for not Ready Snap EBT order")
        public void validateE2ePaidStatusForNotReadyEbtOrder() {
            cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.ANCHOR_SCENARIO_TC_5309);
        }
    }
