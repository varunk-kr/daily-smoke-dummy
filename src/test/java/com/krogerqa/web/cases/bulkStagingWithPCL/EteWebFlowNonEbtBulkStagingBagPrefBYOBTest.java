package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11747 Web Flows -
 * Submit Non-EBT Pcl  Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * 1 rush order with accept bag fee and after picking change bag preference
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Picking change the bag preference and accept bag fee
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtBulkStagingBagPrefBYOBTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_bulkStaging_pcl_tc_11747_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify change preference BYOB SingleThread pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtSingleThreadPclChangeBagPrefOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }

        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11747);
        testOutputData.put(ExcelUtils.BYOB_ALL_COLLAPSE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_RE_STAGE, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(true));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData,testOutputData.get(ExcelUtils.ORDER_NUMBER));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11747, testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11747_web_part2_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify change preference BYOB SingleThread pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtSingleThreadPclChangeBagPrefBYOBfOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11747, ExcelUtils.BULK_STAGING_TC_11747);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        testOutputData.put(ExcelUtils.CHANGE_BAG_PREF, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData,testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11747_web_part3_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for change preference BYOB non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderChangeBagPrefPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11747, ExcelUtils.BULK_STAGING_TC_11747);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11747, testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11747_web_part4_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for change preference BYOB non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderChangeBagPrefStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11747, ExcelUtils.BULK_STAGING_TC_11747);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11747_web_part5_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclChangeBagPrefBYOBOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11747, ExcelUtils.BULK_STAGING_TC_11747);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11747);
    }
}
