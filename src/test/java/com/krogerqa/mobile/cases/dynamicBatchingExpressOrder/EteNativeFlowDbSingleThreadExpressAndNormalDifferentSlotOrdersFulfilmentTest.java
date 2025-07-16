package com.krogerqa.mobile.cases.dynamicBatchingExpressOrder;

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
 * Scenario DB_EXPRESS_ORDER_TC_14054 Mobile Flows -
 * Perform Selecting for both express and normal orders by picking items As Ordered for Single-thread Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native for both the orders >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native for both the orders
 */
public class EteNativeFlowDbSingleThreadExpressAndNormalDifferentSlotOrdersFulfilmentTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_pcl_tc_14054_singleThread_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Request trolleys from harvester for express and normal orders")
    public void validateE2eDbPclOrderRequestTrolleyFromHarvester() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        firstOrderTestData.put(ExcelUtils.REQUEST_TROLLEY_FROM_HARVESTER, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.REQUEST_TROLLEY_FROM_HARVESTER, String.valueOf(true));
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.setUpStoreForTrolleyRequest(firstOrderTestData);
        harvesterSelectingAndStaging.batchContainersFromHarvester(firstOrderTestData);
        harvesterSelectingAndStaging.batchContainersFromHarvester(secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_14054_singleThread_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Perform selecting for express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_14054_singleThread_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Perform staging for express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_pcl_tc_14054_singleThread_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Perform de-staging and checkout for db non-EBT pcl express and normal orders")
    public void validateE2eDbPclExpressAndNormalOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, ExcelUtils.DB_EXPRESS_ORDER_TC_14054);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14054, firstOrderTestData);
        ciaoDestagingAndCheckout.changeStoreSetup();
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DB_EXPRESS_ORDER_TC_14054_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
