package com.krogerqa.mobile.cases.harvesterWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_TC_4566 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native and reusing the pcl labels from picked-up order >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class SmokeEteNativeFlowReusePclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4566_native_part1_reusePcl_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderSelectingAndStagingBaseOrder() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4566_1,ExcelUtils.PCL_SCENARIO_TC_4566_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4566_1, testOutputData);
        PermanentContainerLabelHelper.navigateToHomeScreen();
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4566_1, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4566_native_part2_reusePcl_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderCheckoutBaseOrder() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4566_1,ExcelUtils.PCL_SCENARIO_TC_4566_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4566_1, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4566_native_part3_reusePcl_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Perform selecting for non ebt order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReUsePclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4566, ExcelUtils.PCL_SCENARIO_TC_4566);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4566, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4566_native_part4_reusePcl_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Perform staging for non ebt order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4566, ExcelUtils.PCL_SCENARIO_TC_4566);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4566, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4566_native_part5_reusePcl_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4566, ExcelUtils.PCL_SCENARIO_TC_4566);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4566, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}