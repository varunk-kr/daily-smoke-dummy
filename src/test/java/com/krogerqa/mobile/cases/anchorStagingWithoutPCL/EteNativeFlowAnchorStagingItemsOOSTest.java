package com.krogerqa.mobile.cases.anchorStagingWithoutPCL;

import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-5317 Mobile Flows
 * Perform Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers using anchor staging >
 * After Customer Check-in, Destage containers and complete Checkout for non-EBT Order using Ciao Native
 */

public class EteNativeFlowAnchorStagingItemsOOSTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_anchor_tc_5317_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for non ebt anchor staging order")
    public void validateE2eNativeSelectingForNonEbtOOSAnchorStagingOrder() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5317, ExcelUtils.ANCHOR_SCENARIO_TC_5317);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelecting(ExcelUtils.ANCHOR_SCENARIO_TC_5317, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
