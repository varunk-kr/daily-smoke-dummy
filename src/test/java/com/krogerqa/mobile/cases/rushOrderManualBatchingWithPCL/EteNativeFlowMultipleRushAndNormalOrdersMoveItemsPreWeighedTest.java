package com.krogerqa.mobile.cases.rushOrderManualBatchingWithPCL;

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
 * Scenario FFILLSVCS-TC-7142 mobile flows
 * Perform Selecting for multiple rush orders and normal orders.
 * Stage the containers for all the orders
 * Perform ciao checkout and validate paid status
 */
public class EteNativeFlowMultipleRushAndNormalOrdersMoveItemsPreWeighedTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    static HashMap<String, String> thirdOrderTestData = new HashMap<>();
    static HashMap<String, String> fourthOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();


    @Test(groups = {"ete_tc_7142_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT RUSH orders and Normal orders with Collapse temp as AM_RE")
    public void validateE2eSelectingMultipleRushAndNormalOrders() {
        getTestdata(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        thirdOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_tc_7142_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple non-EBT RUSH orders and Normal orders with Collapse temp as AM_RE")
    public void validateE2eMultipleRushAndNormalOrdersStaging() {
        getTestdata(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
    }

    @Test(groups = {"ete_tc_7142_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT RUSH orders and Normal orders with Collapse temp as AM_RE")
    public void validateE2eMultipleRushAndNormalOrdersCheckout() {
        getTestdata(ExcelUtils.ROMB_SCENARIO_TC_7142, ExcelUtils.ROMB_SCENARIO_TC_7142_1, ExcelUtils.ROMB_SCENARIO_TC_7142_2, ExcelUtils.ROMB_SCENARIO_TC_7142_3);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7142, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(secondOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7142_1, secondOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(thirdOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7142_2, thirdOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(fourthOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ROMB_SCENARIO_TC_7142_3, fourthOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }

    public void getTestdata(String sheetName1, String sheetName2, String sheetName3, String sheetName4) {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142, sheetName1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_1, sheetName2);
        thirdOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_2, sheetName3);
        fourthOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.ROMB_SCENARIO_TC_7142_3, sheetName4);
    }
}
