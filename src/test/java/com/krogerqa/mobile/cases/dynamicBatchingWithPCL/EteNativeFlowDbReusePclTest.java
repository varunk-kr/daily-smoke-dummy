package com.krogerqa.mobile.cases.dynamicBatchingWithPCL;

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
 * Scenario FFILLSVCS-TC-9503 Mobile Flows -
 * Perform destaging for base order.
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native and resuing the pcl labels from pickedup order >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbReusePclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify de-staging and checkout for non ebt dynamic batching base order")
    public void validateE2eNonEbtDbReusePclOrderCheckoutBaseOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503_1, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Perform selecting for non ebt dynamic batching order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtDbReusePclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Perform staging for non ebt dynamic batching order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtDbReusePclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, testOutputData);
    }

    @Test(groups = {"ete_db_pcl_tc_9503_reusePCl_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non ebt dynamic batching order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtDbReusePclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_REUSE_TC_9503, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
