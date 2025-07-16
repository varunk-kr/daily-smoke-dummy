package com.krogerqa.web.cases.mafpCiaoChanges;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-10433
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue >
 * verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After deStaging, verify order is dropped from Dash >
 * Verify Non-EBT order is Picked Up and Paid in Cue
 */

public class EteWebFlowWithUnitAndWeightSubsOrderAdjustmentTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_10433_web_part1_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "description = Verify order placed for mafp order")
    public void validateE2ePclMafpCiaoChangesNewOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433);
        testOutputData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        testOutputData.put(ExcelUtils.MAFP_NOT_READY_FOR_ORDER_ADJUSTMENT_WEIGHT, String.valueOf(true));
        testOutputData.put(ExcelUtils.CIAO_SUBSTITUTE_ITEM_ACCEPT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10433_web_part2_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for mafp order")
    public void validateE2ePclPclMafpCiaoChangesOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10433_web_part3_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for mafp order")
    public void validateE2ePclPclMafpCiaoChangesOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10433_web_part4_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate E2E non Ebt order for MAFP Ciao changes (Picking and Staging Using Harvester Native app)")
    public void validateE2ePclPclMafpCiaoChangesOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10433);
    }
}
