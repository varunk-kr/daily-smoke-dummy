package com.krogerqa.web.cases.byob;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Scenario  FFILLSVCS-TC-7460 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * Customer accepts bag fee>
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Accept and reject subs using 2 ways subs communication >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue
 */
public class EteWebByobTwoWaysSubsTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_byob_pcl_tc_7460_byobAccept_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed")
    public void validateE2ePclAcceptByobOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7460);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7460, testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_tc_7460_byobAccept_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order is picked and accept bag fee")
    public void validateE2ePclAcceptByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7460, ExcelUtils.BYOB_PCL_TC_7460);
        testOutputData.put(ExcelUtils.REJECT_BAG_FEES, String.valueOf(false));
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7460, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrl());
        loginWeb.loginKrogerSeamlessPortal(testOutputData.get(ExcelUtils.USER_EMAIL), testOutputData.get(ExcelUtils.PASSWORD));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData, testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    @Test(groups = {"ete_byob_pcl_tc_7460_byobAccept_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged, 2 ways subs and checked-in for byob order")
    public void validateE2ePclAcceptByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7460, ExcelUtils.BYOB_PCL_TC_7460);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.BYOB_PCL_TC_7460);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.BYOB_PCL_TC_7460, testOutputData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_tc_7460_byobAccept_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for db non-EBT pcl byob order")
    public void validateE2ePclAcceptByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7460, ExcelUtils.BYOB_PCL_TC_7460);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7460);
    }
}
