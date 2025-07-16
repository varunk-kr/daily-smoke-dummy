package com.krogerqa.web.cases.mafpCiaoChanges;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario MAFP_CIAO_SCENARIO_TC_10434 Web Flows -
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store>
 * Modify the order by adding one more item into Order and perform batching
 * Verify New Non-EBT modified Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue  for the modified order>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowMafpModifyOrderPclTest extends BaseTest {

    public static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_10434_web_part1_modifyOrder_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed and modified it by adding 1 new item for MAFP Ciao Order")
    public void validateE2eNewNonEbtMafpModifyOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10434);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10434, testOutputData);
        baseCommands.wait(3);
        testOutputData.put(ExcelUtils.BATCH_ORDER, String.valueOf(true));
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10434, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10434_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10434_1_1, testOutputData);
        baseCommands.wait(5);
        testOutputData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        testOutputData.put(ExcelUtils.CIAO_SUBSTITUTE_ITEM_ACCEPT, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10434_web_part2_modifyOrder_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for MAFP modified Ciao Order")
    public void validateE2eNonEbtMafpModifyOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10434_web_part3_modifyOrder_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for MAFP modified Ciao Order")
    public void validateE2eNonEbtMafpModifyOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10434_web_part4_modifyOrder_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for MAFP modified Ciao Order")
    public void validateE2eNonEbtMafpModifyOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME));
    }
}
