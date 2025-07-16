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
 * FFILLSVCS-TC-10461
 * Perform selecting for sale by weight, sell by unit items in pcl Multi-threaded store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-snapEbt Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowWithMultipleWeightItemsSnapEbtOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeOrderAdjustmentPage harvesterNativeOrderAdjustmentPage = HarvesterNativeOrderAdjustmentPage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10461_native_part1_mafp_ciao", "ete_native_part1_snapEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for multiple items mafp non pcl pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, secondOrderTestData);
        secondOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10461_native_part2_mafp_ciao", "ete_native_part2_snapEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for multiple items mafp non pcl pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, firstOrderTestData);
        harvesterNativeOrderAdjustmentPage.verifyOrderAdjustmentFlow(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, secondOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStagingForNotReadyContainer(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, secondOrderTestData);

    }

    @Test(groups = {"ete_pcl_tc_10461_native_part3_mafp_ciao", "ete_native_part3_snapEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for multiple items mafp non pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsPickOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10461_1, secondOrderTestData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
