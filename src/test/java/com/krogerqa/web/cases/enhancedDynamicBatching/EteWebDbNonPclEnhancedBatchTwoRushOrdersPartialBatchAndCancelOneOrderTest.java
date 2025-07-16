package com.krogerqa.web.cases.enhancedDynamicBatching;

import com.krogerqa.api.PickingServicesHelper;
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
 * Scenario FFILLSVCS-TC-10878 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com and modify for Multi-threaded store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and partially batch the order and cancel one of the order>
 * Assign pcl labels, after selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */

public class EteWebDbNonPclEnhancedBatchTwoRushOrdersPartialBatchAndCancelOneOrderTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_web_part1_multipleRushOrders", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT orders placed in dynamic batching store and cancel the order after partial batching")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.OSFR);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878, firstOrderTestData);
        baseCommands.webpageRefresh();
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        pickingServicesHelper.partialBatching(firstOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        baseCommands.webpageRefresh();
        cueOrderValidation.verifyCancelOrderFromCue(firstOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(firstOrderTestData);
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData.put(ExcelUtils.COMMON_TROLLEY_CANCEL,String.valueOf(true));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, secondOrderTestData);
        secondOrderTestData=cueOrderValidation.getContainersForCollapseTrolleys(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1,secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_web_part2_multipleRushOrders", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders picked for non-EBT order in same store")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderPicked() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_web_part3_multipleRushOrders", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order in same store")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderStaged() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_web_part4_multipleRushOrders", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in same store")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderPaid() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
    }
}
