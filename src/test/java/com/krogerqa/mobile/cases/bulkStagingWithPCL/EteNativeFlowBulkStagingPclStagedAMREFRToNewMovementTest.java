package com.krogerqa.mobile.cases.bulkStagingWithPCL;

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
 * Scenario BULK_STAGING FFILLSVCS-TC-11752 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers,and reStage containers
 * Perform item movement from Ambient ,refrigerated and frozen to new container using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingPclStagedAMREFRToNewMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for bulk staging, re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validatePclBulkStagingMultipleContainerItemMovementOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11752, testOutputData);
    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for bulk staging ,re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validatePclBulkStagingMultipleContainerItemMovementOrderStagingReStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11752, testOutputData);

    }

    @Test(groups = {"ete_bulkStaging_pcl_tc_11752_newMultipleContainerItemMovement_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for bulk staging ,re-staging and item movement from staged AM,RE &FR to a new container from order Lookup flow")
    public void validatePclBulkStagingMultipleContainerItemMovementOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11752, ExcelUtils.BULK_STAGING_TC_11752);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11752, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
