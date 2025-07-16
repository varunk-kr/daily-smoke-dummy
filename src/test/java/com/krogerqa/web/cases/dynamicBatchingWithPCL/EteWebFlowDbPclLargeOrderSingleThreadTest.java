package com.krogerqa.web.cases.dynamicBatchingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario Web Flows -
 * Submit Non-EBT Pcl Pickup large order in Kroger.com for Single-threaded store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowDbPclLargeOrderSingleThreadTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_db_pcl_largeOrder_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for db non-EBT pcl large order")
    public void validateE2eDbPclLargeOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYB_LARGE_ORDER);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_MULTIPLE_OS_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYB_LARGE_ORDER, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_largeOrder_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for db non-EBT pcl large order")
    public void validateE2eDbPclLargeOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYB_LARGE_ORDER, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_largeOrder_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl large order")
    public void validateE2eDbPclLargeOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_largeOrder_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl large order")
    public void validateE2eDbPclLargeOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYB_LARGE_ORDER, ExcelUtils.DYB_LARGE_ORDER);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYB_LARGE_ORDER);
    }
}