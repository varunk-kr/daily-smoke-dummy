package com.krogerqa.mobile.cases.dynamicBatchingWithOS;

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
 * Scenario FFILLSVCS-TC-13625 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbOSCancelFromCueTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_dybOS_tc_13625_native_part1_cancelFromCue", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for db os pcl order")
    public void validateE2eDbPclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, ExcelUtils.DYNAMIC_BATCH_OS_TC_13625);
        testOutputData.put(ExcelUtils.IS_MULTIPLE_ORDER_PCL_OS, String.valueOf(true));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13625_native_part2_cancelFromCue", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db os pcl order")
    public void validateE2eDbPclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, ExcelUtils.DYNAMIC_BATCH_OS_TC_13625);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, testOutputData);
    }

    @Test(groups = {"ete_dybOS_tc_13625_native_part3_cancelFromCue", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for db os pcl order")
    public void validateE2eDbPclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, ExcelUtils.DYNAMIC_BATCH_OS_TC_13625);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_OS_TC_13625, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
