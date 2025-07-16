package com.krogerqa.mobile.cases.collapseTemperatureWithPCL;

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
 * Scenario pcl_with_collapse_temp_tc_5492 Mobile Flows -
 * While Selecting move all ambient items to existing ambient container >
 * Pick all other items as ordered>
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowCollapseTempFrozenRefrigeratorContainersTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateExistingAMContainerItemMovementPickingScreenOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_5492, testOutputData);
    }


    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateExistingAMContainerItemMovementPickingScreenOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_5492, testOutputData);
    }

    @Test(groups = {"ete_pcl_collapseTemp_tc_5492_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for existing container item movement from refrigerated to ambient using containerLookup for non-EBT pcl order with collapse temperature")
    public void validateExistingAMContainerItemMovementPickingScreenOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_5492, ExcelUtils.PCL_SCENARIO_TC_5492);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_5492, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}