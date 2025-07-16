package com.krogerqa.web.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10459
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Single-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue >
 * verify Picked order status and respective item and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After deStaging, verify order is dropped from Dash >
 * Verify Non-EBT order is Picked Up and Paid in Cue
 */

public class EteWebFlowWithMultipleWeightItemsNonEbtOrderTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_10459_web_part1_mafp_ciao", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "description = Verify order placed for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsNewOrder() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        firstOrderTestData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.PICK_MULTIPLE_QTY_SERVICE_COUNTER_ITEM, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.PICK_SERVICE_COUNTER_ITEM, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        secondOrderTestData.put(ExcelUtils.IS_MAFP, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.PICK_SERVICE_COUNTER_ITEM, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.PICK_MULTIPLE_QTY_SERVICE_COUNTER_ITEM, String.valueOf(true));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10459_web_part2_mafp_ciao", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10459_web_part3_mafp_ciao", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10459_web_part4_mafp_ciao", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
    }
}
