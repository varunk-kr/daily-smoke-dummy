package com.krogerqa.web.cases.byob;

import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

public class EteWebByobSubstitutionAndPartiallyFulfilledTest extends BaseTest {
    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_byob_pcl_tc_7454_byobAccept_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify order placed for pcl byob order")
    public void validateE2ePclAcceptByobOrderPlaced() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BYOB_PCL_TC_7454);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BYOB_PCL_TC_7454, testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_tc_7454_byobAccept_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify order picked for pcl byob order")
    public void validateE2ePclAcceptByobOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7454, ExcelUtils.BYOB_PCL_TC_7454);
        testOutputData.put(ExcelUtils.IS_BYOB_FLOW, String.valueOf(true));
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES_AFTER_PICKING, String.valueOf(true));
        testOutputData.put(ExcelUtils.ACCEPT_BAG_FEES, String.valueOf(true));
        krogerSeamLessPortalOrderCreation.verifyBagPreference(testOutputData, testOutputData.get(ExcelUtils.ORDER_NUMBER));
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BYOB_PCL_TC_7454, testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_tc_7454_byobAccept_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for  pcl byob order")
    public void validateE2ePclAcceptByobOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7454, ExcelUtils.BYOB_PCL_TC_7454);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_byob_pcl_tc_7454_byobAccept_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for pcl byob order (Picking and Staging using Harvester Native app)")
    public void validateE2ePclAcceptByobOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BYOB_PCL_TC_7454, ExcelUtils.BYOB_PCL_TC_7454);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BYOB_PCL_TC_7454);
    }
}
