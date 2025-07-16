package com.krogerqa.web.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10442
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue >
 * verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Accept the substitution via 2-way communication>
 * Verify order is displayed in Dash >
 * After de-staging, verify order is dropped from Dash >
 * Verify Non-EBT order is Picked Up and Paid in Cue
 */

public class EteWebFlowWithSellByWeightSubstituteAndSellByUnitTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_pcl_tc_10442_web_part1_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "description = Verify order placed for sell by unit, weight and other items")
    public void validateE2ESellByWeightSubsMafpCiaoChangesNewOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        testOutputData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        testOutputData.put(ExcelUtils.MAFP_NOT_READY_FOR_ORDER_ADJUSTMENT_WEIGHT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10442_web_part2_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked is Cue")
    public void validateE2ESellByWeightSubsMafpCiaoChangesOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10442_web_part3_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and accept response for  partially substituted sell by weight item")
    public void validateE2ESellByWeightSubsMafpCiaoChangesOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        pickingServicesHelper.triggerSubCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        pickingServicesHelper.sendSubstitutionCustomerResponse(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10442_web_part4_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Validate order is picked up and paid")
    public void validateE2ESellByWeightSubsMafpCiaoChangesOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
    }
}
