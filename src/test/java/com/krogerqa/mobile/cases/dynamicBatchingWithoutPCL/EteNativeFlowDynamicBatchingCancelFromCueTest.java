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
 * FFILLSVCS-TC-5576 mobile flows
 * Perform Selecting for each trolley which is combined for both order
 * After cancelling the first order from cue, perform picking for all the trolleys for second order
 * Stage the containers for the second order
 * Perform ciao checkout and paid status
 */

public class EteNativeFlowDynamicBatchingCancelFromCueTest extends BaseTest {
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_tc_5576_native_part1_cancelFromCue", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non-EBT order in same store")
    public void validateE2eOrderDynamicBatchingSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, ExcelUtils.DYNAMIC_BATCH_TC_5576_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5576_native_part2_cancelFromCue", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify staging for non-EBT orders in same store")
    public void validateE2eOrderDynamicBatchingStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, ExcelUtils.DYNAMIC_BATCH_TC_5576_1);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_tc_5576_native_part3_cancelFromCue", "ete_native_part4_nonEbt", "ete_native_part4_multiThreaded"}, description = "Verify de-staging and checkout for non-EBT orders in same store")
    public void validateE2eOrderDynamicBatchingCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, ExcelUtils.DYNAMIC_BATCH_TC_5576_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.DYNAMIC_BATCH_TC_5576_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}


