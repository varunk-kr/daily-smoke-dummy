package com.krogerqa.web.cases.mafpCiaoChanges;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-10463
 * Submit snap-EBT non-Pcl Pickup order in Kroger.com for single-threaded store>
 * Verify New snap-EBT Order Details in Cue >
 * verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After de-staging, verify order is dropped from Dash >
 * Verify snap-EBT order is Picked Up and Paid in Cue
 */

public class EteWebFlowWithPartialSellByUnitAndSellByWeightRandomWeightTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_10463_web_part1_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "description = Verify order placed for partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclMafpCiaoChangesNewOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        testOutputData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10463_web_part2_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclMafpCiaoChangesOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10463_web_part3_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in partially fulfilled nonPCL singleThread mafp order")
    public void validateE2eNonPclMafpCiaoChangesOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10463_web_part4_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for nonPCL singleThread mafp order")
    public void validateE2eNonPclMafpCiaoChangesOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10463);
    }
}
