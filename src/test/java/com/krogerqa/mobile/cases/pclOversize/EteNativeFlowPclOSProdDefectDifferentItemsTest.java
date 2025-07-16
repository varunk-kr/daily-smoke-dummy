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
 * Scenario PCL_OS_DEFECT_DIFFERENT_ITEMS Non-Ebt Mobile Flows -
 * Perform Selecting by assigning pcl and performing picking As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPclOSProdDefectDifferentItemsTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderFirstTrolleySelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeFirstTrolleyStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_native_part3_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderSecondTrolleySelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_native_part4_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeSecondOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_differentItems_native_part5_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize order")
    public void validateE2eNonEbtPickedOsToNewPclOversizeOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OS_DEFECT_DIFFERENT_ITEMS, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
