package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Scenario BYOB_PCL_TC_9083 Web Flows -
 * Submit Non-EBT Pcl Pickup express and normal order in Kroger.com for DyB store and perform Batching using request trolleys >
 * Accept bag fee for normal order >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */

public class EteWebFlowByobExpressAndNormalDifferentSlotOrdersTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    CueOrderDetailsPage cueOrderDetailsPage = CueOrderDetailsPage.getInstance();

    @Test(groups = {"ete_byob_express_and_normal_order_9083_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for db non-EBT pcl express order")
    public void validateE2eDbPclByobExpressOrderPlaced() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        //Verify Express Order Initial count
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyExpressOrderInitialCount();
        //Create Orders
        firstOrderTestData.put(ExcelUtils.IS_BYOB_FLOW,String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.ACCEPT_BAG_FEES,String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKingSoopersSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(firstOrderTestData.get(ExcelUtils.USER_EMAIL), firstOrderTestData.get(ExcelUtils.PASSWORD));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.BYOB_PCL_TC_9083,firstOrderTestData);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BYOB_PCL_TC_9083_1,secondOrderTestData);
        baseCommands.webpageRefresh();
        //Batch Orders
        cueOrderValidation.batchOrders(firstOrderTestData,secondOrderTestData);
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_BATCH,String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_BATCH,String.valueOf(true));
        //Validate orders in Cue
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_9083_1, secondOrderTestData);
        //Update bag count map
        List<HashMap<String, String>> list = new ArrayList<>();
        list.add(firstOrderTestData);
        list.add(secondOrderTestData);
        firstOrderTestData = cueOrderValidation.getMultipleOrderBagCount(ExcelUtils.BYOB_PCL_TC_9083, list, firstOrderTestData);
        firstOrderTestData.put(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG,String.valueOf(true));
        //Generate maps for common trolleys
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_byob_express_and_normal_order_9083_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for db non-EBT pcl express order")
    public void validateE2eDbPclByobExpressOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.BYOB_PCL_TC_9083_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        //update the maps for first order
        cueHomePage.searchOrderId(firstOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
        baseCommands.browserBack();
        cueHomePage.searchOrderId(secondOrderTestData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.BYOB_PCL_TC_9083_1, secondOrderTestData);
        //Validate picked status for both orders
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_9083, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_9083_1, secondOrderTestData);
    }

    @Test(groups = {"ete_byob_express_and_normal_order_9083_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for db non-EBT pcl express order")
    public void validateE2eDbPclByobExpressOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.BYOB_PCL_TC_9083_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_byob_express_and_normal_order_9083_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl express order")
    public void validateE2eDbPclByobExpressOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083, ExcelUtils.BYOB_PCL_TC_9083);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_9083_1, ExcelUtils.BYOB_PCL_TC_9083_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_9083);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_9083_1);
    }
}
