package com.krogerqa.mobile.cases.harrisTeeterDynamicBatching;

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
 * Scenario FFILLSVCS-TC-14370 Mobile Flows -MOVE AMBIENT TO AMBIENT
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Move all items from picked to new container through order look-up screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowHarrisTeeterDyBPickingItemMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14370_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14370, ExcelUtils.HARRIS_TEETER_DYB_14370);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.HARRIS_TEETER_DYB_14370, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14370_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14370, ExcelUtils.HARRIS_TEETER_DYB_14370);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.HARRIS_TEETER_DYB_14370, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14370_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14370, ExcelUtils.HARRIS_TEETER_DYB_14370);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HARRIS_TEETER_DYB_14370, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
