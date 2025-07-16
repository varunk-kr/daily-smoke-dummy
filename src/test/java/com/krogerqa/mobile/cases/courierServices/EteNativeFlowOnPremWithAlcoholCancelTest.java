package com.krogerqa.mobile.cases.courierServices;

import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * FFILLSVCS-TC-9601 Mobile Flows -
 * Perform Shipt Delivery Selecting by picking items As Ordered for Multi-threaded store using Harvester Native >
 * Stage containers using Harvester Native >
 * After Customer Check-in, De-stage containers and complete Checkout for non-EBT Order using Ciao Native
 */
public class EteNativeFlowOnPremWithAlcoholCancelTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();

    @Test(groups = {"ete_native_part1_nonEbt_shiptDeliveryOnPrem_9601", "ete_native_part1_nonEbt", "ete_native_part1_multiThreaded"}, description = "Verify selecting for Shipt Delivery order")
    public void validateE2eDeliveryOnPremOrderSelecting() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9601,ExcelUtils.COURIER_SERVICES_9601);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(),PropertyUtils.getHarvesterNativePassword());
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.COURIER_SERVICES_9601, testOutputData);
    }

    @Test(groups = {"ete_native_part2_nonEbt_shiptDeliveryOnPrem_9601", "ete_native_part2_nonEbt", "ete_native_part2_multiThreaded"}, description = "Verify staging for Shipt Delivery order")
    public void validateE2eDeliveryOnPremOrderStaging() {
        testOutputData=ExcelUtils.getTestDataMap(ExcelUtils.COURIER_SERVICES_9601,ExcelUtils.COURIER_SERVICES_9601);
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        harvesterSelectingAndStaging.verifyHarvesterStagingPcl(ExcelUtils.COURIER_SERVICES_9601, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }
}
