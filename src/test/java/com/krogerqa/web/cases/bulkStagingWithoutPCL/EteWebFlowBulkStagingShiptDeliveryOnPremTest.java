package com.krogerqa.web.cases.bulkStagingWithoutPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11776 Web Flows -
 * Submit Non-EBT shipt delivery order in Kroger.com for Single-threaded store on prem store>
 * Verify New Non-EBT Order Details in Cue >
 * After selecting, verify Picked order and container status in Cue  >
 * After Staging, validate COR event, and Staged Order Status and perform run the shipt APIs for driver arrived in the store >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Delivered in Cue
 */
public class EteWebFlowBulkStagingShiptDeliveryOnPremTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify shipt delivery on prem order placed for bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(true));
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify shipt delivery  order picked for bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11776, testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify shipt delivery  order staged and checked-in for bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_non_pcl_tc_11776_bulkStaging_shipt_delivery_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify shipt delivery  order picked up and delivered for bulk staging order")
    public void validateE2eNonEbtBulkStagingShiptDeliveryOrderPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11776,ExcelUtils.BULK_STAGING_TC_11776);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11776);
    }
}
