package com.krogerqa.web.cases.dynamicBatchingWithoutPCL;

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
 * Scenario FFILLSVCS-TC-5580 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup order in Kroger.com for Dynamic Batching store and perform Batching >
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue >
 * Before batching cancel one of the order and pick the remaining trolley for non-cancelled order >
 * Stage the non-cancelled order
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowMultipleOrderCancelTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_db_tc_5580_web_part1_multipleOrderCancel", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT orders placed in dynamic batching store")
    public void validateE2eNewMultipleOrdersDynamicBatchingPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_5580);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_CANCEL_SCENARIO, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        baseCommands.wait(20);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        baseCommands.browserBack();
        secondOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_CANCEL_SCENARIO, String.valueOf(true));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.searchOrderId(firstOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderValidation.verifyTrolleysNotCreatedForCanceledOrder();
    }

    @Test(groups = {"ete_db_tc_5580_web_part2_multipleOrderCancel", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify non-EBT order picked in same store")
    public void validateE2eMultipleOrdersDynamicBatchingPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5580_web_part3_multipleOrderCancel", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify non-EBT order staged and checked-in in same store")
    public void validateE2eMultipleOrdersDynamicBatchingStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5580_web_part4_multipleOrderCancel", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify non-EBT order picked up and paid in same store")
    public void validateE2eMultipleOrdersDynamicBatchingPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
    }
}
