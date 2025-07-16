package com.krogerqa.web.cases.singleThreadWithPCL;

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
 * Scenario TC_52_NOTIFICATION_TC_9872 Web Flows -
 * Submit Non-EBT Pcl rush Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEBTSingleThreadPclOrderTc52Test extends BaseTest{

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_tc52_notification_tc_9872_web_part1","ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify Non EBT rush order creation for TC 52 Notification")
    public void validateE2ETC52NonEBTOrderCreated() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
        baseCommands.browserBack();
        testOutputData = cueOrderValidation.getItemCount(ExcelUtils.TC_52_NOTIFICATION_TC_9872,testOutputData);
        }
    @Test(groups = {"ete_tc52_notification_tc_9872_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify Non EBT rush order picked for TC 52 Notification")
    public void validateE2eTC52NonEbtPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.TC_52_NOTIFICATION_TC_9872, testOutputData);
    }

    @Test(groups = {"ete_tc52_notification_tc_9872_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify Non EBT Rush order staged for TC52 Notification")
    public void validateE2eTC52NonEbtStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_tc52_notification_tc_9872_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify Non EBT rush order picked up and paid for TC52 Notification")
    public void validateE2eTC52NonEbtPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_9872, ExcelUtils.TC_52_NOTIFICATION_TC_9872);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.TC_52_NOTIFICATION_TC_9872);
    }
}
