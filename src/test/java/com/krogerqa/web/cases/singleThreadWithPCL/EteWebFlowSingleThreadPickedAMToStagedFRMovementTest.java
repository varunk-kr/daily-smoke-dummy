package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-9010 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * Stage only one  container and verify in Cue
 * Move some items from Picked AM container to existing Staged FR container
 * Stage remaining container and checkout from Cue
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowSingleThreadPickedAMToStagedFRMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9010_existingContainerItemMovement_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify non-EBT singleThread pcl order placed for Picked to Staged container item Movement from order Lookup screen")
    public void validateE2eNonEbtItemMovementPclSingleThreadOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9010_existingContainerItemMovement_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for non-EBT singleThread pcl order for Picked to Staged container item Movement from order Lookup screen")
    public void validateE2eNonEbtPclSingleThreadItemMovementOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9010_existingContainerItemMovement_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify in Cue if the container to which some items should be moved is Staged")
    public void validateE2eNonEbtPclSingleThreadItemMovementToContainerStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyToContainerStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9010_existingContainerItemMovement_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order staged and checked-in for non-EBT singleThread pcl order for Picked to Staged container item Movement from order Lookup screen")
    public void validateE2eNonEbtPclSingleThreadItemMovementOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9010_existingContainerItemMovement_web_part5_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_singleThreaded"}, description = "Verify order picked up and paid for non-EBT singleThread pcl order for Picked to Staged container item Movement from order Lookup screen")
    public void validateE2eNonEbtPclSingleThreadItemMovementOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9010, ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9010);
    }
}
