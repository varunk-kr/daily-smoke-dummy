package com.krogerqa.web.cases.bulkStagingWithPCL;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.apps.CueOrderValidation;
import com.krogerqa.web.apps.KrogerSeamLessPortalOrderCreation;
import com.krogerqa.web.apps.LoginWeb;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class EteWebFlowBulkStagingSubstituteItemsWithAnchorZoneTest extends BaseTest {

    static HashMap<String, String> testOutputData = new HashMap<>();
    KrogerSeamLessPortalOrderCreation krogerSeamLessPortalOrderCreation = KrogerSeamLessPortalOrderCreation.getInstance();
    CueOrderValidation cueOrderValidation = CueOrderValidation.getInstance();
    LoginWeb loginWeb = LoginWeb.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_web_part1_nonEbt", "ete_web_part1_nonEbt", "ete_web_part1_SingleThreadedWithBulkStaging"}, description = "Verify pcl order placed for substitution of item in BulkStaging non-EBT pcl order")
    public void validateE2eNewNonEbtPclBulkStagingOrder() {
        if (!(testOutputData == null)) {
            testOutputData.clear();
        }
        testOutputData = krogerSeamLessPortalOrderCreation.verifyNewOrderCreation(ExcelUtils.BULK_STAGING_TC_11719);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        testOutputData.put(ExcelUtils.IS_BULK_STAGING, String.valueOf(true));
        testOutputData.put(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER, String.valueOf(true));
        testOutputData = cueOrderValidation.verifyOrderInCue(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_web_part2_nonEbt", "ete_web_part2_nonEbt", "ete_web_part1_SingleThreadedWithBulkStaging"}, description = "Verify order picked and substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingOrderPicked() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPclPickedStatusInCue(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_web_part3_nonEbt", "ete_web_part3_nonEbt", "ete_web_part1_SingleThreadedWithBulkStaging"}, description = "Verify order staged and checked-in for Existing container movement from substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingOrderStaged() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(ExcelUtils.BULK_STAGING_TC_11719);
        String rejectedContainer = pickingServicesHelper.validateTwoWayCommunication(testOutputData.get(ExcelUtils.ORDER_NUMBER), itemData, testOutputData);
        testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedContainer);
        ExcelUtils.writeToExcel(ExcelUtils.BULK_STAGING_TC_11719, testOutputData);
        cueOrderValidation.verifyStagedPclStatusInCue(testOutputData);
    }

    @Test(groups = {"ete_pcl_tc_11719_bulkStaging_anchor_web_part4_nonEbt", "ete_web_part4_nonEbt", "ete_web_part1_SingleThreadedWithBulkStaging"}, description = "Verify order picked up and paid for Existing container movement from substitution screen non-EBT pcl order")
    public void validateE2eNonEbtPclBulkStagingOrderPaid() {
        testOutputData = ExcelUtils.getTestDataMap(ExcelUtils.BULK_STAGING_TC_11719, ExcelUtils.BULK_STAGING_TC_11719);
        cueOrderValidation.verifyOrderDroppedFromDash(testOutputData);
        loginWeb.loginCue(testOutputData.get(ExcelUtils.STORE_ID), testOutputData.get(ExcelUtils.STORE_BANNER));
        cueOrderValidation.verifyPickedUpAndPaid(ExcelUtils.BULK_STAGING_TC_11719);
    }
}