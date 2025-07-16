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
 * Scenario PCL TC 4509 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage one container to which some items should be moved >
 * Move some items from picked Ambient to Staged Frozen container from container lookup screen >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPickedAmbientToStagedFrozenContainerMovementTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4509_existingContainerItemMovement_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order for Picked AM to Staged FR Container item movement via Container Lookup screen  ")
    public void validateE2eNonEbtPclItemMovementOrderPicking() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4509,ExcelUtils.PCL_SCENARIO_TC_4509);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4509, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4509_existingContainerItemMovement_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Stage the destination Frozen Container for non ebt order for Picked AM to Staged FR Container item movement via Container Lookup screen ")
    public void verifyItemMovementContainerStatus() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4509,ExcelUtils.PCL_SCENARIO_TC_4509);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if (PermanentContainerLabelHelper.getItemMovementToContainerStatus(testOutputData).equals(ExcelUtils.STAGED)) {
            harvesterSelectingAndStaging.stageToContainer(ExcelUtils.PCL_SCENARIO_TC_4509, testOutputData);
        }
    }

    @Test(groups = {"ete_pcl_tc_4509_existingContainerItemMovement_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Perform Item Movement and Verify staging for non ebt order for Picked AM to Staged FR Container item movement via Container Lookup screen")
    public void validateE2eNonEbtPclItemMovementOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4509,ExcelUtils.PCL_SCENARIO_TC_4509);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.PCL_SCENARIO_TC_4509, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4509_existingContainerItemMovement_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order for Picked AM to Staged FR Container item movement via Container Lookup screen")
    public void validateE2eNonEbtPclItemMovementOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4509,ExcelUtils.PCL_SCENARIO_TC_4509);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4509, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
