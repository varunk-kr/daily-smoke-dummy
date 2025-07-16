package com.krogerqa.web.cases.harrisTeeterDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
/**
 * FFILLSVCS-TC-14373 Web Flows -
 * Submit Non-EBT Pickup order in HarrisTeeter.com for Single-threaded Harris Teeter Dynamic Batching Enabled store >
 * Verify New Non-EBT Harris Teeter Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowHarrisTeeterNonPCLTest extends BaseTest{
        static HashMap<String, String> testOutputData = new HashMap<>();
        KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
        CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
        LoginWeb loginWeb = LoginWeb.getInstance();

        @Test(groups = {"ete_harrisTeeterDyB_nonPcl_14373_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify Harris Teeter order placed on Non PCL Dyb store")
        public void validateE2eDybHarrisTeeterNonPclOrder() {
            testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.HARRIS_TEETER_DYB_14373);
            testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
            testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
            testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HARRIS_TEETER_DYB_14373, testOutputData);
        }

        @Test(groups = {"ete_harrisTeeterDyB_nonPcl_14373_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify Harris Teeter order picked")
        public void validateE2eDybHarrisTeeterNonPclOrderPicked() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14373, ExcelUtils.HARRIS_TEETER_DYB_14373);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.HARRIS_TEETER_DYB_14373, testOutputData);
        }

        @Test(groups = {"ete_harrisTeeterDyB_nonPcl_14373_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify Harris Teeter order staged and checked-in")
        public void validateE2eDybHarrisTeeterNonPclOrderStaged() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14373, ExcelUtils.HARRIS_TEETER_DYB_14373);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyStagedStatusInCue(testOutputData);
        }

        @Test(groups = {"ete_harrisTeeterDyB_nonPcl_14373_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Validate E2E Harris Teeter order fulfillment (Picking and Staging Using Harvester App)")
        public void validateE2eDybHarrisTeeterNonPclOrderPaid() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14373, ExcelUtils.HARRIS_TEETER_DYB_14373);
            cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HARRIS_TEETER_DYB_14373);
        }
    }