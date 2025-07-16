package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * PCL_SCENARIO_TC_4551 mobile flows
 * Perform Selecting for one trolley which is combined for both order
 * After cancelling the first order from cue, perform picking for 2nd trolley in non-cancelled order
 * Stage the container for the order
 * Perform ciao checkout and paid status
 */


public class EteNativeFlowMultipleOrderTrolleyCancelTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4551_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eSelectingFirstTrolley() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4551_1, ExcelUtils.PCL_SCENARIO_TC_4551_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4551_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_4551_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersSelectingRemainingTrolleys() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4551_1, ExcelUtils.PCL_SCENARIO_TC_4551_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4551_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_4551_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4551_1, ExcelUtils.PCL_SCENARIO_TC_4551_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4551_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_4551_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_SCENARIO_TC_4551_1, ExcelUtils.PCL_SCENARIO_TC_4551_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4551_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}