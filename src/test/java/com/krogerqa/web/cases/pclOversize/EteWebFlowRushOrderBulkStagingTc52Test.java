package com.krogerqa.web.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12139 Web Flows -
 * Submit Non-EBT Pcl Pickup rush order in Kroger.com for Multi-threaded bulk staging store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowRushOrderBulkStagingTc52Test extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12139_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT pcl oversize order")
    public void validateE2eNewNonEbtPclTc52Order() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
        baseCommands.browserBack();
        testOutputData = cueOrderValidation.getItemCount(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT pcl oversize order")
    public void validateE2eNonEbtPclTc52OrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12139, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT pcl oversize order")
    public void validateE2eNonEbtPclTc52OrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12139_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT pcl oversize order")
    public void validateE2eNonEbtPclTc52OrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12139, ExcelUtils.PCL_OVERSIZE_TC_12139);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12139);
    }
}
