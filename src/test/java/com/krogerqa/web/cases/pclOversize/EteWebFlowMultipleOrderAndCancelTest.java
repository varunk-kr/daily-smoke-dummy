package com.krogerqa.web.cases.pclOversize;

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
 * Scenario PCL_TC_12131 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify common trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting one trolley cancel one of the order and pick the remaining trolley in non-cancelled order >
 * Stage the non-cancelled order
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 * Note : Execute 2 web method first and  Execute 2 native methods after the 2 web methods
 */
public class EteWebFlowMultipleOrderAndCancelTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();


    @Test(groups = {"ete_pclOversize_tc_12131_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple orders placed for non-EBT order in same store")
    public void validateE2eNewMultipleOrdersPlaced() {
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12131);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        firstOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12131, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12131_1, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.PCL_OVERSIZE_TC_12131, firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12131_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify 1 order cancelled and other order picked for non-EBT order in same store")
    public void cancelFirstOrderAndUpdateSecondOrderData() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131, ExcelUtils.PCL_OVERSIZE_TC_12131);
        krogerSeamLessPortalOrderCreation.verifyCancelOrder(firstOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        baseCommands.webpageRefresh();
        baseCommands.browserBack();
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12131_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.searchOrderId(secondOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderValidation.updatePclMapsForMultiOrder(ExcelUtils.PCL_OVERSIZE_TC_12131_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12131_web_part3_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order in same store")
    public void validateE2eMultipleOrdersStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12131_web_part4_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in same store")
    public void validateE2eMultipleOrdersPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12131_1);
    }
}