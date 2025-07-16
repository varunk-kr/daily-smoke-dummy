package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

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
 * Scenario DB_PCL_TC-9502 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Complete staging only for one container.
 * Move All items from staged to existing picked container of same temp type through container look-up screen
 * Stage remaining containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbPclCLStagedToPickedSameTempExistingMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9502_CLS_StagedToExisting_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for existing same temperature container movement from containerLookup non-EBT pcl order")
    public void validateE2eDbPclStagedToExistingContainerMovementLookupScreenOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9502_CLS_StagedToExisting_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify item movement and staging for existing same temperature container movement from containerLookup non-EBT pcl order")
    public void validateE2eDbPclStagedToExistingContainerMovementLookupScreenOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502, ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502, testOutputData);
    }


    @Test(groups = {"ete_db_pcl_tc_9502_CLS_StagedToExisting_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part5_multiThreaded"}, description = "Verify de-staging and checkout for existing same temperature container movement from containerLookup non-EBT pcl order")
    public void validateE2eDbPclStagedToExistingContainerMovementLookupScreenOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502, ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9502, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
