package com.krogerqa.mobile.cases.bulkStagingWithPCL;

import com.krogerqa.mobile.apps.CiaoDeStagingAndCheckout;
import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario BulkStaging PCL FFILLSVCS-TC-11713 Non-Ebt Mobile Flows -
 * Login to Harvester app as “Associate A” and start the trolley123 selection process
 * Reuse the staged pcl and Exit the container Assignment screen
 * Login to Harvester app as “Associate B” and navigate to Trolley123 Selection screen
 * IN-Progress section and the Trolley from previous step is displayed as “Assigning” Status
 * Click on Kebab menu and Select “take over run”
 * Perform Selecting by picking items As Ordered for Single-threaded Pcl store using Harvester Native >
 * Bulk Stage Pcl containers using Harvester Native >
 * After Customer Check-in, Bulk DeStage containers and complete Checkout forEBT Order using Ciao Native for Pcl Order
 * Note:Execute 2 native testcases after running 2 web test cases
 */
public class EteNativeFlowBulkStagingTakeOverTrolleyInAssigningTest extends BaseTest {
    static HashMap<String, String> secondOrderData = new HashMap<>();
    static HashMap<String, String> firstOrderData = new HashMap<>();
    static HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    CiaoDeStagingAndCheckout ciaoDestagingAndCheckout = CiaoDeStagingAndCheckout.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_pcl_tc_11713_bulkStaging_wave_native_part1_nonEbt", "ete_native_part1_nonEbt", "ete_native_part1_singleThreaded"}, description = "Verify Reuse the staged pcl ")
    public void validateE2eNonEbtPclOrderAssigning() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713_1, ExcelUtils.BULK_STAGING_TC_11713_1);
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713, ExcelUtils.BULK_STAGING_TC_11713);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        secondOrderData = harvesterSelectingAndStaging.verifyHarvesterTakeOverAssigningPcl(ExcelUtils.BULK_STAGING_TC_11713, secondOrderData);
        harvesterSelectingAndStaging.assignNewPclLabel(ExcelUtils.BULK_STAGING_TC_11713, secondOrderData, firstOrderData);
    }

    @Test(groups = {"ete_pcl_tc_11713_bulkStaging_wave_native_part2_nonEbt", "ete_native_part2_nonEbt", "ete_native_part2_singleThreaded"}, description = "Verify the trolley in Assigning status and takeover the trolley complete the picking")
    public void validateE2eTakeOverTrolleyPclOrderAssigning() {
        firstOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713_1, ExcelUtils.BULK_STAGING_TC_11713_1);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713, ExcelUtils.BULK_STAGING_TC_11713);
        harvesterSelectingAndStaging.takeOverRunAndSelecting(ExcelUtils.BULK_STAGING_TC_11713, secondOrderData, Constants.PickCreation.ASSIGNING);
    }

    @Test(groups = {"ete_pcl_tc_11713_bulkStaging_wave_native_part3_nonEbt", "ete_native_part3_nonEbt", "ete_native_part3_singleThreaded"}, description = "Verify staging for take over run trolley")
    public void validateE2eTakeOverTrolleyPclOrderStaging() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713, ExcelUtils.BULK_STAGING_TC_11713);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.BULK_STAGING_TC_11713, secondOrderData);
    }

    @Test(groups = {"ete_pcl_tc_11713_bulkStaging_wave_native_part4_nonEbt", "ete_native_part4_nonEbt", "ete_native_part4_singleThreaded"}, description = "Verify de-staging and checkout for take over run trolley")
    public void validateE2eTakeOverTrolleyPclOrderCheckout() {
        secondOrderData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11713, ExcelUtils.BULK_STAGING_TC_11713);
        loginUsingOktaSignIn(PropertyUtils.getCiaoUsername(), PropertyUtils.getCiaoPassword());
        ciaoDestagingAndCheckout.verifyOrderCheckoutInCiao(ExcelUtils.BULK_STAGING_TC_11713, secondOrderData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
