package com.krogerqa.web.cases.harrisTeeterDynamicBatching;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-14379 Web Flows -
 * Submit multiple Non-EBT Pcl Pickup orders in Kroger.com for Harris Teeter Multi-threaded store and perform Batching using request trolleys >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowDbPclMultiThreadCollapseHTTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple non-EBT Pcl orders with same time slots and Collapse temp ALL are placed")
    public void validateE2eHTDybMultiOrderPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        firstOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        firstOrderTestData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        secondOrderTestData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.HT_BANNER);
        firstOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_DYNAMIC_BATCHING, String.valueOf(true));
        firstOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        secondOrderTestData.put(ExcelUtils.BATCH_ORDER, String.valueOf(false));
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, secondOrderTestData);
        firstOrderTestData = cueOrderValidation.generateMapsForPclCommonTrolleys(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, firstOrderTestData, secondOrderTestData);
    }

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify multiple orders picked for non-EBT order in same store")
    public void validateE2eHTDybMultiOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        secondOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, secondOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, secondOrderTestData);
    }

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT pcl rush order in same store")
    public void validateE2eHTDybMultiOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in same store")
    public void validateE2eHTDybMultiOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(secondOrderTestData);
        loginWeb.loginCue(secondOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(secondOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
    }
}
