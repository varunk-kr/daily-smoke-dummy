package com.krogerqa.web.cases.singleThreadWithPCL;

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
 * Scenario FFILLSVCS-TC-9000 Web Flows -
 * Submit NON-EBT Pcl Pickup order for various fulfillment types in Kroger.com for Single-threaded store and perform Batching >
 * Verify New NON-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class SmokeEteWebFlowAllFulfillmentTypeSmokeSingleThreadTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_SingleThread_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for various fulfillment types for Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeSingleOrderTestOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_SingleThread_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for various fulfillment types for Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeSingleOrderTestOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_SingleThread_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for various fulfillment types for Non-EBT pcl Single Thread order")
    public void validatePclFulfillmentTypesSmokeSingleOrderTestOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.SINGLE_THREAD_PCL_TC_9000, testOutputData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_fulfillment_smokeTest_SingleThread_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify E2E non ebt order for various fulfillment types for PCL Single Thread order (Picking and Staging Using Harvester Native app)")
    public void validatePclFulfillmentTypesSmokeTestSingleThreadOrderPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9000,ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9000);
    }
}
