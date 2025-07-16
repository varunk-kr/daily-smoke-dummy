package com.krogerqa.mobile.cases.pclOversize;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-12140 Non-Ebt Mobile Flows -
 * Perform Selecting by performing take over run in assigning status As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowMultipleOrdersTakeOverAssigningPclTest extends BaseTest {
    static HashMap<String, String> secondOrderData = new HashMap<>();
    static HashMap<String, String> firstOrderData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12140_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeFirstOrderSelecting() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140_1, ExcelUtils.PCL_OVERSIZE_TC_12140_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12140_1, firstOrderData);
    }

    @Test(groups = {"ete_pclOversize_tc_12140_native_part2_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeSecondOrderAssigning() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140_1, ExcelUtils.PCL_OVERSIZE_TC_12140_1);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140, ExcelUtils.PCL_OVERSIZE_TC_12140);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderData = harvesterSelectingAndStaging.verifyHarvesterTakeOverAssigningPcl(ExcelUtils.PCL_OVERSIZE_TC_12140, secondOrderData);
        harvesterSelectingAndStaging.assignNewOsLabel(ExcelUtils.PCL_OVERSIZE_TC_12140, secondOrderData);
    }

    @Test(groups = {"ete_pclOversize_tc_12140_native_part3_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify selecting for pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeSecondOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140, ExcelUtils.PCL_OVERSIZE_TC_12140);
        harvesterSelectingAndStaging.takeOverRunAndSelecting(ExcelUtils.PCL_OVERSIZE_TC_12140, secondOrderData, Constants.PickCreation.ASSIGNING);
    }

    @Test(groups = {"ete_pclOversize_tc_12140_native_part4_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeOrderStaging() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140, ExcelUtils.PCL_OVERSIZE_TC_12140);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12140, secondOrderData);
    }

    @Test(groups = {"ete_pclOversize_tc_12140_native_part5_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for pcl oversize order")
    public void validateE2eNonEbtTakeOverAssigningPclOversizeOrderCheckout() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12140, ExcelUtils.PCL_OVERSIZE_TC_12140);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12140, secondOrderData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
