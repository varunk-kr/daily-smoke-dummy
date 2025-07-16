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
 * Scenario FFILLSVCS-TC-13139 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowMultipleOrdersWeightedTrolleyPclTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_13139_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtPclOrder() {
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_WEIGHTED_TC_13139);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DB_WEIGHTED_TC_13139_1);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_WEIGHTED_TC_13139, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_WEIGHTED_TC_13139_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_13139_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT pcl order")
    public void validateE2eNonEbtPclOrderPicked() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_WEIGHTED_TC_13139, firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_13139_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT pcl order")
    public void validateE2eNonEbtPclOrderStaged() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_13139_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT pcl order")
    public void validateE2eNonEbtPclOrderPaid() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_WEIGHTED_TC_13139);
    }
}
