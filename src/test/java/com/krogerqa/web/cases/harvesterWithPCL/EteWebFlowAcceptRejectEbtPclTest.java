package com.krogerqa.web.cases.harvesterWithPCL;

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
 * Scenario pcl_tc_4556 Web Flows
 * Submit EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New EBT Order Details in Cue >
 * Accept and Reject items
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 */

public class EteWebFlowAcceptRejectEbtPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pcl_tc_4556_web_part1_ebtAcceptReject", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "description = Verify pcl order placed for Ebt AcceptReject")
    public void validateE2ePclNewStatusAcceptReject() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_4556);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4556_web_part2_ebtAcceptReject", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify pcl order picked for Ebt AcceptReject")
    public void validateE2ePclAcceptRejectPickedOrderPicked() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4556_web_part3_ebtAcceptReject", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify pcl order staged and checked-in for Ebt AcceptReject")
    public void validateE2ePclEbtAcceptRejectOrderStaged() {
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.PCL_SCENARIO_TC_4556);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData).substring(6);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4556_web_part4_ebtAcceptReject"}, description = "Verify pcl order picked up and paid for Ebt AcceptReject")
    public void validateE2ePclEbtAcceptRejectOrderPaid() {
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_4556);
    }
}
