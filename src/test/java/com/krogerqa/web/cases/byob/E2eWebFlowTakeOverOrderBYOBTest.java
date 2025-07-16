package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.seamlessportal.SeamlessStoreChangePage;
import org.testng.annotations.Test;

import java.util.HashMap;


/**
 * Scenario BYOB-TC-8528 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for multi Threaded store>
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Select bag preference as kroger bag during picking
 * Complete selecting for the order.
 * Stage the order.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class E2eWebFlowTakeOverOrderBYOBTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    SeamlessStoreChangePage seamlessStoreChangePage = SeamlessStoreChangePage.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for non-EBT BYOB order")
    public void validateE2eByobOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_8528);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);

    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify bag preference for non-EBT BYOB order")
    public void validateE2eByobOrderBagPreference() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        seamlessStoreChangePage.verifyStore(testOutputData, testOutputData.get(ExcelUtils.STORE_NAME), testOutputData.get(ExcelUtils.POSTAL_CODE), testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData, testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_web_part3", "ete_web_part3_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT BYOB order")
    public void validateE2eByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_web_part4", "ete_web_part4_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and for non-EBT BYOB order")
    public void validateE2eByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_web_part5", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify picked up and paid status for non-EBT BYOB order")
    public void validateE2eByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_8528);
    }
}
