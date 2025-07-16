package com.krogerqa.web.cases.dynamicBatchingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario DYNAMIC_BATCH_PCL_TC_9516 Web Flows -
 * Submit 2 Non-EBT Pcl Rush Pickup orders and 1 rush order in Kroger.com for Multi-threaded store and cancel one order from cue
 * Perform Batching using request trolleys for remaining order
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowDbPclMultipleRushAndNormalOrderCancelTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple orders placed for non-EBT order in same store")
    public void validateE2eDbPclNewMultipleRushAndNormalOrdersPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_PCL_TC_MULTIPLE_9516, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        cueOrderDetailsPage.verifyTrolleysNotCreated();
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_OS_ORDER,String.valueOf(true));
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        thirdOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, thirdOrderTestData);
        secondOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders picked for non-EBT order in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, thirdOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_web_part3_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_web_part4_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
    }
}