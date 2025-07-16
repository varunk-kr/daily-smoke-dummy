package com.krogerqa.web.cases.pclOversize;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Scenario PCL_OS_PROD_DEFECT_MOVE_ITEMS Web Flows -
 * Submit Non-EBT Pcl Pickup order in Kroger.com for Multi-threaded store and perform Batching >
 * Verify New Non-EBT Order Details in Cue and assign pcl labels and pick only one trolley>
 * Complete selecting for first trolley and stage the items. Post that pick the 2nd trolley using the PCL label used and stage the 2nd container >
 * After Staging, validate COR event, and Staged Order Status and perform Customer Check-in in Cue >
 * Verify order is displayed in Dash >
 * After checkout, verify order is dropped from Dash >
 * Verify order is Picked Up and Paid in Cue and validate KLog
 */
public class EteWebFlowPclOSProdMultipleOrdersTest extends BaseTest {
    static HashMap<String, String> firstOrderTestData = new HashMap<>();
    static HashMap<String, String> secondOrderTestData = new HashMap<>();
    BaseCommands baseCommands = new BaseCommands();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_multiThreaded"}, description = "Verify new order placed for pcl oversize multiOrder")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrder() {
        if (!(firstOrderTestData == null)) {
            firstOrderTestData.clear();
        }
        if (!(secondOrderTestData == null)) {
            secondOrderTestData.clear();
        }
        getTestDataSheet(ExcelUtils.SHEET_NAME_TEST_DATA, ExcelUtils.SHEET_NAME_TEST_DATA);
        firstOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_PCL_OS, String.valueOf(true));
        secondOrderTestData.put(ExcelUtils.IS_MULTIPLE_ORDER_PCL_OS, String.valueOf(true));
        firstOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, firstOrderTestData);
        secondOrderTestData = krogerSeamLessPortalOrderCreation.createCompositeCheckoutPickOrder(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, secondOrderTestData);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, firstOrderTestData);
        browserRefresh();
        secondOrderTestData = cueOrderValidation.verifyOrderInCue(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify first trolley order picked status for pcl oversize multiOrder")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderPicking() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        firstOrderTestData = cueOrderValidation.verifyPickedStatusInCue(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, firstOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_web_part3_nonEbt", "ete_web_part2_nonEbt", "ete_web_part2_multiThreaded"}, description = "Verify first trolley staged status for pcl oversize multiOrder")
    public void validateE2eNewNonEbtPCLOSProdDefectFirstOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(firstOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_web_part4_nonEbt", "ete_web_part3_nonEbt", "ete_web_part3_multiThreaded"}, description = "Verify order staged and checked-in for pcl oversize multiOrder")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiSecondOrderStaged() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), secondOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyStagedPclStatusInCue(secondOrderTestData);
    }

    @Test(groups = {"ete_pclOversize_prod_defect_multiOrder_web_part5_nonEbt", "ete_web_part4_nonEbt", "ete_web_part4_multiThreaded"}, description = "Verify order picked up and paid for pcl oversize multiOrder")
    public void validateE2eNewNonEbtPCLOSProdDefectMultiOrderPaid() {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
        loginWeb.loginCue(firstOrderTestData.get(ExcelUtils.STORE_ID), firstOrderTestData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1);
    }

    public void getTestDataSheet(String sheetName1, String sheetName2) {
        firstOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDERS, sheetName1);
        secondOrderTestData = ExcelUtils.getTestDataMap(ExcelUtils.PCL_OS_PROD_DEFECT_MULTI_ORDER_1, sheetName2);
    }

    public void browserRefresh() {
        baseCommands.browserBack();
        baseCommands.webpageRefresh();
    }

}


