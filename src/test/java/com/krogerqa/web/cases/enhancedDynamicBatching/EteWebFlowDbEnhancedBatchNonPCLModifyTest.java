package com.krogerqa.web.cases.enhancedDynamicBatching;

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
 * Scenario EB_NON_PCL_TC-10938 Web Flows -
 * Submit Snap-EBT Pickup order in Kroger.com for Dynamic batching Multi-threaded store>
 * Modify the order by adding one more item into Order and perform batching
 * Verify New Snap-EBT modified Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue  for the modified order>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowDbEnhancedBatchNonPCLModifyTest extends BaseTest {

    public static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_web_part1_snapEbt", "ete_web_part1_snapEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for Dynamic Batching and modifies the items")
    public void validateE2eDBEnhanceSnapEbtModifyOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_NON_PCL_TC_10938);
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AMRE);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_NON_PCL_TC_10938, testOutputData);
        baseCommands.browserBack();
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.EB_NON_PCL_TC_10938, ExcelUtils.EB_NON_PCL_TC_10938_1, ExcelUtils.EB_NON_PCL_TC_10938_1_1, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_web_part2_snapEbt", "ete_web_part2_snapEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT-Dynamic Batching Modify order")
    public void validateE2eDBEnhanceSnapEbtModifyOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_web_part3_snapEbt", "ete_web_part3_snapEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT-Dynamic Batching Modify order")
    public void validateE2eDBEnhanceSnapEbtModifyOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }


    @Test(groups = {"ete_db_enhancedBatching_nonPcl_modifyOrder_web_part4_snapEbt", "ete_web_part4_snapEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT-Dynamic Batching Modify order")
    public void validateE2eDBEnhanceSnapEbtModifyOrderOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME));
    }
}