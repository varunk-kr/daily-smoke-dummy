package com.krogerqa.web.cases.anchorStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
/**
 * Scenario ANCHOR_STAGING_TC_5314 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowAnchorStagingPCLCancelOrderAfterStagingOneContainerTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_5314_anchor_staging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for anchor staging cancel order")
    public void validateE2eNonEbtPclAnchorStagingCancelAfterStagingNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.ANCHOR_STAGING_TC_5314);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.ANCHOR_STAGING_TC_5314, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5314_anchor_staging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for anchor staging cancel order")
    public void validateE2eNonEbtPclAnchorStagingCancelAfterStagingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_STAGING_TC_5314, ExcelUtils.ANCHOR_STAGING_TC_5314);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.ANCHOR_STAGING_TC_5314, testOutputData);
        cueOrderValidation.updatePclMapsBeforeStaging(ExcelUtils.ANCHOR_STAGING_TC_5314, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5314_anchor_staging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and cancel the order from cue")
    public void validateE2eNonEbtPclAnchorStagingOrderStagedStatusAndCancelOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_STAGING_TC_5314, ExcelUtils.ANCHOR_STAGING_TC_5314);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCancelOrderFromCue(testOutputData);
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}