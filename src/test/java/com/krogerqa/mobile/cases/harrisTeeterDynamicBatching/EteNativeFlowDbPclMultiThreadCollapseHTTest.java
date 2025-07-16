package com.krogerqa.mobile.cases.harrisTeeterDynamicBatching;

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
 * Scenario FFILLSVCS-TC-14379 Mobile Flows -MOVE AMBIENT TO AMBIENT
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowDbPclMultiThreadCollapseHTTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non-EBT order with multiple container having same temperature type for anchor staging")
    public void validateE2eHTDybMultiOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, firstOrderTestData);
    }

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non-EBT order with multiple container having same temperature type for anchor staging")
    public void validateE2eHTDybMultiOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, firstOrderTestData);
    }

    @Test(groups = {"ete_harriesTeeter_db_pcl_tc_14379_native_part3_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non-EBT order with multiple container having same temperature type for anchor staging")
    public void validateE2eHTDybMultiOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, ExcelUtils.HT_DB_MULTITHREAD_TC_14379);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HT_DB_MULTITHREAD_TC_14379, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.HT_DB_MULTITHREAD_TC_14379_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}

