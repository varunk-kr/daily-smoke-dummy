package com.krogerqa.mobile.cases.anchorStagingWithoutPCL;

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
 * Scenario TC_5323 Non-Ebt Mobile Flows -
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Do bulk staging for Pcl containers using Harvester Native >
 * After Customer Check-in, Destage containers and complete bulk destaigng checkout for non EBT Order using Ciao Native for Pcl Order
 */
public class EteNativeFlowAnchorStagingShiptDeliveryTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify selecting for shipt delivery pcl bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323,ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify staging for shipt delivery pcl bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323,ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        loginNativeApp(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_5323_anchorStaging_shipt_delivery_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify de-staging and checkout for shipt delivery pcl bulk staging order")
    public void validateE2eNonEbtPclBulkStagingShiptDeliveryOrderCheckout() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.ANCHOR_SCENARIO_TC_5323,ExcelUtils.ANCHOR_SCENARIO_TC_5323);
        loginNativeApp(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.ANCHOR_SCENARIO_TC_5323, testOutputData);
    }

    private void loginNativeApp(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.loginNativeApp(userName, password);
    }
}
