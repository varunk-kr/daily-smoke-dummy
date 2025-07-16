package com.krogerqa.web.cases.singleThreadWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario SINGLE_THREAD_PCL_TC_9002 Web Flows -
 * Submit Non-EBT Pickup order for Single-threaded store using API >
 * Assign PCL to one trolley via API and pick one 1 item from the trolley via API
 * take over the in progress trolley, verify Picked order and container status in Cue >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Move all items from AM container to new Container via Staging screen using Harvester Native>
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowNonEbtSingleThreadPclTakeOverTrolleyTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_singleThread_pcl_tc_9002_web_part1_takeOverTrolley_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_singleThreaded"}, description = "Verify non-EBT order placed for take over trolley")
    public void validateE2eTakeOverTrolleyPclNewOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SHEET_NAME_TEST_DATA);
        testOutputData.put(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_TAKE_OVER_TROLLEY, String.valueOf(true));
        testOutputData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_web_part1_2_takeOverTrolley_nonEbt", "ete_web_part1_2_nonEbt", "ete_web_part1_2_singleThreaded"}, description = "Verify order picking for for take over run trolley")
    public void validateE2eTakeOverTrolleyPclOrderPicking() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickingStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_web_part2_takeOverTrolley_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_singleThreaded"}, description = "Verify order picked for for take over run trolley")
    public void validateE2eTakeOverTrolleyPclOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_web_part3_takeOverTrolley_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_singleThreaded"}, description = "Verify order staged and checked-in for take over trolley")
    public void validateE2eTakeOverTrolleyPclOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_singleThread_pcl_tc_9002_web_part4_takeOverTrolley_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_singleThreaded"}, description = "Verify order picked up and paid for take over trolley")
    public void validateE2eTakeOverTrolleyPclOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9002, ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.SINGLE_THREAD_PCL_TC_9002);
    }
}
