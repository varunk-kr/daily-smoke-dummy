package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * Scenario TC_4539 Mobile Flows -
 * Perform Carryover Trolley Selecting by taking over run from In Progress trolleys section using Harvester Native>
 * Pick remaining trolley using Harvester Native>
 * Stage containers using Harvester Native >
 * Move all items from AM container to new Container via Staging screen using Harvester Native>
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class SmokeEteNativeFlowTakeOverCarryOverTrolleyPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4539_native_part1_carryOver_takeOver_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for take over run for carry over trolley")
    public void validateE2eTakeOverTrolleyPclOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4539,ExcelUtils.PCL_SCENARIO_TC_4539);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.takeOverRun(ExcelUtils.PCL_SCENARIO_TC_4539, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4539_native_part2_carryOver_takeOver_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for take over run for carry over trolley")
    public void validateE2eTakeOverTrolleyPclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4539, ExcelUtils.PCL_SCENARIO_TC_4539);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.stageAllContainers(testOutputData);
        harvesterSelectingAndStaging.performItemMovement(ExcelUtils.PCL_SCENARIO_TC_4539, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4539_native_part3_carryOver_takeOver_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for take over run for carry over trolley")
    public void validateE2eTakeOverTrolleyPclOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4539,ExcelUtils.PCL_SCENARIO_TC_4539);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4539, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
