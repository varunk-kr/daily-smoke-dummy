package com.krogerqa.mobile.cases.pclOversize;

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
 * Scenario PCL_OS_PROD_DEFECT_MOVE_ITEMS Non-Ebt Mobile Flows -
 * Perform Selecting of one container by assigning pcl and performing picking for 1st trolley for Multi-threaded Pcl store using Harvester Native >
 * Move items from picked trolley to new PL container and reuse the same pcl label for picking 2nd trolley
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPclOSProdDefectItemMovementTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_prod_defect_ItemMovement_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 1st trolley for pcl oversize trolley")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderFirstTrolleySelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_ItemMovement_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 1st pcl oversize trolley and move items to new container")
    public void validateE2eNonEbtPickedOsToNewPclOversizeFirstTrolleyStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_ItemMovement_native_part3_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for 2nd pcl oversize trolley")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderSecondTrolleySelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_ItemMovement_native_part4_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for 2nd pcl oversize trolley")
    public void validateE2eNonEbtPickedOsToNewPclOversizeSecondOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS);
        testOutputData.put(ExcelUtils.ITEM_MOVEMENT, String.valueOf(false));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_ItemMovement_native_part5_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize for all containers")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OS_PROD_DEFECT_MOVE_ITEMS, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
