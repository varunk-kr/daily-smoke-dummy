package com.krogerqa.web.cases.dynamicBatchingWithPCL;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;


/**
 * Scenario DB_PCL_TC_9499 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */

public class EteWebFlowDbPclSubsAndMoveItemsToExistingContainerTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9499_existingContainerItemMovement_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT db pcl order for item Movement from one container to existing container while picking from substitution screen")
    public void validateE2eNonEbtItemMovementPclOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_existingContainerItemMovement_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT db pcl order for item Movement from one container to existing container while picking from substitution screen")
    public void validateE2eNonEbtItemMovementPclOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499, testOutputData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_existingContainerItemMovement_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT db pcl order for item Movement from one container to existing container while picking from substitution screen")
    public void validateE2eNonEbtItemMovementPclOrderStaged() {
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499);
        pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_existingContainerItemMovement_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl order for item Movement from one container to existing container while picking from substitution screen")
    public void validateE2eNonEbtItemMovementPclOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9499);
    }
}
