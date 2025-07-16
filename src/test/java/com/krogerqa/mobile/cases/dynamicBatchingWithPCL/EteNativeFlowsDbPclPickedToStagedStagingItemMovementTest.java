package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario DB_PCL_TC-9512 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native > Move all items from picked AM container to existing staged RE container from staging screen
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowsDbPclPickedToStagedStagingItemMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9512_staging_PickedToStaged_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for picked to staged container movement from staging screen db non-EBT pcl order")
    public void validateE2eDbPclPickedToStagedStagingScreenOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9512_staging_PickedToStaged_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Stage the source FR Container for Item Movement of a non-Ebt order")
    public void verifyE2eItemMovementToContainerStatus() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512, ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if (PermanentContainerLabelHelper.getItemMovementToContainerStatus(testOutputData).equals(ExcelUtils.STAGED)) {
            harvesterSelectingAndStaging.stageToContainer(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512, testOutputData);
        }
    }

    @Test(groups = {"ete_db_pcl_tc_9512_staging_PickedToStaged_native_part3_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for staged to picked container container movement from staging screen db non-EBT pcl order")
    public void validateE2eDbPclPickedToStagedStagingScreenOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9512_staging_PickedToStaged_native_part4_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for staged to picked container movement from staging screen for db non-EBT pcl order")
    public void validateE2eDbPclPickedToStagedStagingScreenOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_IM_PCL_TC_9512, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
