package com.krogerqa.mobile.cases.dynamicBatchingWithoutPCL;

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
 * Scenario DYNAMIC_BATCH_TC_5577 Mobile Flows
 * Perform Selecting by picking items As Ordered after cancelling one order for Multi-threaded Non -Pcl store using Harvester Native for dynamic batching enabled store >
 * Stage containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDynamicBatchingCancelFromKrogerTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_db_tc_5577_native_part1_cancelFromKroger", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Dynamic batching non-EBT pcl order")
    public void validateEteDynamicBatchingCancelFromKSPOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5577, ExcelUtils.DYNAMIC_BATCH_TC_5577);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5577, testOutputData);
    }

    @Test(groups = {"ete_db_tc_5577_native_part2_cancelFromKroger", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for for Dynamic batching non-EBT pcl order")
    public void validateEteDynamicBatchingCancelFromKSPOrderStaging() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5577, testOutputData);
    }

    @Test(groups = {"ete_db_tc_5577_native_part3_cancelFromKroger", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for Dynamic batching non-EBT pcl order")
    public void validateEteDynamicBatchingCancelFromKSPOrderCheckout() {
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5577, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
