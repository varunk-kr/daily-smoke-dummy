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
 * Scenario FFILLSVCS-TC-12761 Mobile Flows -
 * Perform Selecting by picking items As Ordered for  Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for  Order using Ciao Native for  Pcl Order
 */

public class EteNativeFlowDbPclSingleThreadShiptAzureTest extends BaseTest {

    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_12761_delivery_azure_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for non ebt order")
    public void validateE2eDyBSingleThreadDeliveryAzureOrderSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12761_2, ExcelUtils.DB_SINGLE_THREAD_12761_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_SINGLE_THREAD_12761_2, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_12761_delivery_azure_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for non ebt order")
    public void validateE2eDyBSingleThreadDeliveryAzureOrderStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12761_2, ExcelUtils.DB_SINGLE_THREAD_12761_2);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_SINGLE_THREAD_12761_2, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_12761_delivery_azure_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eDyBSingleThreadDeliveryAzureOrderCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_SINGLE_THREAD_12761_2, ExcelUtils.DB_SINGLE_THREAD_12761_2);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_SINGLE_THREAD_12761_2, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
