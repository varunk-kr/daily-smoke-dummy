package com.krogerqa.mobile.cases.enhancedDynamicBatching;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderAdjustmentPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-10940 Mobile Flows - Enhanced dynamic batching
 * Perform Selecting by marking service counter item as not ready and picking rest of the items As Ordered using Harvester Native >
 * Stage containers using Harvester Native -Anchor granular staging.>
 * Pick the not ready item in order adjustment flow using Harvester Native
 * After Customer Check-in, De-stage containers and complete Checkout for EBT Order using Ciao Native
 */
public class EteNativeFlowDbNonPCLEnhancedBatchNrAnchorGranularTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_native_part1_nonEbt", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eNativeSelectingForNotReadyEnhancedBatchingNrAnchorGranularOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_native_part2_nonEbt", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging and order adjustment for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eNativeStagingAndOrderAdjustmentForNotReadyEnhancedBatchingNrAnchorGranularOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        testOutputData.put(ExcelUtils.IS_NOT_READY_SCENARIO, String.valueOf(true));
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.EB_NON_PCL_TC_10940,testOutputData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10940_NR_anchorGranular_native_part3_nonEbt", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging for Not Ready Enhanced Batching db non-EBT non-pcl anchorGranular staging order")
    public void validateE2eNativeDeStagingForNotReadyEnhancedBatchingNrAnchorGranularOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.EB_NON_PCL_TC_10940, ExcelUtils.EB_NON_PCL_TC_10940);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_NON_PCL_TC_10940, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
