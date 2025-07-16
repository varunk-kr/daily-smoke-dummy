package com.krogerqa.mobile.cases.tc52NotificationsWithPCL;

import com.krogerqa.mobile.apps.HarvesterSelectingAndStaging;
import com.krogerqa.mobile.apps.LoginNative;
import com.krogerqa.mobile.ui.pages.littleBirdApp.TC52NotificationValidationPage;
import com.krogerqa.mobile.ui.pages.login.WelcomeToChromePage;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.krogerqa.mobile.apps.PermanentContainerLabelHelper.mobileCommands;

/**
 * Scenario TC 52 Notification Non-Ebt Mobile Flows - 7044
 * Validate TC 52 Notifications in Little Bird App>
 * Validate Config app>
 * Perform Selecting and mark all items Out of stock for Multi-threaded Pcl store using Harvester Native >
 */
public class EteNativeFlowNonEbtPclAllItemOutOfStockTc52Test extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    TC52NotificationValidationPage tc52NotificationValidationPage = TC52NotificationValidationPage.getInstance();
    LoginNative loginNative = LoginNative.getInstance();
    WelcomeToChromePage welcomeToChromePage = WelcomeToChromePage.getInstance();

    @Test(groups = {"ete_tc52_native_part1_nonEbt", "ete_native_part2_nonEbt_tc52", "ete_native_part2_multiThreaded_tC52"}, description = "Verify staging for non ebt TC 52 order")
    public void validateE2eNonEbtTc52LittleBirdApp() {
        tc52NotificationValidationPage.installLittleBirdNotificationApplication();
        tc52NotificationValidationPage.allowUsageAccessForNotificationsApp();
        loginUsingOktaSignInLittleBird(PropertyUtils.getHarvesterNativeUsername1(), PropertyUtils.getHarvesterNativePassword1());
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.TC_52_Notifications_7044, ExcelUtils.TC_52_Notifications_7044);
        mobileCommands.wait(20);
        mobileCommands.scrollDown();
        tc52NotificationValidationPage.validateTC52Notification(testOutputData);
    }

    @Test(groups = {"ete_tc52_native_part2_nonEbt", "ete_native_part3_nonEbt_tc52", "ete_native_part3_multiThreaded_tC52"}, description = "Verify selecting for non ebt TC 52 order")
    public void validateE2eNonEbtPclTc52Selecting() {
        loginUsingOktaSignIn(PropertyUtils.getHarvesterNativeUsername(), PropertyUtils.getHarvesterNativePassword());
        mobileCommands.wait(10);
        testOutputData = harvesterSelectingAndStaging.verifyHarvesterSelectingPcl(ExcelUtils.TC_52_Notifications_7044, testOutputData);
    }

    private void loginUsingOktaSignIn(String userName, String password) {
        welcomeToChromePage.acceptChromeTerms();
        loginNative.LoginOktaSignIn(userName, password);
    }

    private void loginUsingOktaSignInLittleBird(String userName, String password) {
        loginNative.LoginOktaSignIn(userName, password);
    }
}