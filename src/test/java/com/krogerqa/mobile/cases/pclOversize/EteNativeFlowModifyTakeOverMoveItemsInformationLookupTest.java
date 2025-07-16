package com.krogerqa.mobile.cases.pclOversize;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12165 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native>
 * While selecting Take over trolley and move items via Picking screen from Refrigerated to existing Refrigerated containers
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowModifyTakeOverMoveItemsInformationLookupTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12165_native_part1_modifyTakeOverItemMovementViaInformationLookup_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for take over run for carry over trolley")
    public void validateE2eTakeOverItemMovementViaInformationLookupOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteNativeFlowModifyTakeOverMoveItemsInformationLookupTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1_1);
            testOutputData = harvesterSelectingAndStaging.takeOverRun(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1);
            testOutputData = harvesterSelectingAndStaging.takeOverRun(ExcelUtils.PCL_OVERSIZE_TC_12165_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pclOversize_tc_12165_native_part2_modifyTakeOverItemMovementViaInformationLookup_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for take over run for carry over trolley")
    public void validateE2eTakeOverItemMovementViaInformationLookupOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if ((EteNativeFlowModifyTakeOverMoveItemsInformationLookupTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1);
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12165_1, testOutputData);
        }
    }

    @Test(groups = {"ete_pclOversize_tc_12165_native_part3_modifyTakeOverItemMovementViaInformationLookup_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for take over item Movement via picking order")
    public void validateE2eNonEbtPclTakeOverItemMovementViaInformationLookupOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        if ((EteNativeFlowModifyTakeOverMoveItemsInformationLookupTest.testOutputData.containsKey(ExcelUtils.IS_COMPOSITE_MODIFY_ORDER))) {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12165_1_1, testOutputData);
        } else {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12165_1, ExcelUtils.PCL_OVERSIZE_TC_12165_1);
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12165_1, testOutputData);
        }
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
