package com.krogerqa.mobile.cases.dynamicBatchingWeightedTrolley;

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
 * Scenario FFILLSVCS-TC-13139 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowMultipleOrdersWeightedTrolleyPclTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_13139_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order")
    public void validateE2eNonEbtPclOrderSelecting() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_WEIGHTED_TC_13139, firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_13139_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt order")
    public void validateE2eNonEbtPclOrderStaging() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_WEIGHTED_TC_13139, firstOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_13139_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eNonEbtPclOrderCheckout() {
        firstOrderTestData=ExcelUtils.getTestDataMap(ExcelUtils.DB_WEIGHTED_TC_13139, ExcelUtils.DB_WEIGHTED_TC_13139);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_WEIGHTED_TC_13139, firstOrderTestData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
