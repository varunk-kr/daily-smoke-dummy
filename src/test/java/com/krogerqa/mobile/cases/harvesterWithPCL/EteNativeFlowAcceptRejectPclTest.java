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
 * Scenario pcl_tc_4556 Mobile Flows
 * Perform selecting with marking item as Accept and Reject  for pcl Multi-threaded store using Harvester Native >
 * * Stage Pcl containers using Harvester Native >
 * * After Customer Check-in, Destage containers and complete Checkout for EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowAcceptRejectPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_4556_native_part1_ebtAcceptReject", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl EbtAcceptReject order")
    public void validateE2ePclPickingSubsAcceptReject() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4556_native_part2_ebtAcceptReject", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging for pcl EbtAcceptReject order")
    public void validateE2ePclStagingSubsAcceptReject() {
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4556_native_part3_ebtAcceptReject", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for EbtAcceptReject pcl order")
    public void validateE2eEbtPclAcceptRejectCiaoCheckout() {
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemPclInCiao(ExcelUtils.PCL_SCENARIO_TC_4556, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
