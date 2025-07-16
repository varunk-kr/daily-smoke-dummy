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
 * Scenario FFILLSVCS-TC-14372 Web Flows -
 * Submit Non-EBT Pickup order for Previous day for Single-threaded store using API and perform Batching >
 * Validate picking for carryover trolleys and verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowHarrisTeeterCarryOverTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_dbHt_tc_14372_web_part1_carryOver_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify non-EBT carry over order placed")
    public void validateE2eDbHtCarryOverPclNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14372, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_CARRYOVER_ORDER_WITHOUT_PICKING, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.HARRIS_TEETER_DYB_14372, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HARRIS_TEETER_DYB_14372, testOutputData);
    }

    @Test(groups = {"ete_dbHt_tc_14372_web_part2_carryOver_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify carry over order picked")
    public void validateE2eDbHtCarryOverPclOrderPicked() {
        testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14372, ExcelUtils.HARRIS_TEETER_DYB_14372);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.HARRIS_TEETER_DYB_14372, testOutputData);
    }

    @Test(groups = {"ete_dbHt_tc_14372_web_part3_carryOver_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify carry over order staged")
    public void validateE2eDbHtCarryOverPclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14372, ExcelUtils.HARRIS_TEETER_DYB_14372);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dbHt_tc_14372_web_part4_carryOver_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E Carry Over & Take Over order fulfillment (Picking and Staging using Harvester Native app)")
    public void validateE2eDbHtCarryOverPclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14372, ExcelUtils.HARRIS_TEETER_DYB_14372);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HARRIS_TEETER_DYB_14372);
    }
}