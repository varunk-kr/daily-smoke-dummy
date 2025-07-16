package com.krogerqa.web.cases.tc52NotificationsWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario RUSH_ORDER_TC52_TC_7114 Web Flows -
 * Submit Non-EBT Pcl rush Pickup order in Kroger.com for Multi-threaded store and perform Batching Order level batching>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class SmokeEteWebFlowNonEBTPclOrderROMBTc52Test extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify Non EBT rush order creation for TC 52 Notification with rush order manual batching")
    public void validateE2ETC52NonEBTOrderCreation() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyExpressOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_EXPRESS_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
        baseCommands.browserBack();
        testOutputData = cueOrderValidation.getItemCount(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify Non EBT rush order picked for TC 52 Notification with rush order manual batching")
    public void validateE2eTC52NonEbtPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.RUSH_ORDER_TC52_TC_7114, testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify Non EBT Rush order staged for TC52 Notification with rush order manual batching")
    public void validateE2eTC52NonEbtStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_tc52_rush_notification_tc_7114_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify Non EBT rush order picked up and paid for TC52 Notification with rush order manual batching (Picking and Staging using Harvester Native app)")
    public void validateE2eTC52NonEbtPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.RUSH_ORDER_TC52_TC_7114, ExcelUtils.RUSH_ORDER_TC52_TC_7114);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.RUSH_ORDER_TC52_TC_7114);
    }
}
