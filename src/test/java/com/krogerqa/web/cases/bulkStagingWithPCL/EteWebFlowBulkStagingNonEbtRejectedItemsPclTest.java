package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Scenario BULK_STAGING_TC_11754 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowBulkStagingNonEbtRejectedItemsPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_pcl_tc_11754_bulkStaging_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11754);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER,String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11754, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11754_bulkStaging_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11754,ExcelUtils.BULK_STAGING_TC_11754);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11754, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11754_bulkStaging_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11754,ExcelUtils.BULK_STAGING_TC_11754);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.BULK_STAGING_TC_11754);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.BULK_STAGING_TC_11754, testOutputData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11754_bulkStaging_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for bulk staging order")
    public void validateE2eNonEbtPclBulkStagingOrderPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11754,ExcelUtils.BULK_STAGING_TC_11754);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11754);
    }
}
