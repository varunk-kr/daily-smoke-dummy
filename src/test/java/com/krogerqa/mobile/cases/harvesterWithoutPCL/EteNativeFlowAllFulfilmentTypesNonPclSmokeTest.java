package com.krogerqa.mobile.cases.harvesterWithoutPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

public class EteNativeFlowAllFulfilmentTypesNonPclSmokeTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for various for all fulfillment type smoke test")
    public void validateNonPclFulfillmentTypesSmokeTestOrderSelecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for various for all fulfillment type smoke test")
    public void validateNonPclFulfillmentTypesSmokeTestOrdersStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
        testOutputData.put(ExcelUtils.STAGING_STATUS, Constants.PickCreation.NOT_STAGED);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    @Test(groups = {"ete_nonPcl_tc_fulfillment_smokeTest_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for various for all fulfillment type smoke test")
    public void validateNonPclFulfillmentTypesSmokedTestOrderCheckout() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyRejectedItemInCiao(ExcelUtils.NON_PCL_FULFILLMENT_TYPES_SMOKE, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
