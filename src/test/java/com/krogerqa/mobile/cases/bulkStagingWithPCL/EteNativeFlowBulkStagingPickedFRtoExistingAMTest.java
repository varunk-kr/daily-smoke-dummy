package com.krogerqa.mobile.cases.bulkStagingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BULK_STAGING_TC_11749 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Move SOME items from Picked Frozen to Existing Ambient Container through Order Lookup screen >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete bulk destaigng checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingPickedFRtoExistingAMTest extends BaseTest {

        static HashMap<String, String> testOutputData = new HashMap<>();
        CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
        HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
        HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();

        WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
        LoginNative loginNative = LoginNative.getInstance();

        @Test(groups = {"ete_pcl_tc_11749_bulkStaging_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for bulk staging order")
        public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderSelecting() {
            testOutputData= ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11749,ExcelUtils.BULK_STAGING_TC_11749);
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11749, testOutputData);
        }

        @Test(groups = {"ete_pcl_tc_11749_bulkStaging_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify item movement and staging for bulk staging order")
        public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderStaging() {
            testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11749, ExcelUtils.BULK_STAGING_TC_11749);
            loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            PermanentContainerLabelHelper.performItemMovementViaOrderLookup(ExcelUtils.BULK_STAGING_TC_11749, testOutputData);
            harvesterSelectingAndStaging.changeStoreSetup();
            testOutputData.put(ExcelUtils.ITEM_MOVEMENT, String.valueOf(false));
            harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11749, testOutputData);
        }

        @Test(groups = {"ete_pcl_tc_11749_bulkStaging_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for bulk staging order")
        public void validateE2eNonEbtPclBulkStagingPickedFRToExistingAMOrderCheckout() {
            testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11749,ExcelUtils.BULK_STAGING_TC_11749);
            loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
            ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11749, testOutputData);
        }

        private void loginUsingOktaSignIn(String userName, String password) {
            welcomeToChromePage.acceptChromeTerms();
            loginNative.LoginOktaSignIn(userName, password);
        }
    }

