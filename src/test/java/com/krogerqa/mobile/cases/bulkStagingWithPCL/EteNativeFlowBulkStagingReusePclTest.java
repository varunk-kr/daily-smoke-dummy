package com.krogerqa.mobile.cases.bulkStagingWithPCL;

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
 * Scenario BULK_STAGING_TC_11728 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Bulk Staging Pcl store using Harvester Native and reusing the pcl labels from pickup order >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowBulkStagingReusePclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11728_native_part1_reusePcl_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify de-staging and checkout for non ebt order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderCheckoutBaseOrder() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728_1,ExcelUtils.BULK_STAGING_TC_11728_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11728_1, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_native_part2_reusePcl_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Perform selecting for non ebt order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.BULK_STAGING_TC_11728, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11728_native_part3_reusePcl_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Perform staging for non ebt order by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11728, testOutputData);
        harvesterSelectingAndStaging.performReStaging(ExcelUtils.BULK_STAGING_TC_11728);
    }

    @Test(groups = {"ete_pcl_tc_11728_native_part4_reusePcl_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify de-staging and checkout for non ebt order which is picked and staged by reusing the pcl labels from pickedUp order")
    public void validateE2eNonEbtReusePclOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11728, ExcelUtils.BULK_STAGING_TC_11728);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11728, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
