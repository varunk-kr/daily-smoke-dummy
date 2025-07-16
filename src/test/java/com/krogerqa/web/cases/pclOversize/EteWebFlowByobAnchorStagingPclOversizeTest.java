package com.krogerqa.web.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12137 Web Flows -
 * Submit Non-EBT Pcl Pickup order with OS, Non Os, Nr items in Kroger.com for Multi-threaded store and perform Batching >
 * Do not accept bag fee in byob preference in KSP >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Fulfill Nr item in order adjustment screen >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * Change bag preference to accept bag fee after picking in KSP >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowByobAnchorStagingPclOversizeTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pclOversize_tc_12137_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for byob anchor granular pcl oversize order")
    public void validateE2eNewNonEbtByobAnchorStagingPclOversizeNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12137);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12137, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12137_web_part1_1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify pcl order placed for byob anchor granular pcl oversize order")
    public void validateE2eNewNonEbtByobAnchorStagingPclOversizeChangeBagPrefOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12137, ExcelUtils.PCL_OVERSIZE_TC_12137);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        testOutputData.put(ExcelUtils.CHANGE_BAG_PREF, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData,testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_pclOversize_tc_12137_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for byob anchor granular pcl oversize order")
    public void validateE2eNonEbtByobAnchorStagingPclOversizeOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12137, ExcelUtils.PCL_OVERSIZE_TC_12137);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12137, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12137_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for byob anchor granular pcl oversize order")
    public void validateE2eNonEbtByobAnchorStagingPclOversizeOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12137, ExcelUtils.PCL_OVERSIZE_TC_12137);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12137_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for byob anchor granular pcl oversize order")
    public void validateE2eNonEbtByobAnchorStagingPclOversizeOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12137, ExcelUtils.PCL_OVERSIZE_TC_12137);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12137);
    }
}
