package com.krogerqa.mobile.cases.byob;

import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario FFILLSVCS-TC-7463 Mobile Flows -
 * Perform Selecting by picking items As Ordered for Bulk-Staging Pcl store using Harvester Native >
 * Move items from multiple picked Ambient containers to a new container from order lookup screen >
 * Stage Pcl containers using Bulk Staging >
 * After Customer Check-in, DeStage containers and complete Checkout for non-EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeByobAcceptBagFeeBeforePickingItemMovementToOSTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_byob_pcl_itemMovementToOS_tc_7463_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify picking for Byob item movement from Ambient to OS order")
    public void validateE2eNonEbtPclByobItemMovementAMToOSOrderPicking() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_BYOB_TC_7463, ExcelUtils.PCL_BYOB_TC_7463);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_BYOB_TC_7463, testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_itemMovementToOS_tc_7463_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Perform Item Movement from Ambient to Oversize Byob order")
    public void validateE2eNonEbtPclByobItemMovementAMToOSOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_BYOB_TC_7463, ExcelUtils.PCL_BYOB_TC_7463);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.CROSS_TEMP_ITEM_MOVEMENT, String.valueOf(true));
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.PCL_BYOB_TC_7463, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
