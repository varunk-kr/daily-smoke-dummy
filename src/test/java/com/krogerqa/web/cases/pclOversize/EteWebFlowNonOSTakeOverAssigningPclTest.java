package com.krogerqa.web.cases.pclOversize;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12148 Web Flows -
 * Submit Non-EBT Multiple Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowNonOSTakeOverAssigningPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    static HashMap<String, String> secondOrderData = new HashMap<>();
    static HashMap<String, String> firstOrderData = new HashMap<>();

    @Test(groups = {"ete_pclOversize_tc_12148_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT pcl oversize order")
    public void validateE2eNewNonEbtTakeOverAssigningPclOversizeFirstOrder() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        firstOrderData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        firstOrderData.put(ExcelUtils.IS_REUSE_PCL_SCENARIO, String.valueOf(true));
        firstOrderData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        firstOrderData.put(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING, String.valueOf(true));
        firstOrderData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OVERSIZE_TC_12148_1, firstOrderData);
        loginWeb.loginCue(firstOrderData.get(ExcelUtils.STORE_ID), firstOrderData.get(ExcelUtils.STORE_BANNER));
        firstOrderData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12148_1, firstOrderData);
    }

    @Test(groups = {"ete_pclOversize_tc_12148_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify the second order placed for non-EBT pcl oversize order")
    public void validateE2eNewNonEbtTakeOverAssigningPclOversizeSecondOrder() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148_1, ExcelUtils.PCL_OVERSIZE_TC_12148_1);
        secondOrderData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OVERSIZE_TC_12148);
        secondOrderData.put(ExcelUtils.IS_PCL_OVERSIZE, String.valueOf(true));
        secondOrderData.put(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING, String.valueOf(true));
        loginWeb.loginCue(secondOrderData.get(ExcelUtils.STORE_ID), secondOrderData.get(ExcelUtils.STORE_BANNER));
        secondOrderData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12148, secondOrderData);
        secondOrderData = cueOrderValidation.updateReusePclTemperatureMaps(firstOrderData, secondOrderData, ExcelUtils.PCL_OVERSIZE_TC_12148);
    }
    @Test(groups = {"ete_pclOversize_tc_12148_web_part3_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify the second order placed for non-EBT pcl oversize order")
    public void validateE2eNewNonEbtTakeOverAssigningPclOversizeNewSecondOrder() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148, ExcelUtils.PCL_OVERSIZE_TC_12148);
        loginWeb.loginCue(secondOrderData.get(ExcelUtils.STORE_ID), secondOrderData.get(ExcelUtils.STORE_BANNER));
        secondOrderData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OVERSIZE_TC_12148, secondOrderData);
    }

    @Test(groups = {"ete_pclOversize_tc_12148_web_part4_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148, ExcelUtils.PCL_OVERSIZE_TC_12148);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_OVERSIZE_TC_12148, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12148_web_part5_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148, ExcelUtils.PCL_OVERSIZE_TC_12148);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12148_web_part6_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12148, ExcelUtils.PCL_OVERSIZE_TC_12148);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OVERSIZE_TC_12148);
    }
}
