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
 * FFILLSVCS-TC-5580 mobile flows
 * Perform Selecting for each trolley which is combined for both order
 * After cancelling the first order from cue, perform picking for all the trolleys for second order
 * Stage the containers for the second order
 * Perform ciao checkout and paid status
 */
public class EteNativeFlowMultipleOrderCancelTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_tc_5580_native_part1_multipleOrderCancel", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersDynamicBatchingSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5580_native_part2_multipleOrderCancel", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersDynamicBatchingStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5580_native_part3_multipleOrderCancel", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for multiple non-EBT orders in same store")
    public void validateE2eMultipleOrdersDynamicBatchingCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, ExcelUtils.DYNAMIC_BATCH_TC_5580_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5580_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
