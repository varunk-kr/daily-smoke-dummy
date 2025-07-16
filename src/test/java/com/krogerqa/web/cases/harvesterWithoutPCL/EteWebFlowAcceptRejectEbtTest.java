package com.krogerqa.web.cases.harvesterWithoutPCL;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Scenario 5 Web Flows -
 * Submit EBT Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * Accept and Reject items
 * After selecting, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */
public class EteWebFlowAcceptRejectEbtTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete5_web_part1_EbtAcceptReject", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for Ebt AcceptReject")
    public void validateE2eEbtAcceptRejectOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.SCENARIO_5);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SCENARIO_5, testOutputData);
    }

    @Test(groups = {"ete5_web_part2_EbtAcceptReject", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order picked for Ebt AcceptReject")
    public void validateE2eEbtAcceptRejectOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.SCENARIO_5, testOutputData);
    }

    @Test(groups = {"ete5_web_part3_EbtAcceptReject", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order staged and checked-in for Ebt AcceptReject")
    public void validateE2eEbtAcceptRejectOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.SCENARIO_5);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData,testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.SCENARIO_5,testOutputData);
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete5_web_part4_EbtAcceptReject"}, description = "Verify order picked up and paid for Ebt AcceptReject")
    public void validateE2eEbtAcceptRejectOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SCENARIO_5);
    }
}
