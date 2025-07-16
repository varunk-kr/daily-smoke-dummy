package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-7464 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteByobWebByobContainerLookupItemMovementToExistingAMTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_7464_byob_web_part1_itemMovementViaContainerLookup_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eByobNewNonEbtPclItemMovementViaContainerLookupOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7464);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7464, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_7464_byob_web_part2_itemMovementViaContainerLookup_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eByobNonEbtPclItemMovementViaContainerLookupOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7464, ExcelUtils.BYOB_PCL_TC_7464);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7464, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_7464_byob_web_part3_itemMovementViaContainerLookup_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eByobNonEbtPclItemMovementViaContainerLookupOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7464, ExcelUtils.BYOB_PCL_TC_7464);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_7464_byob_web_part4_itemMovementViaContainerLookup_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E order fulfillment for Item Movement via pre weigh order (Picking and Staging Using Harvester Native app) ")
    public void validateE2eByobNonEbtPclItemMovementViaContainerLookupOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7464, ExcelUtils.BYOB_PCL_TC_7464);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7464);
    }
}