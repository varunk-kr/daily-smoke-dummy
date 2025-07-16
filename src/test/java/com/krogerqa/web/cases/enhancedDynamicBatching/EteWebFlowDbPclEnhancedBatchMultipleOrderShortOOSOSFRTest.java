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
 * Scenario FFILLSVCS-TC-10903 Web Flows - Enhanced Batching.
 * Submit 3 Non-EBT Pcl Pickup orders in different time slots with CollapseTemp AM,RE and OS FR in Kroger.com for Multi-threaded store and perform Enhanced Batching >
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Complete selecting and Stage the orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowDbPclEnhancedBatchMultipleOrderShortOOSOSFRTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS FR are placed")
    public void validateE2eEnhancedBatchingMultipleOrdersWithDifferentTimeSlotPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10903);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10903_1);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10903_2);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_AM_RE_OSFR);
        firstOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP_OOS, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10903_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10903_2, thirdOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData, thirdOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS_FR are picked")
    public void validateE2eEnhancedBatchingMultipleOrdersWithDifferentTimeSlotPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_TC_10903, firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS_FR are staged and checked-in")
    public void validateE2eEnhancedBatchingMultipleOrdersWithDifferentTimeSlotStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_multipleOrder_short_OOS_pcl_tc_10903_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with different time slots and Collapse temp AM,RE and OS_FR are picked up and paid")
    public void validateE2eEnhancedBatchingMultipleOrdersWithDifferentTimeSlotPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10903, ExcelUtils.EB_PCL_TC_10903);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10903);
    }
}