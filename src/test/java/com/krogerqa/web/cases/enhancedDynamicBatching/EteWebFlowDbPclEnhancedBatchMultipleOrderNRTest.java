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
 * Scenario FFILLSVCS-TC-10902 Web Flows - Enhanced Batching.
 * Submit 3 Non-EBT Pcl Pickup orders in same time slots with CollapseTemp All in Kroger.com for Multi-threaded store and perform Enhanced Batching >
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbPclEnhancedBatchMultipleOrderNRTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with same time slots and Collapse temp ALL are placed")
    public void validateE2eEnhancedBatchingMultipleOrdersWithSameTimeSlotPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10902);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10902_1);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10902_2);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
        firstOrderTestData.put(ExcelUtils.SINGLE_TROLLEY, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        secondOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        thirdOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10902_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10902_2, thirdOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData, thirdOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with same time slots and Collapse temp ALL are picked")
    public void validateE2eEnhancedBatchingMultipleOrdersWithSameTimeSlotPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
        firstOrderTestData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(false));
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.EB_PCL_TC_10902, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with same time slots and Collapse temp ALL are staged and checked-in")
    public void validateE2eEnhancedBatchingMultipleOrdersWithSameTimeSlotStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_NR_pcl_tc_10902_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with same time slots and Collapse temp ALL are picked up and paid")
    public void validateE2eEnhancedBatchingMultipleOrdersWithSameTimeSlotPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10902, ExcelUtils.EB_PCL_TC_10902);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10902);
    }
}
