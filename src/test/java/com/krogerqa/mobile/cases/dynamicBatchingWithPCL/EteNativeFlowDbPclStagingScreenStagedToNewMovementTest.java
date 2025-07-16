package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStagingPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-9515 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Move all items from staged to new container through staging screen
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbPclStagingScreenStagedToNewMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeStagingPage harvesterNativeStagingPage = HarvesterNativeStagingPage.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9515_staging_StagedToNew_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for new container movement from staging screen for db non ebt order")
    public void validateE2eDbPclStagedToNewContainerMovementStagingScreenOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9515, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9515_staging_StagedToNew_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for db for new container movement from staging screen for db non ebt pcl order")
    public void validateE2eDbPclStagedToNewContainerMovementStagingScreenOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData);
        PermanentContainerLabelHelper.performItemMovementViaStaging(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9515,testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9515_staging_StagedToNew_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for new container movement from staging screen for db non-EBT pcl order")
    public void validateE2eDbPclStagedToNewContainerMovementStagingScreenOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9515, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
