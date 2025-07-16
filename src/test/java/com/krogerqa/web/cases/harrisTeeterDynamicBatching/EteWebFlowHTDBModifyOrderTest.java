package com.krogerqa.web.cases.harrisTeeterDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_HT_TC_14378 Web Flows -
 * Submit Non-EBT Pickup order in Kroger.com for Multi-threaded store>
 * Modify the order by adding one more item into Order and perform batching
 * Verify New Non-EBT modified Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue  for the modified order>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is cancelled from Ciao
 */
public class EteWebFlowHTDBModifyOrderTest extends BaseTest {
    public static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_14378_web_part1_dyb_ht_modify_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed and modified it by adding 1 new item")
    public void validateE2eNewNonEbtDybHarrisTeeterModifyOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_HT_TC_14378);
        testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.HARRIS_TEETER_DYB, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_HT_TC_14378, testOutputData);
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.DB_HT_TC_14378, ExcelUtils.DB_HT_TC_14378_1, ExcelUtils.DB_HT_TC_14378_1_1, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_14378_web_part2_dyb_ht_modify_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT-Modify order")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_14378_web_part3_dyb_ht_modify_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT-Modify order")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_14378_web_part4_dyb_ht_modify_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "\tValidate E2E fulfillment for Modify order and Cancel from Ciao assigning PCL (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtDybHarrisTeeterModifyOrder() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCanceledStatusInCue(testOutputData);
    }
}