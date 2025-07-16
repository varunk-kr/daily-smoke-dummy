package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Scenario BYOB_TC_7461 Web Flows BYOB functionality-
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */
public class EteWebFlowSubstitutionAndNRItemBYOBTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    static List<HashMap<String, String>> list = new ArrayList<>();

    @Test(groups = {"ete_byob_tc_7461_web_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify BYOB order placed for non-EBT order with Substitution and NR Item")
    public void validateE2eNewSubstitutionAndNRItemByobOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7461);
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        testOutputData.put(ExcelUtils.CIAO_SUBSTITUTE_ITEM_ACCEPT, String.valueOf(true));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7461, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7461_web_part2", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for non-EBT order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7461, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7461_web_part3", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for non-EBT order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_byob_tc_7461_web_part4", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for non-EBT order with Substitution and NR Item")
    public void validateE2eSubstitutionAndNRItemByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7461, ExcelUtils.BYOB_PCL_TC_7461);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7461);
    }
}
