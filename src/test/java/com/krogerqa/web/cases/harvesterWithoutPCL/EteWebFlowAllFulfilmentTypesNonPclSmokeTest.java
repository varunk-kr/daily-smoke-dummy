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

public class EteWebFlowAllFulfilmentTypesNonPclSmokeTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_web_part1_nonEbt", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for all fulfillment type smoke test")
    public void validateE2eNonEbtNonPclSmokeTest() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_web_part2_nonEbt", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order picked for all fulfillment type smoke test")
    public void validateE2eNonEbtNonPclSmokeTestPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_web_part3_nonEbt", "ete_web_part1_ebt", "ete_web_part1_multiThreaded"}, description = "Verify order staged and checked-in for for all fulfillment type smoke test")
    public void validateE2eNonEbtNonPclSmokeTestStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_web_part4_nonEbt"}, description = "Fulfillment Non Pcl Smoke-(1. Substitute Accept and Reject, 2. Full OOS, 3. Not Ready Service Counter item, 4. Shorting, 5. Non-EBT order fulfillment)")
    public void validateE2eNonEbtNonPclSmokeTestPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
    }
}
