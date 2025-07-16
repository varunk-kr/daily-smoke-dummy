package com.krogerqa.mobile.cases.mafpCiaoChanges;

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
 * FFILLSVCS-TC-10459
 * Perform selecting for sale by weight, sel by unit, random weight , normal weight items in pcl Single-threaded store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeFlowWithMultipleWeightItemsNonEbtOrderTest extends BaseTest {

    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_10459_native_part1_mafp_ciao", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderSelecting() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        firstOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, secondOrderTestData);
        secondOrderTestData = harvesterSelectingAndStaging.verifyMultipleOrderHarvesterSelecting(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pcl_tc_10459_native_part2_mafp_ciao", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsOrderStaging() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        loginUsingOktaSign(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, firstOrderTestData);
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, secondOrderTestData);

    }

    @Test(groups = {"ete_pcl_tc_10459_native_part3_mafp_ciao", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for multiple items mafp pcl order")
    public void validateE2eNonPclMafpCiaoChangesMultipleWeightItemsPickOrderCheckout() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1);
        loginUsingOktaSign(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459, firstOrderTestData);
        ciaoDestagingAndCheckout.setUpStoreForMultipleOrders(firstOrderTestData);
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.MAFP_CIAO_SCENARIO_TC_10459_1, secondOrderTestData);
    }

    private void loginUsingOktaSign(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
