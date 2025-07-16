package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeOrderAdjustmentMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.MobileUtils;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;

public class HarvesterNativeOrderAdjustmentPage {
    private static final String ORDER_ADJUSTMENT_COMPLETION_TEXT = "This order's service counter items \nare good to go. Thanks for helping \nget this order to the finish line.";
    private static final String STAGED_LABEL_MESSAGE = "Staged Label status is incorrect in Order Adjustment Screen.";
    private static HarvesterNativeOrderAdjustmentPage instance;
    String upcIdMafp;
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeOrderAdjustmentMap harvesterNativeOrderAdjustmentMap = HarvesterNativeOrderAdjustmentMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();
    HarvesterNativeSelectingPage harvesterNativeSelectingPage = HarvesterNativeSelectingPage.getInstance();
    HarvesterNativePage harvesterNativePage = HarvesterNativePage.getInstance();
    String upcIdForOrderAdjustment;

    private HarvesterNativeOrderAdjustmentPage() {
    }

    public synchronized static HarvesterNativeOrderAdjustmentPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeOrderAdjustmentPage.class) {
            if (instance == null) {
                instance = new HarvesterNativeOrderAdjustmentPage();
            }
        }
        return instance;
    }

    public void enterBarcode(String barcode) {
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.enterBarcodeLink());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.enterBarcodeLink());
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.barcodeInput());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.barcodeInput());
        mobileCommands.enterText(harvesterNativeOrderAdjustmentMap.barcodeInput(), barcode, true);
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.continueButton());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.continueButton());
    }

    public String generateServiceCounterItemUpc() {
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.itemUpcText());
        String upc = mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.itemUpcText());
        upcIdForOrderAdjustment = mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.itemUpcText());
        upcIdMafp = mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.itemUpcText());
        return upc.replace("0000", "0001");
    }

    public void clickDoneStagingButton() {
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.doneStagingButton());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.doneStagingButton());
    }

    public void assertOrderAdjustmentStagingCompleteText(String notReadyContainer) {
        try {
            mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.orderAdjustmentStagingCompleteText());
        } catch (Exception | AssertionError e) {
            enterBarcode(notReadyContainer);
            mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.orderAdjustmentStagingCompleteText());
            mobileCommands.assertElementText(harvesterNativeOrderAdjustmentMap.orderAdjustmentStagingCompleteText(), ORDER_ADJUSTMENT_COMPLETION_TEXT, true);
        }
    }

    public void pickAllNrItems(HashMap<String, String> testOutputData) {
        try {
            String notReadyContainerMap = testOutputData.get(ExcelUtils.NOT_READY_CONTAINER);
            String notReadyContainer = (((notReadyContainerMap.split("="))[0]).split("\\{"))[1];
            while (mobileCommands.elementDisplayed(harvesterNativeOrderAdjustmentMap.itemUpcText())) {
                enterBarcodeForServiceCounterItem(generateServiceCounterItemUpc());
                enterNotReadyBarcode(notReadyContainer);
            }
            assertOrderAdjustmentStagingCompleteText(notReadyContainer);
            clickDoneStagingButton();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void verifyOrderAdjustmentFlow(String scenario, HashMap<String, String> testOutputData) {
        try {
            String orderId = testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            HashMap<String, String> upcLabel = harvesterNativeSelectingPage.getItemsWithLabelsFromTestData(itemData);
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            if (updatedTestOutputData.containsKey(ExcelUtils.NOT_READY_CONTAINER)) {
                String notReadyContainerMap = updatedTestOutputData.get(ExcelUtils.NOT_READY_CONTAINER);
                String notReadyContainer = (((notReadyContainerMap.split("="))[0]).split("\\{"))[1];
                mobileCommands.tap(harvesterNativeOrderAdjustmentMap.orderAdjustmentBtn());
                mobileCommands.scrollDownToFindVisibleText(orderId);
                mobileCommands.tap(harvesterNativeOrderAdjustmentMap.clickOnOrder(orderId));
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.SC_ITEMS_MT_SCALE))) {
                    String upcId = generateServiceCounterItemUpc();
                    validateStagingStatus(testData);
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
                        if (upcId.endsWith("0000")) {
                            upcId = upcId.substring(0, upcId.length() - 1) + "1";
                        }
                        upcIdMafp = mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.itemUpcText()).substring(5);
                        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.MAFP_NOT_READY_FOR_ORDER_ADJUSTMENT_WEIGHT))) {
                            if (upcLabel.get(ExcelUtils.SELL_BY_WEIGHT_ITEM).contains(upcIdMafp)) {
                                harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                                harvesterNativeSelectingPage.serviceCounterSellByWeightOrUnit(upcLabel, upcIdMafp);
                            }
                        }
                    } else {
                        enterBarcodeForServiceCounterItem(generateServiceCounterItemUpc());
                    }
                    if (testData.containsKey(ExcelUtils.PICK_MULTIPLE_QTY_SERVICE_COUNTER_ITEM)) {
                        int totalItems = Integer.parseInt((mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.itemsQtyText()).split(" "))[3]);
                        for (int i = 0; i < totalItems - 1; i++) {
                            mobileCommands.tap(harvesterNativeOrderAdjustmentMap.stepperCount(generateServiceCounterItemUpc()));
                        }
                        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.continueButton());
                    }
                    enterNotReadyBarcode(notReadyContainer);
                    assertOrderAdjustmentStagingCompleteText(notReadyContainer);
                    clickDoneStagingButton();
                }
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    private void enterBarcodeForServiceCounterItem(String barcode) {
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.kebabMenu());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.kebabMenu());
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.enterBarcodeLink());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.enterBarcodeLink());
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.barcodeInput());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.barcodeInput());
        mobileCommands.enterText(harvesterNativeOrderAdjustmentMap.barcodeInput(), barcode, true);
        mobileCommands.waitForElementVisibility(harvesterNativeOrderAdjustmentMap.continueButton());
        mobileCommands.tap(harvesterNativeOrderAdjustmentMap.continueButton());
    }

    private void validateStagingStatus(HashMap<String, String> testOutputData) {
        if (testOutputData.containsKey(testOutputData.get(ExcelUtils.STAGING_STATUS))) {
            String stagingStatus = testOutputData.get(ExcelUtils.STAGING_STATUS);
            Assert.assertTrue(mobileCommands.getElementText(harvesterNativeOrderAdjustmentMap.getStagedLabelStatus()).contains(stagingStatus), STAGED_LABEL_MESSAGE);
        }
    }

    public void enterNotReadyBarcode(String notReadyContainer) {
        try {
            enterBarcode(notReadyContainer);
        } catch (Exception | AssertionError e) {
            enterBarcode(notReadyContainer);
        }
    }
}
