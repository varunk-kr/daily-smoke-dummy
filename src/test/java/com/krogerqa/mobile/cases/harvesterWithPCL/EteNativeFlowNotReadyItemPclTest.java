package com.krogerqa.mobile.cases.harvesterWithPCL;

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
 * Scenario TC-4567 - NR Item Mobile Flows -
 * Perform Selecting by marking service counter item as not ready and picking rest of the items As Ordered using Harvester Native
 * Stage containers using Harvester Native
 * Pick the not ready item in order adjustment flow using Harvester Native
 * Stage the not ready container using Harvester Native
 * After Customer Check-in, De-stage containers and complete Checkout for NonEBT Order using Ciao Native
 */

public class EteNativeFlowNotReadyItemPclTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_pcl_tc_4567_native_part1_PclNotReadyItem_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Not Ready  order")
    public void validateE2eNativeSelectingForNotReadyEbtOrderPcl() {
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_SCENARIO_TC_4567, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4567_native_part2_PclNotReadyItem_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging and order adjustment for Not Ready  order")
    public void validateE2eNativeStagingAndOrderAdjustmentForNotReadyEbtOrderPcl() {
        testOutputData.put(ExcelUtils.IS_NOT_READY_SCENARIO, String.valueOf(true));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.PCL_SCENARIO_TC_4567, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.PCL_SCENARIO_TC_4567, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.PCL_SCENARIO_TC_4567, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_4567_native_part3_PclNotReadyItem_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging for not Ready  order")
    public void validateE2eNativeDeStagingForNotReadyEbtOrderPcl() {
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.PCL_SCENARIO_TC_4567, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
