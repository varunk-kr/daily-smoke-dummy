package com.krogerqa.web.cases.pclOversize;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12223 Web Flows -
 * Submit Non-EBT pclOversize Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowTakeOverMoveItemsPickingTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12223_web_part1_itemMovementViaPicking_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for take over item Movement via picking pcl order")
    public void validateE2eNewNonEbtPclTakeOverItemMovementViaPickingOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12223);
        testOutputData.put(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_CARRYOVER,String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.TAKE_OVER_WITH_ITEM_MOVEMENT, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12223, testOutputData);
        testOutputData.put(ExcelUtils.IS_CARRYOVER,String.valueOf(false));
        ExcelUtils.writeToExcel(ExcelUtils.PCL_OVERSIZE_TC_12223, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12223_web_part2_itemMovementViaPicking_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for take over item Movement via picking pcl order")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPickingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12223, ExcelUtils.PCL_OVERSIZE_TC_12223);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12223, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12223_web_part3_itemMovementViaPicking_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged for take over item Movement via picking pcl order")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPickingOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12223, ExcelUtils.PCL_OVERSIZE_TC_12223);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12223_web_part4_itemMovementViaPicking_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for take over item Movement via picking pcl order")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPickingOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12223, ExcelUtils.PCL_OVERSIZE_TC_12223);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12223);
    }
}
