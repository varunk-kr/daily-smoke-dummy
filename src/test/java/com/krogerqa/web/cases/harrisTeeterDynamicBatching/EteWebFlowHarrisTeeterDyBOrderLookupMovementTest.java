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
 * Scenario FFILLSVCS-TC-14380 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Harris Teeter Multi-threaded store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowHarrisTeeterDyBOrderLookupMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.HARRIS_TEETER_DYB_14380);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        testOutputData.put(ExcelUtils.HARRIS_TEETER_DYB, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HARRIS_TEETER_DYB_14380);
    }
}
