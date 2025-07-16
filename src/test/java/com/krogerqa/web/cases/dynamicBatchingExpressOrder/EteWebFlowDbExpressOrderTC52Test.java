package com.krogerqa.web.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14051 Web Flows -
 * Submit 1 express order on DyB single thread store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */

public class EteWebFlowDbExpressOrderTC52Test  extends BaseTest{
        static HashMap<String, String> testOutputData= new HashMap<>();
        KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
        CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
        LoginWeb loginWeb = LoginWeb.getInstance();
        BaseCommands baseCommands = new BaseCommands();
        CueHomePage cueHomePage = CueHomePage.getInstance();

        @Test(groups = {"ete_db_expressOrder_singleThread_web_part1_nonEbt_14051", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify express order and normal order placed")
        public void validateE2eDbPclExpressOrderTC52Placed() {
            if (!(testOutputData == null)) {
                testOutputData.clear();
            }
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.SHEET_NAME_TEST_DATA);
            loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
            cueHomePage.verifyExpressOrderInitialCount();
            testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
            baseCommands.webpageRefresh();
            testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
            testOutputData.put(ExcelUtils.HEADS_UP_SCREEN, String.valueOf(true));
            testOutputData.put(ExcelUtils.ITEM_COUNT,"4");
            testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
            testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
        }

    @Test(groups = {"ete_db_expressOrder_singleThread_web_part2_nonEbt_14051", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for various fulfillment types for snap-EBT pcl order")
    public void validateE2EDBPclExpressOrderTC52Picked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, testOutputData);
    }
    @Test(groups = {"ete_db_expressOrder_singleThread_web_part3_nonEbt_14051", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for various fulfillment types for snap-EBT pcl order")
    public void validateE2EDBPclExpressOrderTC52Staged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }
    @Test(groups = {"ete_db_expressOrder_singleThread_web_part4_nonEbt_14051", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Fulfillment Smoke-(1. Substitute Accept and Reject, 2. Full OOS, 3. Not Ready Service Counter item, 4. Shorting, 5. Non-EBT order fulfillment)")
    public void validateE2EDBPclExpressOrderTC52Paid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14051, ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_EXPRESS_ORDER_TC_14051);
    }
}