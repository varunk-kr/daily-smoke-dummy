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
 * Scenario PCL_TC_4511 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Move items from ambient to refrigerated container from staging screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowAmbientToRefrigeratedTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();

    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4511_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Item movement (Picked Ambient to Picked Refrigerated) Non EBT Order")
    public void validateE2eAmbientToRefrigeratedPclOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4511,ExcelUtils.PCL_SCENARIO_TC_4511);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4511, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4511_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Item movement (Picked Ambient to Picked Refrigerated) Non EBT Order")
    public void validateE2eAmbientToRefrigeratedPclOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4511,ExcelUtils.PCL_SCENARIO_TC_4511);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4511, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4511_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for for Item movement (Picked Ambient to Picked Refrigerated) Non EBT Order")
    public void validateE2eAmbientToRefrigeratedPclOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4511,ExcelUtils.PCL_SCENARIO_TC_4511);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4511, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
