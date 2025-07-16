package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario TC-9873 Web Flows -
 * Submit 3 Non-EBT Pcl Pickup rush orders and normal order in Kroger.com for single threaded store and cancel one rush order >
 * Verify the rush order Label and count in cue.
 * Verify trolleys generated and verify New Non-EBT Order Details in Cue and assign pcl labels >
 * Complete selecting and Stage the rush orders.
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify both rush orders are dropped from Dash >
 * Verify each order is Picked Up and Paid in Cue and validate K-log
 */

public class EteWebFlowMultipleRushNonEbtOrderRemoveTC52NotificationForOneOrderTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    CueHomePage cueHomePage = CueHomePage.getInstance();

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part1", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify multiple rush orders placed for non-EBT single thread order in same store non ebt")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalNewAndCancelOrdersPlaced() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        if (!(thirdOrderTestData == null)) {
            thirdOrderTestData.clear();
        }
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SHEET_NAME_TEST_DATA);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_1, ExcelUtils.SHEET_NAME_TEST_DATA);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SHEET_NAME_TEST_DATA);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueHomePage.verifyRushOrderInitialCount();
        firstOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        secondOrderTestData.put(ExcelUtils.IS_RUSH_ORDER, String.valueOf(true));
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_1, secondOrderTestData);
        thirdOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyCancelOrderFromCue(secondOrderTestData);
        cueOrderValidation.verifyCanceledStatusInCue(secondOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        firstOrderTestData = cueOrderValidation.getItemCount(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part2", "ete_web_part2_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify multiple orders picked for non-EBT order in same store")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersPicked() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(firstOrderTestData.get(ExcelUtils.STORE_ID)));
        thirdOrderTestData = cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
        cueOrderValidation.updatePclMapsAfterPicking(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_part3", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify multiple orders staged and checked-in for non-EBT order single Thread pcl TC52 Remove Notifications in Little Bird App")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyStagedPclStatusInCue(thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tC_9873_multipleRush_normalOrder_TC52_nonEbt", "ete_web_part5_nonEbt", "ete_web_part5_multiThreaded"}, description = "Verify multiple orders picked up and paid for non-EBT order in single Thread pcl TC52 Remove Notifications in Little Bird App")
    public void validateE2ePclSingleThreadTC52MultipleRushAndNormalOrdersPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873, ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2, ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
        cueOrderValidation.verifyOrderDroppedFromDash(firstOrderTestData);
        cueOrderValidation.verifyOrderDroppedFromDash(thirdOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9873);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(thirdOrderTestData.get(ExcelUtils.STORE_ID)));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9873_2);
    }

}
