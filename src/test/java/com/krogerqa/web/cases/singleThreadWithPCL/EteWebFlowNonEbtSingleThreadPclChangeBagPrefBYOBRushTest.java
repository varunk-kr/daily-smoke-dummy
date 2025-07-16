package com.krogerqa.web.cases.singleThreadWithPCL;

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
 * Scenario SINGLE THREAD PCL FFILLSVCS-TC-9717 Web Flows -
 * Submit Non-EBT SingleThread Pcl Rush Pickup order in Kroger.com for Single-threaded store>
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * 1 rush order with accept bag fee and after picking change bag preference
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Picking change the bag preference and accept bag fee
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadPclChangeBagPrefBYOBRushTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9717_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify change preference BYOB SingleThread pcl rush order placed for non-EBT order")
    public void validateE2eNewNonEbtSingleThreadPclChangeBagPrefOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
        testOutputData.put(ExcelUtils.BYOB_ALL_COLLAPSE, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_OOS_SHORT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9717_web_part1_1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify change preference BYOB SingleThread pcl rush order placed for non-EBT order")
    public void validateE2eNewNonEbtSingleThreadPclChangeBagPrefBYOBfOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        testOutputData.put(ExcelUtils.CHANGE_BAG_PREF, String.valueOf(true));
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData,testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9717_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify rush order picked for change preference BYOB non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderChangeBagPrefPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9717_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify rush order staged and checked-in for change preference BYOB non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclOrderChangeBagPrefStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9717_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify rush order picked up and paid for non-EBT SingleThread pcl order")
    public void validateE2eNonEbtSingleThreadPclChangeBagPrefBYOBOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9717, ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9717);
    }
}
