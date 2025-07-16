package com.krogerqa.web.cases.pclOversize;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_OS_DEFECT_DIFFERENT_ITEMS Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowPclOSProdDefectDifferentItemsTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for pcl oversize order")
    public void validateE2eNewNonEbtPickedOsToNewPclOversizeOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        testOutputData.put(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS,String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_PCL_OS_DIFFERENT_UPC, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
        testOutputData = cueOrderValidation.updatePclTrolleyMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizePicking() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickingStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_web_part3_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagingStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_web_part4_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_web_part5_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizePaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
    }
}
