package com.krogerqa.mobile.cases.singleThreadWithPCL;

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
 * Scenario SINGLE_THREAD_PCL_TC_9006 Mobile Flows -
 * Perform taking over run from In Progress trolleys section using Harvester Native>
 * Stage containers using Harvester Native >
 * Move all items from move all item from Ambient container to an existing Refrigerated  via Staging screen using Harvester Native>
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class EteNativeFlowRushOrderItemMovementAmbientToRefrigeratedTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_singleThread_tc_9006_native_part1_RushOrder_ItemMovement_AmbientToFrozen", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify rush order placed pcl rush order Item Movement")
    public void validateE2eRushOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, ExcelUtils.SINGLE_THREAD_PCL_TC_9006);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, testOutputData);
    }

    @Test(groups = {"ete_singleThread_tc_9006_native_part2_RushOrder_ItemMovement_AmbientToFrozen", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for pcl rush order Item Movement")
    public void validateE2eRushOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, ExcelUtils.SINGLE_THREAD_PCL_TC_9006);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, testOutputData);
    }

    @Test(groups = {"ete_singleThread_tc_9006_native_part3_RushOrder_ItemMovement_AmbientToFrozen", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for pcl rush order Item Movement ")
    public void validateE2eRushOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, ExcelUtils.SINGLE_THREAD_PCL_TC_9006);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.SINGLE_THREAD_PCL_TC_9006, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
