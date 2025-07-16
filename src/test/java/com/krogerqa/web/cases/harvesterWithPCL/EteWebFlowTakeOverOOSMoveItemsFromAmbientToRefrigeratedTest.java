package com.krogerqa.web.cases.harvesterWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-4529 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 * Note:Execute 2 web testcases before running native
 */


public class EteWebFlowTakeOverOOSMoveItemsFromAmbientToRefrigeratedTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> baseOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_4529_web_part1_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed and staged for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNewNonEbtPclOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_SCENARIO_TC_4529_1, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.eteOrderApi(ExcelUtils.PCL_SCENARIO_TC_4529_1, ExcelUtils.PCL_SCENARIO_TC_4529, testOutputData);
        baseOrderData = testOutputData;
    }

    @Test(groups = {"ete_pcl_tc_4529_web_part2_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify new order placed for for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNewNonEbtPclTakeOverAssigningItemMovementViaContainerLookupOrderPlaced() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_4529);
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCueReuseStagedPcl(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529_1, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4529_web_part3_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNewNonEbtPclTakeOverAssigningItemMovementViaContainerLookupOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_4529, testOutputData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.PCL_SCENARIO_TC_4529, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4529_web_part4_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNewNonEbtPclTakeOverAssigningItemMovementViaContainerLookupOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4529_web_part5_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order PickedUp and paid for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNewNonEbtPclTakeOverAssigningItemMovementViaContainerLookupOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_4529);
    }
}
