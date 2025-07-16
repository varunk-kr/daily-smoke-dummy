package com.krogerqa.web.cases.dynamicBatchingWithPCL;

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
import java.util.List;

/**
 * Scenario DB_PCL_TC-9499 Web Flows -
 * Submit 2 Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching using request trolleys with all fulfilment types>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowDBPclAllFulflimentTypeTest extends BaseTest {
    static HashMap<String, String> firstOrderData = new HashMap<>();
    static HashMap<String, String> secondOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9499_smokeFlow_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for db non-EBT pcl order")
    public void validateE2eWebDbAllfulfilmentTypesOrderPlaced() {
        firstOrderData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        secondOrderData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        firstOrderData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(firstOrderData.get(ExcelUtils.STORE_ID), firstOrderData.get(ExcelUtils.STORE_BANNER));
        firstOrderData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderData.get(ExcelUtils.STORE_ID)));
        secondOrderData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, secondOrderData);
        firstOrderData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData, secondOrderData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_smokeFlow_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for db non-EBT pcl order")
    public void validateE2eWebDbAllfulfilmentTypesOrderPicked() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        loginWeb.loginCue(firstOrderData.get(ExcelUtils.STORE_ID), firstOrderData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, secondOrderData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, secondOrderData);
    }

    @Test(groups = {"ete_db_pcl_tc_9499_smokeFlow_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl order")
    public void validateE2eWebDbAllfulfilmentTypesOrderStaged() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        loginWeb.loginCue(firstOrderData.get(ExcelUtils.STORE_ID), firstOrderData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(firstOrderData.get(ExcelUtils.ORDER_NUMBER), itemData, firstOrderData);
        firstOrderData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, firstOrderData);
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderData);

    }

    @Test(groups = {"ete_db_pcl_tc_9499_smokeFlow_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl order")
    public void validateE2eWebDbAllfulfilmentTypesOrderPaid() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1, ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderData);
        loginWeb.loginCue(firstOrderData.get(ExcelUtils.STORE_ID), firstOrderData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_PCL_TC_9499_1);
    }
}