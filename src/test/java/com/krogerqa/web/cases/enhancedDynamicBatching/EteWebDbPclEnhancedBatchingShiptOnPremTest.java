package com.krogerqa.web.cases.enhancedDynamicBatching;

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
 * Scenario FFILLSVCS-TC-10937 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in using shipt pif API >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebDbPclEnhancedBatchingShiptOnPremTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    BaseCommands baseCommands = new BaseCommands();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_10937_delivery_OnPrem_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order placed for non-EBT order")
    public void validateE2eNewNonEbtPclEbDeliveryOnPremOrder() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginKrogerSeamlessPortal(firstOrderTestData.get(ExcelUtils.USER_EMAIL), firstOrderTestData.get(ExcelUtils.PASSWORD));
        firstOrderTestData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DELIVERY_SHIPT_PIF_ORDER, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getKrogerSeamlessUrlEbt());
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyOrderInKSP(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10937_delivery_OnPrem_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT pcl order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10937_delivery_OnPrem_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT pcl order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10937_delivery_OnPrem_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT pcl order")
    public void validateE2eNonEbtPclEbDeliveryOnPremOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2, ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_1);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.EB_PCL_SHIPT_ON_PREM_TC_10937_2);
    }
}

