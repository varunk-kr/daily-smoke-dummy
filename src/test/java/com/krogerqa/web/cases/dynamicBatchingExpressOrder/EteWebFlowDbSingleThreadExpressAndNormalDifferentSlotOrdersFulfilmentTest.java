package com.krogerqa.web.cases.dynamicBatchingExpressOrder;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_EXPRESS_ORDER_TC_14054 Web Flows -
 * Submit express order and normal Pickup orders in Kroger.com for Single-thread store >
 * Verify Batching is completed >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels for express and normal orders>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue for both the orders>
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */
public class EteWebFlowDbSingleThreadExpressAndNormalDifferentSlotOrdersFulfilmentTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_db_expressOrder_tc_14054_singleThread_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for db non-EBT pcl express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyExpressOrderInitialCount();
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
    }

    @Test(groups = {"ete_db_expressOrder_tc_14054_singleThread_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order batched for both express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderBatched() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        firstOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
        secondOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING,String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING,String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_tc_14054_singleThread_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order picked for express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_tc_14054_singleThread_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order staged and checked-in for normal and express orders")
    public void validateE2eDbPclOrderExpressAndNormalStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_db_expressOrder_tc_14054_singleThread_web_part5_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_singleThreaded"}, description = "Verify order picked up and paid for express and normal orders")
    public void validateE2eDbPclOrderExpressAndNormalPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
         cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
         cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
         loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
         cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
         baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
         cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
    }
}