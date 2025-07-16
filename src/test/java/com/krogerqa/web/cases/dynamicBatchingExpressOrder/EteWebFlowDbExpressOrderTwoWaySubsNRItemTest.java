package com.krogerqa.web.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14050 Web Flows -
 * Submit  Pickup express order in Kroger.com for DyB store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After staging and order adjustment, validate the Staging status in Cue
 * After Staging remaining container, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */
public class EteWebFlowDbExpressOrderTwoWaySubsNRItemTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify order placed for db non-EBT pcl express order")
    public void validateE2eTwoWaysSubsNRItemExpressOrderPlaced() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyExpressOrderInitialCount();
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
        baseCommands.webpageRefresh();
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        testOutputData.put(ExcelUtils.ENHANCED_BATCHING_MAP, ExcelUtils.COLLAPSE_ALL);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for db non-EBT pcl express order")
    public void validateE2eTwoWaysSubsNRItemExpressOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staging status in Cue")
    public void validateE2eTwoWaysSubsNRItemExpressOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagingStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_web_part4_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for various fulfillment types")
    public void validateE2eTwoWaysSubsNRItemExpressOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, testOutputData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_expressOrder_singleThread_14050_web_part5_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT db pcl express order")
    public void validateE2eTwoWaysSubsNRItemExpressOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14050, ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DB_EXPRESS_ORDER_TC_14050);
    }
}