package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11728 Web Flows -
 * Place EBT order from API and completed End to End from API.
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign used pcl labels from first scenario>
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowBulkStagingReusePclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> baseOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();


    @Test(groups = {"ete_pcl_tc_11728_web_part1_reusePcl_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify EBT order placed and completed End to End from API")
    public void validateE2ePclApiNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728_1, ExcelUtils.SHEET_NAME_TEST_DATA);

        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.BULK_STAGING_TC_11728_1, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.eteOrderApi(ExcelUtils.BULK_STAGING_TC_11728_1, ExcelUtils.BULK_STAGING_TC_11728, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part2_reusePcl_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify bulk staging pcl order placed for non-EBT order")
    public void validateE2eNonEbtReusePclBaseOrderStagedValidation() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728_1, ExcelUtils.BULK_STAGING_TC_11728_1);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part3_reusePcl_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify bulk staging pcl order picked and paid for first order")
    public void validateE2eNonEbtReusePclBaseOrderPickedStatusValidation() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728_1, ExcelUtils.BULK_STAGING_TC_11728_1);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11728_1);
        baseOrderData = testOutputData;
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part4_reusePcl_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify bulk staging pcl order placed for Reuse order")
    public void validateE2eNewNonEbtReusePclOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11728);
        testOutputData.put(ExcelUtils.IS_BULK_STAGING,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCueReusePcl(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728_1, testOutputData);
        testOutputData = cueOrderValidation.updateReusePclTemperatureMaps(baseOrderData, testOutputData, ExcelUtils.BULK_STAGING_TC_11728);
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part5_reusePcl_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_singleThreaded"}, description = "Verify non-EBT bulk staging pcl order picked, by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11728, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part6_reusePcl_nonEbt", "ete_web_part6_nonEbt", "ete_web_part6_singleThreaded"}, description = "Verify non-EBT bulk staging pcl order staged and checked-in, which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_web_part7_reusePcl_nonEbt", "ete_web_part7_nonEbt", "ete_web_part7_singleThreaded"}, description = "Validate E2E fulfillment for Re-use PCL assigning PCL (Picking and Staging Using Harvester Native app)")
    public void validateE2eNonEbtReusePclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11728);
    }
}
