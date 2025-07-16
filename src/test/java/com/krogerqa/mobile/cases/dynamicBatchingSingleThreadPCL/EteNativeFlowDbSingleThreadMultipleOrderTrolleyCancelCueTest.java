package com.krogerqa.mobile.cases.dynamicBatchingSingleThreadPCL;

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
 * Scenario DYNAMIC_BATCH_PCL_TC_12731 Mobile Flows -
 * Perform Selecting by picking items As Ordered for not cancelled order in Single-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowDbSingleThreadMultipleOrderTrolleyCancelCueTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_tc_12731_singleThread_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eSelectingFirstTrolley() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12731_singleThread_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, firstOrderTestData);
    }

    @Test(groups = {"ete_db_tc_12731_singleThread_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for PCL Dynamic batching nonEBT singleThread Order")
    public void validateE2eOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_PCL_TC_12731, firstOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
