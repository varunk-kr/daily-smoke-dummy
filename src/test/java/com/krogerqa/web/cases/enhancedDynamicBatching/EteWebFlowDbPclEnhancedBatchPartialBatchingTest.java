package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.api.PickingServicesHelper;
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
 * Scenario FFILLSVCS-TC-10907 Web Flows - Enhanced Batching.
 * Create 1 order, perform partially Enhanced Batching>
 * Create another order, completely batch both orders with Enhanced Batching
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbPclEnhancedBatchPartialBatchingTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify 1 order placed then partially batched and another placed, completely batched")
    public void validateE2eEnhancedBatchPartialBatchingOrderCreation() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10907);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        pickingServicesHelper.partialBatching(firstOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        baseCommands.webpageRefresh();
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        krogerSeamLessPortalOrderCreation.logoutAndLoginKCP();
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10907_1);
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        secondOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.OSFR);
        baseCommands.wait(8);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10907, firstOrderTestData);
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_OS_ORDER, String.valueOf(true));
        secondOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify second Non EBT Order batched with Enhanced Batching AM,RE and OSFR are picked")
    public void validateE2eEnhancedBatchPartialBatchingPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907, ExcelUtils.EB_PCL_TC_10907);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10907, firstOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.EB_PCL_TC_10907, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify second Non EBT Order batched with Enhanced BatchingAM,RE and OS FR are staged and checked-in")
    public void validateE2eEnhancedBatchPartialBatchingStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify second Non EBT Order batched with Enhanced Batching AM,RE and OS FR are picked up and paid")
    public void validateE2eEnhancedBatchPartialBatchingPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10907_1);
    }
}
