package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SINGLE_THREAD_PCL_TC_8731 Web Flows -
 * Place nonEBT order from API and completed End to End from API.
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store >
 * Verify New Non-EBT Order Details in Cue and assign used pcl labels from first scenario>
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadPclReusePclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    static HashMap<String, String> baseOrderData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part1_reusePcl_nonEbt", "ete_web_part_nonEbt", "ete_web_part_singleThreaded"}, description = "Verify non EBT order placed and completed End to End from API")
    public void validateE2ePclSingleThreadApiNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.eteOrderApi(ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part2_reusePcl_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify pcl order placed for non-EBT order")
    public void validateE2eNonEbtReusePclBaseOrderStagedValidation() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part3_reusePcl_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify pcl order placed for non-EBT order")
    public void validateE2eNonEbtReusePclBaseOrderPickedStatusValidation() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        baseOrderData = testOutputData;
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part4_reusePcl_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtReusePclOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8730, ExcelUtils.SINGLE_THREAD_PCL_TC_8730);
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCueReusePcl(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, ExcelUtils.SINGLE_THREAD_PCL_TC_8731_1, testOutputData);
        testOutputData = cueOrderValidation.updateReusePclTemperatureMaps(baseOrderData, testOutputData, ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part5_reusePcl_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_singleThreaded"}, description = "Verify non-EBT pcl order picked, by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part6_reusePcl_nonEbt", "ete_web_part6_nonEbt", "ete_web_part6_singleThreaded"}, description = "Verify non-EBT pcl order staged and checked-in, which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_8731_web_part7_reusePcl_nonEbt", "ete_web_part7_nonEbt", "ete_web_part7_singleThreaded"}, description = "Verify order picked up and paid by reusing the pcl labels from pickedUp order for non-EBT pcl order")
    public void validateE2eNonEbtReusePclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_8731, ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_8731);
    }
}
