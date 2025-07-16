package com.krogerqa.mobile.cases.harvesterWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-4529 Mobile Flows -
 * Take over trolley while assigning
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native and mark one trolley as out of stock>
 * After picking move items via container lookup screen from ambient to Refrigerated container
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 * Note: Execute 2 native testcases after 2 web testcases.
 */
public class EteNativeFlowTakeOverOOSMoveItemsFromAmbientToRefrigeratedTest extends BaseTest {
    static HashMap<String, String> secondOrderData = new HashMap<>();
    static HashMap<String, String> firstOrderData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4529_native_part1_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify take over run in assigning ")
    public void validateE2eNonEbtPclItemMovementViaPreWeighedOrderSelecting() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529_1, ExcelUtils.PCL_SCENARIO_TC_4529_1);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderData = harvesterSelectingAndStaging.verifyHarvesterTakeOverAssigningPcl(ExcelUtils.PCL_SCENARIO_TC_4529, secondOrderData);
        harvesterSelectingAndStaging.assignNewPclLabel(ExcelUtils.PCL_SCENARIO_TC_4529, secondOrderData, firstOrderData);
    }

    @Test(groups = {"ete_pcl_tc_4529_native_part2_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify selecting for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPreWeighedOrderSelecting() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529_1, ExcelUtils.PCL_SCENARIO_TC_4529_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        harvesterSelectingAndStaging.takeOverRunAndSelecting(ExcelUtils.PCL_SCENARIO_TC_4529, secondOrderData, Constants.PickCreation.ASSIGNING);
    }

    @Test(groups = {"ete_pcl_tc_4529_native_part3_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPreWeighedOrderStaging() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.performItemMovementOrderLookupAndStage(ExcelUtils.PCL_SCENARIO_TC_4529);
    }

    @Test(groups = {"ete_pcl_tc_4529_native_part4_takeOverAssigningItemMovementFromAmbientToRefrigeratedViaContainerLookup_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for for take over in assigning and item Movement via container lookup screen")
    public void validateE2eNonEbtPclTakeOverItemMovementViaPreWeighedOrderCheckout() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4529, ExcelUtils.PCL_SCENARIO_TC_4529);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4529, secondOrderData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
