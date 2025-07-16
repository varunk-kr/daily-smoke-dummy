package com.krogerqa.web.cases.dynamicBatchingWithOS;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-13620 Web Flows -
 * Submit a Non-EBT Pickup order in Kroger.com for Multi-threaded store and perform dynamic Batching >
 * Verify New Non-EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowDbOsReusePclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_dybOS_tc_13629_web_part1_reusePcl", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for db pcl oversize order")
    public void validateE2eNewNonEbtDbOSPclOversizeOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629);
        testOutputData.put(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DB_OS_REUSE_PCL_SPLIT_TROLLEY, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629, testOutputData);
        CueOrderValidation.updateMapsForDbOsSplitTrolley(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629, testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13629_web_part2_reusePcl", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for db pcl oversize order")
    public void validateE2eNonEbtDbOSPclOversizePicking() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629, ExcelUtils.DYNAMIC_BATCH_OS_TC_13629);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickingStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13629_web_part3_reusePcl", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for db pcl oversize order")
    public void validateE2eNonEbtDbOSPclOversizeStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629, ExcelUtils.DYNAMIC_BATCH_OS_TC_13629);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13629_web_part4_reusePcl", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for db pcl oversize order")
    public void validateE2eNonEbtDbOSPclOversizePaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629, ExcelUtils.DYNAMIC_BATCH_OS_TC_13629);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_OS_TC_13629);
    }
}
