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
 * DYNAMIC_BATCH_PCL_TC_9516 mobile flows
 * Perform Selecting for one trolley which is combined for both rush and normal order
 * Stage the containers for both rush and normal orders
 * Perform ciao checkout and paid status for both orders
 */
public class EteNativeFlowDbPclMultipleRushAndNormalOrderCancelTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT rush and normal orders in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT pcl rush and normal orders in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, thirdOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_9516_multipleRushOrder_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT pcl rush and normal orders in same store")
    public void validateE2eDbPclMultipleRushAndNormalOrdersCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_PCL_TC_MULTIPLE_9516_1_1, thirdOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}