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
 * Scenario TC_52_NOTIFICATION_TC_7038 Web Flows -
 * Submit multiple Non-EBT Pcl rush Pickup orders in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowTC52MultipleOrderTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_tc52_notification_tc_7038_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify Non EBT rush order creation for TC 52 Notification")
    public void validateE2ETC52NonEBTOrderCreated() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_7038, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.TC_52_NOTIFICATION_TC_7038, firstOrderTestData);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1, secondOrderTestData);
        baseCommands.webpageRefresh();
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.TC_52_NOTIFICATION_TC_7038, firstOrderTestData);
        baseCommands.browserBack();
        firstOrderTestData = cueOrderValidation.getItemCount(ExcelUtils.TC_52_NOTIFICATION_TC_7038, firstOrderTestData);
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1, secondOrderTestData);
        baseCommands.browserBack();
        secondOrderTestData = cueOrderValidation.getItemCount(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1, secondOrderTestData);
    }

    @Test(groups = {"ete_tc52_notification_tc_7038_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify Non EBT rush order picked for TC 52 Notification")
    public void validateE2eTC52NonEbtPicked() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.TC_52_NOTIFICATION_TC_7038, firstOrderTestData);
        baseCommands.browserBack();
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1, secondOrderTestData);
    }

    @Test(groups = {"ete_tc52_notification_tc_7038_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify Non EBT Rush order staged for TC52 Notification")
    public void validateE2eTC52NonEbtStaged() {
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_tc52_notification_tc_7038_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify Non EBT rush order picked up and paid for TC52 Notification")
    public void validateE2eTC52NonEbtPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.TC_52_NOTIFICATION_TC_7038);
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.TC_52_NOTIFICATION_TC_7038_1);
    }
}