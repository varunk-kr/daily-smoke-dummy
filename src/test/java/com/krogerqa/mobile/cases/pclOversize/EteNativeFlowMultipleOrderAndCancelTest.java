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
 * Scenario FFILLSVCS-TC-12131 Non-Ebt Mobile Flows -
 * Cancel one order and perform picking for the other order >
 * Perform Selecting by performing take over run in assigning status As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */


public class EteNativeFlowMultipleOrderAndCancelTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_pclOversize_tc_12131_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eSelectingFirstTrolley() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131, ExcelUtils.PCL_OVERSIZE_TC_12131);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12131, firstOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12131_native_part2_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12131_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_tc_12131_native_part3_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12131_1, ExcelUtils.PCL_OVERSIZE_TC_12131_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_OVERSIZE_TC_12131, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}