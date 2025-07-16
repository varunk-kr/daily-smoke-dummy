package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario EB_PCL_TC_10942 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded DB store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * While selecting, move item to existing container, After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowEnhancedBatchingPclExistingItemMovementTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();

    @Test(groups = {"ete_db_enhancedBatchPcl_cancel_Order_ItemMovement_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for db non-EBT pcl order")
    public void validateE2eDbPclItemMovementOrderPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10942, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10942_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10942);
        baseCommands.webpageRefresh();
        secondOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_NONE);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.EB_PCL_TC_10942_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        baseCommands.webpageRefresh();
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        cueOrderDetailsPage.verifyTrolleysNotCreated();
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_TC_10942_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatchPcl_cancel_Order_ItemMovement_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for db non-EBT pcl order")
    public void validateE2eDbPclItemMovementOrderPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10942_1, ExcelUtils.EB_PCL_TC_10942_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.EB_PCL_TC_10942_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatchPcl_cancel_Order_ItemMovement_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl order")
    public void validateE2eDbPclItemMovementOrderStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10942_1, ExcelUtils.EB_PCL_TC_10942_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatchPcl_cancel_Order_ItemMovement_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl order")
    public void validateE2eDbPclItemMovementOrderPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10942_1, ExcelUtils.EB_PCL_TC_10942_1);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_TC_10942_1);
    }
}
