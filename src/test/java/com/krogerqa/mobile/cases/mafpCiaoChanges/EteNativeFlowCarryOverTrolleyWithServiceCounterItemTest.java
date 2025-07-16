package com.krogerqa.mobile.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10432
 * Perform Carryover Trolley Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Sell by Service counter sell by unit and partially fulfill it.
 * Sell by Service counter sell by weight
 * Stage non-Pcl  containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowCarryOverTrolleyWithServiceCounterItemTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10432_native_part1_mafp_ciao", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for carry over non ebt order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432,ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10432_native_part2_mafp_ciao", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for carry over non ebt order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432,ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10432_native_part3_mafp_ciao", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for carry over non ebt order")
    public void validateE2ePclMafpCiaoChangesCarryOverOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432,ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10432, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
