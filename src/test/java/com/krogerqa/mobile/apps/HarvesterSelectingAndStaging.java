package com.krogerqa.mobile.apps;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeOrderLookUpMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeSelectingMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeStagingMap;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativePage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeSelectingPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStagingPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStorePage;
import com.krogerqa.mobile.ui.pages.toggle.NativeTogglePage;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.MobileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvesterSelectingAndStaging {

    static String TAKE_OVER_TROLLEY = "Take over trolley";
    static String SELECTING = "Selecting";
    private static HarvesterSelectingAndStaging harvesterSelectingAndStaging;
    HarvesterNativeStorePage harvesterNativeStorePage = HarvesterNativeStorePage.getInstance();
    HarvesterNativeSelectingPage harvesterNativeSelectingPage = HarvesterNativeSelectingPage.getInstance();
    HarvesterNativeStagingPage harvesterNativeStagingPage = HarvesterNativeStagingPage.getInstance();
    HarvesterNativePage harvesterNativePage = HarvesterNativePage.getInstance();
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeSelectingMap harvesterNativeSelectingMap = HarvesterNativeSelectingMap.getInstance();
    HarvesterNativeOrderLookUpMap harvesterNativeOrderLookUpMap = HarvesterNativeOrderLookUpMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();
    HarvesterNativeStagingMap harvesterNativeStagingMap = HarvesterNativeStagingMap.getInstance();
    NativeTogglePage nativeTogglePage = NativeTogglePage.getInstance();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    private HarvesterSelectingAndStaging() {
    }

    public synchronized static HarvesterSelectingAndStaging getInstance() {
        if (harvesterSelectingAndStaging != null) {
            return harvesterSelectingAndStaging;
        }
        synchronized (HarvesterSelectingAndStaging.class) {
            if (harvesterSelectingAndStaging == null) {
                harvesterSelectingAndStaging = new HarvesterSelectingAndStaging();
            }
        }
        return harvesterSelectingAndStaging;
    }

    public HashMap<String, String> verifyHarvesterSelecting(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_SINGLE_TROLLEY_PICKING))) {
                harvesterNativeSelectingPage.verifyHarvesterSelectingForSingleContainer(testOutputData, itemData);
                ExcelUtils.writeToExcel(scenario, testOutputData);
            } else {
                harvesterNativeSelectingPage.verifyHarvesterSelecting(testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
            }
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> collapseAllTrolleySelecting(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            List<String> containerIdList = ExcelUtils.convertStringToList(testData.get(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS));
            mobileCommands.tap(harvesterNativeSelectingMap.selecting());
            harvesterNativeSelectingPage.collapseTemperatureTrolleysSelecting(containerIdList);
            return testData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyHarvesterSelectingPcl(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
                harvesterNativeSelectingPage.takeOverTrolley(scenario, testOutputData);
            } else {
                harvesterNativeSelectingPage.verifyHarvesterSelectingPcl(scenario, testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER))) {
                harvesterNativePage.backToSelectingScreen();
            }
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> takeOverRun(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
            mobileCommands.waitForElementClickability(harvesterNativeSelectingMap.selecting());
            mobileCommands.tap(harvesterNativeSelectingMap.selecting());
            mobileCommands.wait(2);
            harvesterNativePage.clickInProgressTrolleysButton();
            String takeOverTrolley = testOutputData.get(ExcelUtils.TAKE_OVER_TROLLEY);
            String takeOverContainerNumber = testOutputData.get(ExcelUtils.TAKE_OVER_CONTAINER_NO);
            harvesterNativeSelectingPage.moveToInProgressTrolley(takeOverTrolley);
            String status = harvesterNativePage.getProgressTagText(takeOverTrolley);
            verifySelectingInProgress(takeOverTrolley);
            harvesterNativePage.clickInProgressTrolleyMenuButton(takeOverTrolley);
            Assert.assertEquals(harvesterNativePage.getTakeOverRunLabelText(), TAKE_OVER_TROLLEY);
            harvesterNativePage.clickTakeOverRunButton();
            harvesterNativePage.enterBarcodeInProgressTrolley(takeOverContainerNumber);
            HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
            HashMap<String, String> upcLabel = harvesterNativeSelectingPage.getItemsWithLabelsFromTestData(itemData);
            for (Map.Entry<String, String> pclLabelMap : pclTrolleyMap.entrySet()) {
                if (pclLabelMap.getValue().equals(takeOverTrolley)) {
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.completeRunButton())) {
                        HarvesterNativeSelectingPage.scannedTrolleys.add(pclLabelMap.getValue());
                        harvesterNativeSelectingPage.completePickRun();
                        harvesterNativePage.backToSelectingScreen();
                        harvesterNativeSelectingPage.verifyHarvesterSelectingPcl(scenario, testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
                    } else if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                        List<HashMap<String, String>> itemsList = ExcelUtils.getItemUpcData(scenario);
                        harvesterNativeSelectingPage.singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                        if (status.equalsIgnoreCase(SELECTING))
                            break;
                        else
                            continue;
                    } else {
                        testOutputData = harvesterNativeSelectingPage.multiThreadedPclTrolleySelecting(scenario, testOutputData, testOutputData.get(ExcelUtils.ITEM_WEIGHT), itemData, upcLabel, pclLabelMap.getValue());
                    }
                    break;
                }
            }
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.newSectionTrolleysText());
            mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.backButton());
            mobileCommands.tap(harvesterNativeStagingMap.backButton());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
            pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
            HashMap<String, String> pclUnpickedContainerMap = PickingServicesHelper.getInstance().getContainersNotPicked(testOutputData.get(ExcelUtils.ORDER_NUMBER), pclTrolleyMap);
            HashMap<String, String> carryOverPclUnpickedContainerMap = new HashMap<>(pclUnpickedContainerMap);
            if (!pclUnpickedContainerMap.isEmpty()) {
                testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pclUnpickedContainerMap));
                if (pclUnpickedContainerMap.containsValue(takeOverTrolley)) {
                    for (Map.Entry<String, String> pcl : pclUnpickedContainerMap.entrySet()) {
                        if (pcl.getValue().equals(takeOverTrolley)) {
                            carryOverPclUnpickedContainerMap.remove(pcl.getKey());
                            break;
                        }
                    }
                    testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(carryOverPclUnpickedContainerMap));
                } else {
                    testOutputData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(pclUnpickedContainerMap));
                }
                ExcelUtils.writeToExcel(scenario, testOutputData);
                harvesterNativeSelectingPage.verifyHarvesterSelectingPcl(scenario, testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
            }
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public void verifyHarvesterStaging(String scenario, HashMap<String, String> testOutputData) {
        try {
            if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
                nativeTogglePage.handleToggle(testOutputData);
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NON_PCL_IS_OOS_SHORT)) || scenario.equals(ExcelUtils.SCENARIO_10)) {
                HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
                HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
                harvesterNativeStagingPage.verifyHarvesterStaging(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.CONTAINER_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
            } else if ((Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_SINGLE_TROLLEY_PICKING)))) {
                HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
                HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
                harvesterNativeStagingPage.verifyHarvesterStaging(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PICKED_CONTAINER_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
                HashMap<String, String> pickedContainerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PICKED_CONTAINER_MAP));
                pickedContainerMap.clear();
                testData.put(ExcelUtils.PICKED_CONTAINER_MAP, String.valueOf(pickedContainerMap));
                ExcelUtils.writeToExcel(scenario, testOutputData);
            } else {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                    harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)), testOutputData);
                } else {
                    harvesterNativeStagingPage.verifyHarvesterStaging(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
                }
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void changeStoreSetup() {
        try {
            PermanentContainerLabelHelper.navigateToHomeScreen();
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.hamburgerMenuIcon());
            mobileCommands.tap(harvesterNativeSelectingMap.hamburgerMenuIcon());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selectStore());
            mobileCommands.tap(harvesterNativeSelectingMap.selectStore());
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void verifyHarvesterStagingPcl(String scenario, HashMap<String, String> testOutputData) {
        try {
            PermanentContainerLabelHelper.itemMovementTo = "";
            if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER) || testOutputData.containsKey(ExcelUtils.CANCEL_TROLLEY_PCL_LABEL_TEMPERATURE_MAP) || Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_ORDER_STAGING))) {
                harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
                nativeTogglePage.handleToggle(testOutputData);
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT))) {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.STAGING)) {
                    PermanentContainerLabelHelper.performItemMovementViaStaging(scenario, testOutputData);
                }
            }
            if (!PermanentContainerLabelHelper.itemMovementTo.equalsIgnoreCase(ExcelUtils.OVERSIZE) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.MOVEMENT_TO_OOS)))) {
                HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
                HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
                if (PermanentContainerLabelHelper.fromContainerStatus == null)
                    PermanentContainerLabelHelper.fromContainerStatus = "";
                if (!PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT))) {
                        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.ORDER_LOOKUP)) {
                            updatedTestOutputData = PermanentContainerLabelHelper.performItemMovementViaOrderLookup(scenario, testOutputData);
                        }
                    }
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.INFORMATION_LOOKUP)) {
                            updatedTestOutputData = PermanentContainerLabelHelper.itemMovementFromInformationLookup(scenario, testOutputData);
                        }
                    }
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                        harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.INSTACART_CONTAINER_MAP)), updatedTestOutputData);
                    } else {
                        harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), updatedTestOutputData);
                    }
                }
                if (PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED))
                    harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData);
            }
            if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.STAGING)) {
                    PermanentContainerLabelHelper.performItemMovementAfterStaging(scenario, testOutputData);
                }
            }
            if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.ORDER_LOOKUP)) {
                    PermanentContainerLabelHelper.performItemMovementViaOrderLookup(scenario, testOutputData);
                }
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.INFORMATION_LOOKUP)) {
                    PermanentContainerLabelHelper.itemMovementFromInformationLookup(scenario, testOutputData);
                }
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DB_OS_REUSE_PCL_SPLIT_TROLLEY))) {
                testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(HarvesterNativeSelectingPage.pclLabelTempMapForDBOsSplitTrolley));
                ExcelUtils.writeToExcel(scenario, testOutputData);
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void performReStaging(String scenario) {
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_STAGE_FROM_LOOKUP))) {
            mobileCommands.tap(harvesterNativeStagingMap.selectInformationLookup());
        }
        HashMap<String, String> pclLabelTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> stagingZoneMaps = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.STAGING_ZONES));
        String reStagingZone = "";
        String reStagingContainerTemp = "";
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT))) {
            for (Map.Entry<String, String> stagingZone : stagingZoneMaps.entrySet()) {
                if (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT)) {
                    if (!stagingZone.getKey().equals(PermanentContainerLabelHelper.itemMovementTo)) {
                        reStagingZone = stagingZone.getValue();
                        reStagingContainerTemp = stagingZone.getKey();
                        break;
                    }
                } else {
                    if (stagingZone.getKey().equals(PermanentContainerLabelHelper.itemMovementTo)) {
                        reStagingZone = stagingZone.getValue();
                        break;
                    }
                }
            }
            for (Map.Entry<String, String> entry : pclLabelTemperatureMap.entrySet()) {
                if (!entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    if (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT)) {
                        if (entry.getValue().equalsIgnoreCase(reStagingContainerTemp)) {
                            harvesterNativePage.enterBarcode(entry.getKey(), Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                            harvesterNativePage.enterBarcode(reStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                            overRideStagingZone();
                            break;
                        }
                    } else {
                        if (entry.getValue().equalsIgnoreCase(PermanentContainerLabelHelper.itemMovementTo)) {
                            harvesterNativePage.enterBarcode(entry.getKey(), Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                            harvesterNativePage.enterBarcode(reStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                            overRideStagingZone();
                            break;
                        }
                    }
                }
            }
        } else {
            for (Map.Entry<String, String> entry : pclLabelTemperatureMap.entrySet()) {
                if (!entry.getValue().equalsIgnoreCase(Constants.PickCreation.AMBIENT)) {
                    for (Map.Entry<String, String> stagingZone : stagingZoneMaps.entrySet()) {
                        if (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT)) {
                            if (entry.getValue().equalsIgnoreCase(reStagingContainerTemp)) {
                                harvesterNativePage.enterBarcode(entry.getKey(), Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                                harvesterNativePage.enterBarcode(reStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                                overRideStagingZone();
                                break;
                            }
                        } else {
                            if (stagingZone.getKey().equals(entry.getValue())) {
                                reStagingZone = stagingZone.getValue();
                                harvesterNativePage.enterBarcode(entry.getKey(), Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                                harvesterNativePage.enterBarcode(reStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                                overRideStagingZone();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void validateInfoLookupScreenDetails(String scenario) {
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.selectStaging());
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_STAGE_FROM_LOOKUP))) {
            mobileCommands.tap(harvesterNativeStagingMap.selectInformationLookup());
        }
        HashMap<String, String> pclLabelTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> originalStagingZones = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.UPDATED_STAGING_ZONE_MAP));
        for (Map.Entry<String, String> entry : originalStagingZones.entrySet()) {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.InfoLookUpEnterBarcodeLink());
            mobileCommands.enterText(harvesterNativeOrderLookUpMap.InfoLookUpEnterBarcodeLink(), entry.getKey(), true);
            mobileCommands.tap(harvesterNativeOrderLookUpMap.continueButton());
            Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeStagingMap.LocationDetails()));
            if (!entry.getValue().equals(testOutputData.get(ExcelUtils.OS_STAGING_ZONE))) {
                if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.viewButton())) {
                    mobileCommands.tap(harvesterNativeStagingMap.viewButton());
                }
                mobileCommands.tap(harvesterNativeStagingMap.expandAllButton());
                for (Map.Entry<String, String> pclContainers : pclLabelTemperatureMap.entrySet()) {
                    mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.textIdentifier(pclContainers.getKey().substring(6, 12)));
                    Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeStagingMap.textIdentifier(pclContainers.getKey().substring(6, 12))));
                }
                for (Map.Entry<String, String> pclTempType : pclLabelTemperatureMap.entrySet()) {
                    Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeStagingMap.textIdentifier(pclTempType.getValue().substring(0, 2))));
                }
                int i = 0;
                while (!mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.InfoLookUpEnterBarcodeLink())) {
                    if (i > 4) {
                        break;
                    }
                    mobileCommands.pressBack();
                    i++;
                }
            }
        }
        int i = 0;
        while (!mobileCommands.elementDisplayed(harvesterNativeStagingMap.selectStaging())) {
            if (i > 4) {
                break;
            }
            mobileCommands.pressBack();
            i++;
        }
    }

    public void overRideStagingZone() {
        harvesterNativePage.confirmOverRideStagingZone();
    }

    public void verifyHarvesterStagingForNotReadyContainer(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            if (updatedTestOutputData.containsKey(ExcelUtils.NOT_READY_CONTAINER))
                harvesterNativeStagingPage.verifyHarvesterStaging(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.NOT_READY_CONTAINER)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void stageFromContainer(String scenario, HashMap<String, String> testOutputData) {
        try {
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            nativeTogglePage.handleToggle(updatedTestOutputData);
            harvesterNativeStagingPage.stageItemMovementFromContainer(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void stageToContainer(String scenario, HashMap<String, String> testOutputData) {
        try {
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            nativeTogglePage.handleToggle(updatedTestOutputData);
            harvesterNativeStagingPage.stageItemMovementToContainer(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void verifyHarvesterStagingAfterItemMovementPcl(String scenario, HashMap<String, String> testOutputData) {
        try {
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.STAGING)) {
                    PermanentContainerLabelHelper.performItemMovementAfterStaging(scenario, testOutputData);
                }
            }
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.ORDER_LOOKUP)) {
                    PermanentContainerLabelHelper.performItemMovementViaOrderLookup(scenario, testOutputData);
                }
            }
            if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.orderDetailsHeader())) {
                harvesterNativeStagingPage.returnToHomeFromScanningScreen();
            }
            if ((!Boolean.parseBoolean(testOutputData.get(ExcelUtils.CROSS_TEMP_ITEM_MOVEMENT)))) {
                harvesterNativeStagingPage.harvesterStagingAfterItemMovementPcl(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData.get(ExcelUtils.STORE_ID), testOutputData);
            }
            ExcelUtils.writeToExcel(scenario, testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void stageAllContainers(HashMap<String, String> testOutputData) {
        try {
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
            harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void performItemMovement(String scenario, HashMap<String, String> testOutputData) {
        try {
            PermanentContainerLabelHelper.itemMovementTo = "";
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.STAGING)) {
                PermanentContainerLabelHelper.performItemMovementViaStaging(scenario, testOutputData);
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyHarvesterTakeOverAssigningPcl(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testData);
            if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                harvesterNativeSelectingPage.takeOverTrolleyInAssigningSingleThread(scenario, testOutputData, itemData);
            } else {
                harvesterNativeSelectingPage.takeOverTrolleyInAssigning(scenario, testOutputData, itemData);
            }
            ExcelUtils.writeToExcel(scenario, testOutputData);
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public void takeOverRunAndSelecting(String scenario, HashMap<String, String> testOutputData, String status) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT))) {
                PermanentContainerLabelHelper.updateContainer(scenario, testOutputData);
            }
            HashMap<String, String> testData1 = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData1);
            harvesterNativeStorePage.setupLocation(updatedTestOutputData.get(ExcelUtils.STORE_DIVISION_ID), updatedTestOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testData);
            if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                harvesterNativeSelectingPage.verifyTakeoverHarvesterSelectingSingleThreadPcl(scenario, updatedTestOutputData, itemData);
            } else {
                harvesterNativeSelectingPage.verifyTakeOverHarvesterSelectingPcl(scenario, updatedTestOutputData, itemData, status);
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void performItemMovementOrderLookupAndStage(String scenario) {
        try {
            HashMap<String, String> testDataUpdated = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testDataUpdated);
            PermanentContainerLabelHelper.itemMovementTo = "";
            harvesterNativeStorePage.setupLocation(updatedTestOutputData.get(ExcelUtils.STORE_DIVISION_ID), updatedTestOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(updatedTestOutputData);
            PermanentContainerLabelHelper.performItemMovementViaOrderLookup(scenario, updatedTestOutputData);
            harvesterNativeStagingPage.verifyHarvesterStagingPcl(ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)), updatedTestOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyMultipleOrderHarvesterSelectingPcl(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
                nativeTogglePage.handleToggle(testOutputData);
            }
            harvesterNativeSelectingPage.verifyHarvesterSelectingPcl(scenario, testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
            mobileCommands.browserBack();
            if (!mobileCommands.elementDisplayed(harvesterNativeSelectingMap.selecting())) {
                mobileCommands.browserBack();
            }
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyMultipleOrderHarvesterSelecting(String scenario, HashMap<String, String> testOutputData) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            testOutputData.putAll(testData);
            if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
                nativeTogglePage.handleToggle(testOutputData);
            }
            harvesterNativeSelectingPage.verifyHarvesterSelecting(testOutputData, itemData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)));
            mobileCommands.pressBack();
            return testOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public HashMap<String, String> verifyHarvesterSelectingMultipleOrderWithCollapseTempAll(String scenario, HashMap<String, String> firstOrderOutputData, HashMap<String, String> secondOrderOutputData) {
        try {
            List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
            harvesterNativeStorePage.setupLocation(firstOrderOutputData.get(ExcelUtils.STORE_DIVISION_ID), firstOrderOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(firstOrderOutputData);
            harvesterNativeSelectingPage.verifyHarvesterSelectingForCollapseTempAll(itemData, firstOrderOutputData, secondOrderOutputData);
            return firstOrderOutputData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public void setUpStoreForMultipleOrders(HashMap<String, String> testOutputData) {
        try {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.hamburgerMenuIcon());
            mobileCommands.tap(harvesterNativeSelectingMap.storeChangeButton());
            harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
    }

    public void assignNewOsLabel(String scenario, HashMap<String, String> secondOrderOutputData) {
        String newPclLabel = secondOrderOutputData.get(ExcelUtils.STORE_DIVISION_ID) + secondOrderOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.OVERSIZE + PermanentContainerLabelHelper.generateRandomPclNumber();
        String newTempType = "", trolley = "";
        HashMap<String, String> pclTrolleyMapSecondOrder = new HashMap<>();
        HashMap<String, String> pclTemperatureMapSecondOrder = new HashMap<>();
        HashMap<String, String> pclIdMapSecondOrder = new HashMap<>();
        for (Map.Entry<String, String> pclLabelTempMap : ExcelUtils.convertStringToMap(secondOrderOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)).entrySet()) {
            newTempType = pclLabelTempMap.getValue();
        }
        for (Map.Entry<String, String> pclLabelTrolleyMap : ExcelUtils.convertStringToMap(secondOrderOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP)).entrySet()) {
            trolley = pclLabelTrolleyMap.getValue();
        }
        pclTrolleyMapSecondOrder.put(newPclLabel, trolley);
        pclTemperatureMapSecondOrder.put(newPclLabel, newTempType);
        pclIdMapSecondOrder.put((newPclLabel.substring(6)), newTempType);
        secondOrderOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMapSecondOrder));
        secondOrderOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureMapSecondOrder));
        secondOrderOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdMapSecondOrder));
        ExcelUtils.writeToExcel(scenario, secondOrderOutputData);
    }

    public void assignNewPclLabel(String scenario, HashMap<String, String> secondOrderOutputData, HashMap<String, String> firstOrderOutputData) {
        HashMap<String, String> pclTrolleyMapFirstOrder = ExcelUtils.convertStringToMap(firstOrderOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> pclTrolleyMapSecondOrder = ExcelUtils.convertStringToMap(secondOrderOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> pclTemperatureMapSecondOrder = ExcelUtils.convertStringToMap(secondOrderOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> pclIdMap = ExcelUtils.convertStringToMap(secondOrderOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String stagedPclLabel = "";
        String newPclLabel = "";
        String trolley = "";
        String temp = "";
        for (Map.Entry<String, String> tempMap : pclTemperatureMapSecondOrder.entrySet()) {
            for (Map.Entry<String, String> pclFirstOrderLabel : pclTrolleyMapFirstOrder.entrySet()) {
                if (pclFirstOrderLabel.getKey().equals(tempMap.getKey())) {
                    temp = tempMap.getValue();
                    break;
                }
            }
        }
        for (Map.Entry<String, String> pclBaseOrderLabel : pclTrolleyMapFirstOrder.entrySet()) {
            for (Map.Entry<String, String> pclNewOrderLabel : pclTrolleyMapSecondOrder.entrySet()) {
                if (pclBaseOrderLabel.getKey().equals(pclNewOrderLabel.getKey())) {
                    if (pclBaseOrderLabel.getKey().contains(Constants.PickCreation.FLOATING_LABEL)) {
                        newPclLabel = secondOrderOutputData.get(ExcelUtils.STORE_DIVISION_ID) + secondOrderOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.FLOATING_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
                    } else {
                        newPclLabel = secondOrderOutputData.get(ExcelUtils.STORE_DIVISION_ID) + secondOrderOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.PERMANENT_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
                    }
                    stagedPclLabel = pclNewOrderLabel.getKey();
                    trolley = pclNewOrderLabel.getValue();
                    break;
                }
            }
        }
        secondOrderOutputData.put(ExcelUtils.UPDATED_PCL_VALUE, newPclLabel);
        pclTrolleyMapSecondOrder.remove(stagedPclLabel);
        pclTemperatureMapSecondOrder.remove(stagedPclLabel);
        pclIdMap.remove(stagedPclLabel);
        pclTrolleyMapSecondOrder.put(newPclLabel, trolley);
        pclTemperatureMapSecondOrder.put(newPclLabel, temp);
        pclIdMap.put(newPclLabel, temp);
        secondOrderOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMapSecondOrder));
        secondOrderOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureMapSecondOrder));
        secondOrderOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdMap));
        ExcelUtils.writeToExcel(scenario, secondOrderOutputData);
    }

    public HashMap<String, String> collapseTrolleysSelecting(String scenario) {
        try {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            harvesterNativeStorePage.setupLocation(testData.get(ExcelUtils.STORE_DIVISION_ID), testData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testData);
            mobileCommands.tap(harvesterNativeSelectingMap.selecting());
            int trolleyCount = Integer.parseInt(testData.get(ExcelUtils.TROLLEY_COUNT));
            for (int i = 1; i <= trolleyCount; i++) {
                String containerList = testData.get(ExcelUtils.TROLLEY_CONTAINERS + i);
                List<String> containerIdList = ExcelUtils.convertStringToList(containerList);
                harvesterNativeSelectingPage.collapseTemperatureTrolleysSelecting(containerIdList);
            }
            return testData;
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
            return null;
        }
    }

    public void updateMapsAfterItemMovementOS(String scenario) {
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        String oversizeItemMoved = testOutputData.get(ExcelUtils.MOVED_OS_ITEM);
        HashMap<String, String> osContainer = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER));
        for (Map.Entry<String, String> oversizeUpc : osContainer.entrySet()) {
            if (oversizeUpc.getValue().equals(oversizeItemMoved)) {
                osContainer.remove(oversizeUpc.getKey());
                testOutputData.put(ExcelUtils.OVER_SIZED_CONTAINER, String.valueOf(osContainer));
                ExcelUtils.writeToExcel(scenario, testOutputData);
                break;
            }
        }
    }

    public void verifySelectingInProgress(String takeOverTrolley) {
        try {
            Assert.assertEquals(harvesterNativePage.getProgressTagText(takeOverTrolley), SELECTING);
        } catch (Exception e) {
            harvesterNativeSelectingPage.moveToRequiredTrolleyInProgress();
            Assert.assertEquals(harvesterNativePage.getProgressTagText(takeOverTrolley), SELECTING);
        }
    }

    public void batchContainersFromHarvester(HashMap<String, String> testOutputData) {
        validateTrolleysGenerated(testOutputData);
    }

    public void setUpStoreForTrolleyRequest(HashMap<String, String> testOutputData) {
        harvesterNativeStorePage.setupLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        nativeTogglePage.handleToggle(testOutputData);
        harvesterNativeSelectingPage.clickOnSelectingButton();
    }

    private void validateTrolleysGenerated(HashMap<String, String> testOutputData) {
        Boolean batched = pickingServicesHelper.getAllContainersBatched(testOutputData.get(ExcelUtils.ORDER_NUMBER));
        int i = 0;
        while (batched) {
            requestTrolleyFromHarvester();
            batched = pickingServicesHelper.getAllContainersBatched(testOutputData.get(ExcelUtils.ORDER_NUMBER));
            i++;
            if (i > 40) {
                Assert.fail(Constants.PickCreation.TROLLEY_NOT_GENERATED);
            }
        }
    }

    private void requestTrolleyFromHarvester() {
        scrollToRequestTrolleyButton();
        clickRequestTrolleyButton();


    }

    private void clickRequestTrolleyButton() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.requestTrolleyButton());
        mobileCommands.tap(harvesterNativeSelectingMap.requestTrolleyButton());
    }

    private void scrollToRequestTrolleyButton() {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.newSectionTrolleysText());
        String trolleys = mobileCommands.getElementText(harvesterNativeSelectingMap.newSectionTrolleysText());
        if (trolleys.equalsIgnoreCase("New") || Integer.parseInt(((trolleys.split("\\("))[1].split("\\)"))[0]) < 3) {
            actions.sendKeys(Keys.DOWN).build().perform();
            return;
        }
        String totalTrolleys = ((trolleys.split("\\("))[1].split("\\)"))[0];
        int numberOfTrolleys = Integer.parseInt(totalTrolleys);
        if (numberOfTrolleys > 4) {
            try {
                for (int i = 0; i < (numberOfTrolleys / 2); i++) {
                    String text = mobileCommands.getWebDriver().getPageSource();
                    if (text != null && text.contains("Request Trolley")) {
                        break;
                    } else {
                        for (int j = 0; j < 4; j++) {
                            actions.sendKeys(Keys.DOWN).build().perform();
                        }
                    }
                }

            } catch (Exception e) {
                Assert.fail(Constants.PickCreation.TROLLEY_NOT_FOUND);
            }
        }
    }
}
