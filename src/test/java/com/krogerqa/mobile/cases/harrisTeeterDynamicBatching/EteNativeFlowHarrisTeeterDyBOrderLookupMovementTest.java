package com.krogerqa.mobile.cases.harrisTeeterDynamicBatching;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.mobile.ui.pages.toggle.NativeTogglePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-14380 Mobile Flows -MOVE AMBIENT TO AMBIENT
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Move all items from picked to new container through order look-up screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowHarrisTeeterDyBOrderLookupMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    NativeTogglePage nativeTogglePage = NativeTogglePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        nativeTogglePage.handleToggle(testOutputData);
        PermanentContainerLabelHelper.performItemMovementViaOrderLookup(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
        harvesterSelectingAndStaging.changeStoreSetup();
        testOutputData.put(ExcelUtils.ITEM_MOVEMENT, String.valueOf(false));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
    }

    @Test(groups = {"ete_harrisTeeterDyB_pcl_14380_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for new container movement from order lookup ht db non-EBT pcl order")
    public void validateE2eHarrisTeeterDbPclOrderLookupMovementOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.HARRIS_TEETER_DYB_14380, ExcelUtils.HARRIS_TEETER_DYB_14380);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HARRIS_TEETER_DYB_14380, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
