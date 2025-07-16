package com.krogerqa.mobile.cases.enhancedDynamicBatching;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * DYNAMIC_BATCH_PCL_TC_10907 mobile flows
 * Perform selecting for both Non EBT Orders
 * Stage the containers for 1 order
 * Perform ciao checkout and paid status
 */
public class EteNativeFlowDbPclEnhancedBatchPartialBatchingTest extends BaseTest {

    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for second Non EBT Order batched with Enhanced Batching AM,RE and OS FR")
    public void validateE2EehnancedBatchPartialBatchingOrderSelecting() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderTestData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for second Non EBT Order batched with Enhanced Batching AM,RE and OS FR")
    public void validateE2EehnancedBatchPartialBatchingOrderStaging() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterNativeStorePage.setupLocation(secondOrderTestData.get(ExcelUtils.STORE_DIVISION_ID), secondOrderTestData.get(ExcelUtils.STORE_LOCATION_ID));
        PermanentContainerLabelHelper.performItemMovementViaOrderLookup(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
        harvesterSelectingAndStaging.changeStoreSetup();
        secondOrderTestData.put(ExcelUtils.ITEM_MOVEMENT, String.valueOf(false));
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);
    }

    @Test(groups = {"ete_enhancedBatching_partialBatching_pcl_tc_10907_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_multiThreaded"}, description = "Verify destaging for second Non EBT Order batched with Enhanced Batching AM,RE and OS FR ")
    public void validateE2EehnancedBatchPartialBatchingOrderCheckout() {
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.EB_PCL_TC_10907_1, ExcelUtils.EB_PCL_TC_10907_1);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.EB_PCL_TC_10907_1, secondOrderTestData);

    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
