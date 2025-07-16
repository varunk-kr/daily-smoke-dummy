package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * Scenario 1 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native > Move some items from staged AM container to existing picked FR container from staging screen
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowStagedAmbientToPickedFrozenContainerMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4527_containerMovement_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order for Staged AM to Picked FR Container item movement via Staging screen")
    public void validateE2eNonEbtPclItemMovementOrderPicking() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4527, ExcelUtils.PCL_SCENARIO_TC_4527);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4527, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4527_containerMovement_native_part2", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Stage the source Ambient Container for Item Movement of a non-Ebt order")
    public void verifyItemMovementContainerStatus() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4527, ExcelUtils.PCL_SCENARIO_TC_4527);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if (PermanentContainerLabelHelper.getItemMovementFromContainerStatus(testOutputData).equals(ExcelUtils.STAGED)) {
            harvesterSelectingAndStaging.stageFromContainer(ExcelUtils.PCL_SCENARIO_TC_4527, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_4527_containerMovement_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Perform Item Movement and Verify staging for non ebt order for Staged AM to Picked FR Container item movement via Staging screen")
    public void validateE2eNonEbtPclItemMovementOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4527, ExcelUtils.PCL_SCENARIO_TC_4527);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4527, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4527_containerMovement_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order for Staged to Picked Container item movement via Staging screen")
    public void validateE2eNonEbtPclItemMovementOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4527, ExcelUtils.PCL_SCENARIO_TC_4527);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4527, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}