package com.krogerqa.web.cases.harvesterWithPCL;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import com.krogerqa.web.ui.pages.cue.CueOrderDetailsPage;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-4477 Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels >
 * After selecting, verify Picked order and container status in Cue using assigned pcl labels >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate Klog
 */
public class EteWebFlowMoveItemsPreWeighedScreenExistingTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    CueHomePage  cueHomePage= CueHomePage.getInstance();
    CueOrderDetailsPage cueOrderDetailsPage= CueOrderDetailsPage.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    @Test(groups = {"ete_pcl_tc_4477_web_part1_itemMovementViaPreWeighedToExisting_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eNewNonEbtPclItemMovementViaPreWeighedOrder() {
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.PCL_SCENARIO_TC_4477);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.PRE_WEIGHED_CONTAINER, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_SCENARIO_TC_4477, testOutputData);
        baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)));
        cueHomePage.searchOrderId(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        cueOrderDetailsPage.getVisualOrderIdAndOpenDetails();
        cueOrderValidation.getPclPreWeighedContainer(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4477_web_part2_itemMovementViaPreWeighedToExisting_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eNonEbtPclItemMovementViaPreWeighedOrderPicked() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4477,ExcelUtils.PCL_SCENARIO_TC_4477);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.PCL_SCENARIO_TC_4477, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4477_web_part3_itemMovementViaPreWeighedToExisting_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eNonEbtPclItemMovementViaPreWeighedOrderStaged() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4477,ExcelUtils.PCL_SCENARIO_TC_4477);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4477_web_part4_itemMovementViaPreWeighedToExisting_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked for item Movement via pre weighed pcl order")
    public void validateE2eNonEbtPclItemMovementViaPreWeighedOrderPaid() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4477,ExcelUtils.PCL_SCENARIO_TC_4477);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_SCENARIO_TC_4477);
    }
}
