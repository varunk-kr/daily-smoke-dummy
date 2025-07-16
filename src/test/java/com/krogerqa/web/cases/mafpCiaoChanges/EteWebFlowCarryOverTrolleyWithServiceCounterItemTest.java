package com.krogerqa.web.cases.mafpCiaoChanges;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-10432
 * Submit Non-EBT Pcl Pickup order in Kroger.com for only service counter items in Multi-threaded store and perform Batching >
 * * Verify New Non-EBT Order Details in Cue >
 * Pick few trolleys the previous day using API and mark one of the trolley as out of stock and verify Picking status in Cue
 * Carryover remaining trolleys and after selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After deStaging, verify order is dropped from Dash >
 * Verify Non-EBT order is Picked Up and Paid in Cue
 */
public class EteWebFlowCarryOverTrolleyWithServiceCounterItemTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_10432_web_part1_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "description = Verify order placed for service counter items")
    public void validateE2ePclMafpCiaoChangesCarryOverOrder() {
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        testOutputData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10432_web_part2_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for carry over non-EBT order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10432_web_part3_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for carry over non-EBT order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10432_web_part4_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for carry over non-EBT order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
    }
}
