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
 * Scenario FFILLSVCS-TC-12136 Non-Ebt Mobile Flows -
 * Perform Selecting by assigning pcl and performing picking As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, cancel order from ciao for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPclOversizeCancelFromCiaoTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pclOversize_tc_12136_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_pclOversize"}, description = "Verify selecting for pcl oversize cancel from ciao order")
    public void validateE2eNonEbtCancelFromCiaoPclOversizeOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12136, ExcelUtils.PCL_OVERSIZE_TC_12136);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12136, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12136_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_pclOversize"}, description = "Verify staging for pcl oversize cancel from ciao order")
    public void validateE2eNonEbtCancelFromCiaoPclOversizeOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12136, ExcelUtils.PCL_OVERSIZE_TC_12136);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_OVERSIZE_TC_12136, testOutputData);
    }

    @Test(groups = {"ete_pclOversize_tc_12136_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_pclOversize"}, description = "Verify de-staging and checkout for pcl oversize cancel from ciao order")
    public void validateE2eNonEbtCancelFromCiaoPclOversizeOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12136, ExcelUtils.PCL_OVERSIZE_TC_12136);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyCancelOrderInCiao(testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
