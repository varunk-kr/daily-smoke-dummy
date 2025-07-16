package com.krogerqa.mobile.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10442
 * Perform selecting  for pcl single-threaded store using Harvester Native >
 * Mark sell by weight items as NR during picking>
 * Pick sell by unit item with Qty 2 in packages of 1 >
 * Stage Pcl containers using Harvester Native >
 * From order adjustment - pick the sell by weight item with Qty 2> fulfill 1  and substitute other>
 * Stage the NR item container
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowWithSellByWeightSubstituteAndSellByUnitItemTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();

    @Test(groups = {"ete_pcl_tc_10442_native_part1_mafp_ciao", "ete_native_part1_ebt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for pcl order with mafp items")
    public void validateE2ENativeSellByWeightSubsSelectingMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10442_native_part2_mafp_ciao", "ete_native_part2_ebt", "ete_native_part2_multiThreaded"}, description = "Verify staging and order adjustment flow , partial substitution flow")
    public void validateE2ENativeSellByWeightSubsStagingMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_10442_native_part3_mafp_ciao", "ete_native_part3_ebt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for pcl order with mafp items")
    public void validateE2ENativeSellByWeightSubsCheckoutMafpCiaoChangesOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10442, testOutputData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
