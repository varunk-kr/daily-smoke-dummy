package com.krogerqa.mobile.cases.enhancedDynamicBatching;

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
 * Scenario FFFILLSVCS-TC-10878 Mobile Flows -
 * Perform Selecting by picking items As Ordered and move items to new container
 * while picking for modify order for Multi-threaded Pcl store using Harvester Native >
 * Stage Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */

public class EteNativeDbNonPclEnhancedBatchTwoRushOrdersPartialBatchAndCancelOneOrderTest extends BaseTest {

    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt order")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.collapseTrolleysSelecting( ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for non ebt order")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStaging(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, secondOrderTestData);
    }

    @Test(groups = {"ete_db_enhancedBatching_tc_10878_nonPcl_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify de-staging and checkout for non ebt order")
    public void validateE2eNonPclEnhanceDBPartialBatchAndCancelOneOrderOrderCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_RUSH_ORDER_TC_10878_1, secondOrderTestData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
