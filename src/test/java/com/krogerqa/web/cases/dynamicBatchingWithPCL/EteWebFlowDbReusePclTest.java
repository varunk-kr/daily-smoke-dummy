package com.krogerqa.web.cases.dynamicBatchingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-9503 Web Flows -
 * Place EBT order from API ,perform dynamic batching ,complete picking and staging from API and complete End to End.
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign used pcl labels from first scenario>
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowDbReusePclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> baseOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();


    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify non ebt order placed ,batched using dynamic batching and completed till staging from API")
    public void validateE2eDbPclApiNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, testOutputData);
        testOutputData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.eteOrderApi(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify non ebt dynamic batching pcl base order staged")
    public void validateE2eNonEbtDbReusePclBaseOrderStagedValidation() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify non ebt dynamic batching pcl base order picked up and paid")
    public void validateE2eNonEbtDbReusePclBaseOrderPickedStatusValidation() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1);
        baseOrderData = testOutputData;
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify dynamic batching pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtDbReusePclOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        testOutputData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCueReusePcl(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, testOutputData);
        testOutputData = cueOrderValidation.updateReusePclTemperatureMaps(baseOrderData, testOutputData, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part5_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify non-EBT dynamic batching pcl order picked, by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtDbReusePclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part6_nonEbt", "ete_web_part6_nonEbt", "ete_web_part6_multiThreaded"}, description = "Verify non-EBT dynamic batching pcl order staged and checked-in, which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtDbReusePclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_web_part7_nonEbt", "ete_web_part7_nonEbt", "ete_web_part7_multiThreaded"}, description = "Verify non-EBT dynamic batching order picked up and paid by reusing the pcl labels from pickedUp order for non-EBT pcl order")
    public void validateE2eNonEbtDbReusePclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
    }
}
