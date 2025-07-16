package com.krogerqa.mobile.cases.pclOversize;

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
 * Scenario FFILLSVCS-TC-12239 Non-Ebt Mobile Flows -
 * Perform Selecting by assigning pcl and performing picking As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Move items from Picked Frozen to existing Frozen container through Staging screen >
 * Stage Pcl containers using Harvester Native in anchor staging zones >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowNonEbtPickedFrToExistingStagedFrPclTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12239_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize picked fr to existing staged fr item movement order")
    public void validateE2eNonEbtPickedFrToExistingFrPclOversizeOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12239, ExcelUtils.PCL_OVERSIZE_TC_12239);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12239, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12239_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Stage the destination Frozen Container for non ebt order for Picked FR to Staged FR Container item movement via Staging screen ")
    public void validateE2eNonEbtPickedFrToExistingFrPclOversizeToContainerOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12239,ExcelUtils.PCL_OVERSIZE_TC_12239);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        if (PermanentContainerLabelHelper.getItemMovementToContainerStatus(testOutputData).equals(ExcelUtils.STAGED)) {
            harvesterSelectingAndStaging.stageToContainer(ExcelUtils.PCL_OVERSIZE_TC_12239, testOutputData);
        }
    }

    @Test(groups = {"ete_pclOversize_tc_12239_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for pcl oversize picked fr to existing staged fr item movement order")
    public void validateE2eNonEbtPickedFrToExistingFrPclOversizeOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12239, ExcelUtils.PCL_OVERSIZE_TC_12239);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.ITEM_MOVEMENT_FROM_PICKED_TO_STAGED, String.valueOf(true));
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.PCL_OVERSIZE_TC_12239, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12239_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize picked fr to existing staged fr item movement order")
    public void validateE2eNonEbtPickedFrToExistingFrPclOversizeOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12239, ExcelUtils.PCL_OVERSIZE_TC_12239);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12239, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
