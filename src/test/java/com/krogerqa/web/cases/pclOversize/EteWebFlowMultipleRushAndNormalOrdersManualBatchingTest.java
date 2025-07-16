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
 * Scenario FFILLSVCS-TC-12132 Web Flows -
 * Submit 2 normal and 2 rush orders in Kroger.com for Multi-threaded store and perform manual Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowMultipleRushAndNormalOrdersManualBatchingTest extends BaseTest {
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    LoginWeb loginWeb = LoginWeb.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pclOversize_tc_12132_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeNewOrder() {
        getTestDataSheet(ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        thirdOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        fourthOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_ROMB, String.valueOf(true));
        fourthOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_STAGING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ROMB, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12132_1, secondOrderTestData);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
        fourthOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12132_3, fourthOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueHomePage.multipleRushAndNormalOrderBatching(KrogerSeamLessPortalOrderCreation.rombOrders, KrogerSeamLessPortalOrderCreation.normalOrders);
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        browserRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_1, secondOrderTestData);
        browserRefresh();
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
        browserRefresh();
        fourthOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_3, fourthOrderTestData);
        browserRefresh();
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData, secondOrderTestData);
        thirdOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData, fourthOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12132_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderPicked() {
        getTestDataSheet(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        fourthOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12132_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12132_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderStaged() {
        getTestDataSheet(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.PCL_OVERSIZE_TC_12132, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.PCL_OVERSIZE_TC_12132_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.PCL_OVERSIZE_TC_12132_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(fourthOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        fourthOrderTestData = cueOrderValidation.updatePclTemperatureMap(ExcelUtils.PCL_OVERSIZE_TC_12132_3, fourthOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12132_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for 2 normal and 2 romb pcl oversize orders")
    public void validateE2eMultipleROMBOrdersPclOversizeOrderPaid() {
        getTestDataSheet(ExcelUtils.PCL_OVERSIZE_TC_12132, ExcelUtils.PCL_OVERSIZE_TC_12132_1, ExcelUtils.PCL_OVERSIZE_TC_12132_2, ExcelUtils.PCL_OVERSIZE_TC_12132_3);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(fourthOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12132);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12132_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12132_2);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12132_3);
    }

    public void getTestDataSheet(String sheetName1, String sheetName2, String sheetName3, String sheetName4) {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132, sheetName1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_1, sheetName2);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_2, sheetName3);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12132_3, sheetName4);
    }

    public void browserRefresh() {
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
    }
}
