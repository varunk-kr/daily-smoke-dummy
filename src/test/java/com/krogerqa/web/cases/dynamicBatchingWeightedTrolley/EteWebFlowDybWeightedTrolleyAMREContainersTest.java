package com.krogerqa.web.cases.dynamicBatchingWeightedTrolley;

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
 * Scenario DB_EXPRESS_ORDER_TC_13140 Web Flows -
 * Submit 2 orders in DyB store 1 with AM RE containers and 1 with OS FR Containers>
 * Request trolley to batch orders and validate order 2 is not batched>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */

public class EteWebFlowDybWeightedTrolleyAMREContainersTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_db_weightedTrolley_13140_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify orders placed and batched for Dyb Weighted Trolley")
    public void validateE2eDbPclWeightedTrolleyOrderPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_WEIGHTED_TROLLEY_13140_1, secondOrderTestData);
        baseCommands.webpageRefresh();
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, firstOrderTestData);
    }

    @Test(groups = {"ete_db_weightedTrolley_13140_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for db non-EBT Weighted Trolley")
    public void validateE2eDbPclWeightedTrolleyOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, firstOrderTestData);
    }

    @Test(groups = {"ete_db_weightedTrolley_13140_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl Weighted Trolley")
    public void validateE2eDbPclWeightedTrolleyOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_db_weightedTrolley_13140_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl Weighted Trolley")
    public void validateE2eDbPclWeightedTrolleyOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TROLLEY_13140, ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_WEIGHTED_TROLLEY_13140);
    }
}
