package com.krogerqa.mobile.cases.byob;

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
 * Scenario BYOB-TC-8528 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native>
 * While selecting Take over trolley and complete picking.
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowTakeOverBYOBTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_native_part1", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for take over non-EBT BYOB order")
    public void validateE2eByobOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        testOutputData.put(ExcelUtils.IS_TAKE_OVER_TROLLEY,String.valueOf(true));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify selecting for take over non-EBT BYOB order")
    public void validateE2eByobOrderTakeOverSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        testOutputData = harvesterSelectingAndStaging.takeOverRun(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_native_part3", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for take over non-EBT BYOB order")
    public void validateE2eByobOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);
    }

    @Test(groups = {"ete_byob_tc_8528_takeOverOrder_native_part4", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for take over non-EBT BYOB order")
    public void validateE2eByobOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_8528, ExcelUtils.BYOB_PCL_TC_8528);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BYOB_PCL_TC_8528, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
