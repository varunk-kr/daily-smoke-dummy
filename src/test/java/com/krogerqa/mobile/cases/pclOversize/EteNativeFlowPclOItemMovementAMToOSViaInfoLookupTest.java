package com.krogerqa.mobile.cases.pclOversize;


import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_OVERSIZE_TC_12236 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Move SOME items from multiple refrigerated items to existing ambient container through Order Lookup screen >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, DeStage containers and complete bulk deStaging checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowPclOItemMovementAMToOSViaInfoLookupTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_12236_pclOversize_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for bulk staging item movement order")
    public void validateE2eNonEbtPclOversizeItemMovementAMToOSViaInfoLookupOrderSelecting() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12236, ExcelUtils.PCL_OVERSIZE_TC_12236);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.PCL_OVERSIZE_TC_12236, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_12236_pclOversize_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify item movement and staging for bulk staging item movement order")
    public void validateE2eNonEbtPclOversizeItemMovementAMToOSViaInfoLookupOrderStaging() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OVERSIZE_TC_12236, ExcelUtils.PCL_OVERSIZE_TC_12236);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData.put(ExcelUtils.CROSS_TEMP_ITEM_MOVEMENT, String.valueOf(true));
        harvesterSelectingAndStaging.verifyHarvesterStagingAfterItemMovementPcl(ExcelUtils.PCL_OVERSIZE_TC_12236, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
