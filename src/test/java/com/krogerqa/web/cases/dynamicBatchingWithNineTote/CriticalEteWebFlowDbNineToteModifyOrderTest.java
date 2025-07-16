package com.krogerqa.web.cases.dynamicBatchingWithNineTote;

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
 * Scenario  FFILLSVCS-TC-13624 Web Flows -
 * Submit Non-EBT Pcl DB OS Plus Pickup order in Kroger.com for Multi-threaded store >
 * Modify the order from Kroger Seamless portal and perform Batching-Enhanced Batching using request trolleys >
 * Validate SMOS items are batched with SMOS trolleys
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class CriticalEteWebFlowDbNineToteModifyOrderTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_dyb_9tote_tc_15073_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify modify order placed for 9 tote batching")
    public void validateE2eDBOSModifyOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_9TOTE_15073);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_9TOTE_15073, testOutputData);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_SMALL_OS_ITEM_PRESENT, String.valueOf(true));
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AM_RE_OSFR_SMOS);
        testOutputData.put(ExcelUtils.MAX_SMOS_COUNT, "8");
        testOutputData = cueOrderValidation.modifyOrder(ExcelUtils.DYNAMIC_BATCH_9TOTE_15073, ExcelUtils.DYNAMIC_BATCH_9TOTE_15073_1, ExcelUtils.DYNAMIC_BATCH_9TOTE_15073_1_1, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15073_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify modify order picked for 9 tote batching")
    public void validateDBOSModifyOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME), testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15073_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify modify order staged and checked-in for 9 tote batching")
    public void validateE2eDBOSModifyOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_dyb_9tote_tc_15073_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify modify order picked up and paid for 9 tote batching")
    public void validateE2eDBOSModifyOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(testOutputData.get(ExcelUtils.MODIFY_SCENARIO_NAME));
    }
}
