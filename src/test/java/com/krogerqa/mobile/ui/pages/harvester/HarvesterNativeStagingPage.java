package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.api.HarvesterServicesHelper;
import com.krogerqa.api.StagingServicesHelper;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeSelectingMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeStagingMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.util.*;

public class HarvesterNativeStagingPage {

    private static final String ITEMS_CANNOT_BE_MOVED = "Items cannot be moved to this container. Please scan a container that is fully selected or staged.";
    static HashMap<String, String> stagingZones = new HashMap<>();
    private static HarvesterNativeStagingPage instance;
    HarvesterServicesHelper harvesterServicesHelper = HarvesterServicesHelper.getInstance();
    HarvesterNativeStagingMap harvesterNativeStagingMap = HarvesterNativeStagingMap.getInstance();
    HarvesterNativeSelectingMap harvesterNativeSelectingMap = HarvesterNativeSelectingMap.getInstance();
    StagingServicesHelper stagingServicesHelper = StagingServicesHelper.getInstance();
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativePage harvesterNativePage = HarvesterNativePage.getInstance();
    HarvesterNativeMap harvesterNativeMap = HarvesterNativeMap.getInstance();
    Set<String> stagingZoneSet = new HashSet<>();
    HashMap<String, String> updateStagingZoneMap = new HashMap<>();
    String temp = "";

    private HarvesterNativeStagingPage() {
    }

    public synchronized static HarvesterNativeStagingPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeStagingPage.class) {
            if (instance == null) {
                instance = new HarvesterNativeStagingPage();
            }
        }
        return instance;
    }

    public void verifyHarvesterStaging(HashMap<String, String> containerMap, String storeId, HashMap<String, String> testOutputData) {
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(storeId, testOutputData);
        int i = 0;
        for (Map.Entry<String, String> entry : containerMap.entrySet()) {
            temp = stagingZonesBarcode.get(entry.getKey());
            stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, temp);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_SINGLE_TROLLEY_PICKING))) {
                stagingZones.put(entry.getKey(), stagingZonesBarcode.get(entry.getValue()));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CUE_STAGING_ZONE_VALIDATION))) {
                stagingZones.put(entry.getKey(), stagingZonesBarcode.get(entry.getValue()));
                testOutputData.put(ExcelUtils.CONTAINER_STAGING_ZONE_MAP, String.valueOf(stagingZones));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INVALID_STAGING_LOCATION_VALIDATION))) {
                invalidStagingZoneValidation(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), entry.getValue(), containerMap, testOutputData);
            }
            if (i++ == containerMap.entrySet().size() - 1) {
                mobileCommands.tap(harvesterNativeStagingMap.doneButton());
            }
        }
    }

    public void verifyHarvesterStagingPcl(HashMap<String, String> pclTemperatureContainerMap, HashMap<String, String> testOutputData) {
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        HashMap<String, String> stagingZonesBarcode = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.GET_STAGING_ZONES));
        for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
            if (!entry.getKey().equals(PermanentContainerLabelHelper.fromContainerLabel)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    stagingEnterBarcodePclOversize(entry.getKey(), testOutputData);
                } else {
                    stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, entry.getValue());
                }
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                break;
            }
        }
        testOutputData.put(ExcelUtils.UPDATED_STAGING_ZONE_MAP, String.valueOf(updateStagingZoneMap));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.doneButton());
        mobileCommands.tap(harvesterNativeStagingMap.doneButton());
    }

    public boolean osItemsInContainer() {
        return mobileCommands.elementDisplayed(harvesterNativeStagingMap.osItemsRemaining());
    }

    public void stagingEnterBarcodePclOversize(String containerId, HashMap<String, String> testOutputData) {
        HashMap<String, String> overSizeUpc = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTIPLE_OS_UPC_TEMP));
        HashMap<String, String> originalStagingZones = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.STAGING_ZONES));
        String scenario = testOutputData.get(ExcelUtils.SCENARIO);
        List<String> substituedUpcList = new ArrayList<>();
        harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        boolean multipleSubstitution = testOutputData.containsKey(ExcelUtils.MULTIPLE_SUBS) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_SUBS));
        while (osItemsInContainer()) {
            String overSizeUpcValue = "";
            if (multipleSubstitution) {
                String substitutionUpc = mobileCommands.getElementText(harvesterNativeStagingMap.selectFirstSubItem()).replace("UPC:", "").trim();
                substituedUpcList.add(substitutionUpc);
                harvesterNativePage.enterBarcode(substitutionUpc, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.unStagedItems());
                int totalItemsSubstituted = mobileCommands.numberOfElements(harvesterNativeStagingMap.substituteItemsRemaining()) - 1;
                for (int i = 1; i <= totalItemsSubstituted; i++) {
                    substitutionUpc = mobileCommands.getElementText(harvesterNativeStagingMap.performOsSubstitution(i + 1)).replace("UPC:", "").trim();
                    harvesterNativePage.enterBarcode(substitutionUpc, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                    substituedUpcList.add(substitutionUpc);
                }
                testOutputData.put(ExcelUtils.MULTIPLE_SUBS_UPC, String.valueOf(substituedUpcList));
                ExcelUtils.writeToExcel(scenario, testOutputData);
            } else {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NOT_READY_SUBSTITUTION))) {
                    overSizeUpcValue = mobileCommands.getElementText(harvesterNativeStagingMap.selectSubstitutedOsContainer()).replace("UPC:", "").trim();
                    harvesterNativePage.enterBarcode(overSizeUpcValue, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                } else {
                    overSizeUpcValue = mobileCommands.getElementText(harvesterNativeStagingMap.selectFirstOsContainer()).replace("UPC:", "").trim();
                    harvesterNativePage.enterBarcode(overSizeUpcValue, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                }
            }
            String overSizeUpcTemp = "";
            for (Map.Entry<String, String> overSizeUpcMap : overSizeUpc.entrySet()) {
                if ((Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_SINGLE_TROLLEY_PICKING)))) {
                    anchorStagingZoneValidation();
                }
                if (overSizeUpcValue.equals(overSizeUpcMap.getKey())) {
                    overSizeUpcTemp = overSizeUpcMap.getValue();
                    break;
                } else if (multipleSubstitution) {
                    overSizeUpcTemp = overSizeUpcMap.getValue();
                }
            }
            for (Map.Entry<String, String> OSIterator : originalStagingZones.entrySet()) {
                if (overSizeUpcTemp.equalsIgnoreCase(OSIterator.getKey())) {
                    harvesterNativePage.enterBarcode(OSIterator.getValue(), Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                    testOutputData.put(ExcelUtils.OS_STAGING_ZONE, OSIterator.getValue());
                    ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
                    break;
                }
            }
            if (!mobileCommands.elementDisplayed(harvesterNativeMap.enterBarcodeButton())) {
                if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.locationOverrideContinueButton())) {
                    mobileCommands.tap(harvesterNativeStagingMap.locationOverrideContinueButton());
                }
            }
        }
    }

    private void anchorStagingZoneValidation() {
        if (!stagingZones.entrySet().isEmpty()) {
            for (Map.Entry<String, String> previousContainer : stagingZones.entrySet()) {
                if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.anchorLastStagedContainerInfo())) {
                    Assert.assertEquals(("Last " + previousContainer.getValue().substring(0, 2) + " container: " + previousContainer.getKey() + " /"), mobileCommands.getElementText(harvesterNativeStagingMap.anchorLastStagedContainerInfo()));
                    Assert.assertEquals((previousContainer.getValue()), mobileCommands.getElementText(harvesterNativeStagingMap.anchorLastStagedContainerLocation()));
                }
            }
        }
    }

    public void stageItemMovementFromContainer(HashMap<String, String> pclTemperatureContainerMap, String storeId, HashMap<String, String> testOutputData) {
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(storeId, testOutputData);
        for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
            temp = stagingZonesBarcode.get(entry.getKey());
            if (!entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                if (entry.getValue().equals(PermanentContainerLabelHelper.fromContainerTemp)) {
                    stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, temp);
                    PermanentContainerLabelHelper.fromContainerLabel = entry.getKey();
                    break;
                }
            }
        }
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
    }

    public void stageItemMovementToContainer(HashMap<String, String> pclTemperatureContainerMap, String storeId, HashMap<String, String> testOutputData) {
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        HashMap<String, String> containerStatusMap = new HashMap<>();
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(storeId, testOutputData);
        for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
            temp = stagingZonesBarcode.get(entry.getKey());
            if (entry.getValue().equals(PermanentContainerLabelHelper.toContainerTemp)) {
                stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, temp);
                PermanentContainerLabelHelper.toContainerLabel = entry.getKey();
                containerStatusMap.put(entry.getKey(), ExcelUtils.STAGED);
                testOutputData.put(ExcelUtils.CONTAINER_STATUS_MAP, String.valueOf(containerStatusMap));
                ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
                break;
            }
        }
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
    }

    public void harvesterStagingAfterItemMovementPcl(HashMap<String, String> pclTemperatureContainerMap, String storeId, HashMap<String, String> testOutputData) {
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(storeId, testOutputData);
        if (PermanentContainerLabelHelper.toContainerStatus.equalsIgnoreCase(ExcelUtils.STAGED)) {
            for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
                temp = stagingZonesBarcode.get(entry.getKey());
                if (!entry.getKey().equals(PermanentContainerLabelHelper.toContainerLabel)) {
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        stagingEnterBarcodePclOversize(entry.getKey(), testOutputData);
                    } else {
                        stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, temp);
                    }
                }
            }
        } else {
            for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
                temp = stagingZonesBarcode.get(entry.getKey());
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    stagingEnterBarcodePclOversize(entry.getKey(), testOutputData);
                } else {
                    stagingEnterBarcode(entry.getKey(), stagingZonesBarcode.get(entry.getValue()), testOutputData, temp);
                }
            }
        }
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
    }

    public void moveItems(String itemsToMove) {
        mobileCommands.tap(harvesterNativeStagingMap.kebabMenu());
        mobileCommands.elementWhenClickable(harvesterNativeStagingMap.moveItemsOptions());
        mobileCommands.tap(harvesterNativeStagingMap.moveItemsOptions());

        if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
            mobileCommands.tap(harvesterNativeStagingMap.moveAllItemsCheckbox());
        } else {
            if (!itemsToMove.equalsIgnoreCase(ExcelUtils.OS_ITEM_MOVEMENT)) {
                String upc = (mobileCommands.getElementText(harvesterNativeStagingMap.getUpcOnItemMovement()).split(" "))[1];
                mobileCommands.tap(harvesterNativeStagingMap.someItemsCheckbox(upc));
            }
        }
        mobileCommands.tap(harvesterNativeStagingMap.nextButton());
    }

    public void moveFromOneContainerToAnother(String scenario, String itemMovementFrom, String itemsToMove, String
            itemMovementTo, String fromContainerTemp, String toContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        waitForStagingMenuVisibility();
        mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
        harvesterNativePage.enterBarcode(itemMovementFrom, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        moveItems(itemsToMove);
        harvesterNativePage.enterBarcode(itemMovementTo, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeStagingMap.toastMessage()), Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE);
            waitForToastInvisibility();
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.MOVEMENT_TO_OOS))) {
            if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeStagingMap.toastMessage()), ITEMS_CANNOT_BE_MOVED);
            waitForToastInvisibility();
        } else {
            if (!toContainerTemp.equals(fromContainerTemp)) {
                mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.scanContainerToMoveText());
                if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.scanContainerToMoveText())) {
                    mobileCommands.assertElementDisplayed(harvesterNativeStagingMap.scanContainerToMoveText(), true);
                    mobileCommands.browserBack();
                    mobileCommands.browserBack();
                }
            } else {
                mobileCommands.tap(harvesterNativeStagingMap.moveItems());
                if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                    pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
                    pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
                }

                if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL) || (itemsToMove.equalsIgnoreCase(ExcelUtils.OS_ITEM_MOVEMENT))) {
                    pclTemperatureContainerMap.remove(itemMovementFrom);
                    pclIdTemperatureMap.remove(pclLocationFromContainer);
                }
                testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
                testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
                ExcelUtils.writeToExcel(scenario, testOutputData);
            }
        }
    }

    private void waitForStagingMenuVisibility() {
        mobileCommands.waitForElementVisibility(harvesterNativeStagingMap.selectStaging());
    }

    public void waitForToastInvisibility() {
        mobileCommands.waitForElementInvisible(harvesterNativeStagingMap.toastPopup());
    }

    public void returnToHomeFromScanningScreen() {
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
        mobileCommands.tap(harvesterNativeStagingMap.backButton());
    }

    public void stagingEnterBarcode(String containerId, String stagingZoneBarcode, HashMap<String, String> testOutputData, String temp) {
        harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        if ((Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.REJECT_BAG_FEES))) || (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES_AFTER_PICKING)))) {
            mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagsIcon());
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
            String temperature = mobileCommands.getElementText(harvesterNativeStagingMap.stagingTemperatureForBulkStaging());
            Assert.assertEquals(temperature, Constants.PickCreation.AMBIENT_BULK_DE_STAGING, Constants.PickCreation.INCORRECT_TEMP_TYPE_BULK_STAGING);
        }
        if ((Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_SINGLE_TROLLEY_PICKING)))) {
            if (!stagingZones.entrySet().isEmpty()) {
                for (Map.Entry<String, String> previousContainer : stagingZones.entrySet()) {
                    if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.anchorLastStagedContainerInfo())) {
                        Assert.assertEquals(("Last " + previousContainer.getValue().substring(0, 2) + " container: " + previousContainer.getKey() + " /"), mobileCommands.getElementText(harvesterNativeStagingMap.anchorLastStagedContainerInfo()));
                        Assert.assertEquals((previousContainer.getValue()), mobileCommands.getElementText(harvesterNativeStagingMap.anchorLastStagedContainerLocation()));
                    }
                }
            }
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_ZONE))) {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                String stagingArea = "";
                try {
                    stagingArea = mobileCommands.getElementText(harvesterNativeStagingMap.stagingArea()).replaceAll("[^A-Z]+", "");
                } catch (Exception | AssertionError e) {
                    mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
                }
                String recommendedStagingZone = StringUtils.substring(stagingZoneBarcode, 0, stagingZoneBarcode.length() - 1) + stagingArea;
                stagingZoneSet.add(recommendedStagingZone);
                harvesterNativePage.enterBarcode(recommendedStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                updateStagingZoneMap.put(recommendedStagingZone, temp);
            } else {
                if (Objects.equals(HarvesterServicesHelper.ambientStagingZone, "")) {
                    HarvesterServicesHelper.ambientStagingZone = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.GET_STAGING_ZONES)).get(Constants.PickCreation.AMBIENT);
                }
                harvesterNativePage.enterBarcode(HarvesterServicesHelper.ambientStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
            }
            if (!mobileCommands.elementDisplayed(harvesterNativeMap.enterBarcodeButton())) {
                if (mobileCommands.elementDisplayed(harvesterNativeStagingMap.locationOverrideContinueButton())) {
                    mobileCommands.tap(harvesterNativeStagingMap.locationOverrideContinueButton());
                }
            }
        } else {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                harvesterNativePage.enterBarcode(HarvesterServicesHelper.ambientStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
            } else {
                harvesterNativePage.enterBarcode(stagingZoneBarcode, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                updateStagingZoneMap.put(stagingZoneBarcode, temp);
            }
        }
    }

    public void verifyHarvesterStagingReusePcl(HashMap<String, String> containerMap, String
            storeId, HashMap<String, String> testOutputData) {
        HashMap<String, String> stagingZonesBarcode = harvesterServicesHelper.getStagingZones(storeId, testOutputData);
        for (Map.Entry<String, String> entry : containerMap.entrySet()) {
            stagingServicesHelper.startStageRun(storeId, stagingZonesBarcode.get(entry.getValue()), entry.getKey());
        }
    }

    public void invalidStagingZoneValidation(String Temperature, String stagingZoneBarcode, String
            Container, HashMap<String, String> containerMap, HashMap<String, String> testOutputData) {
        String[] invalidZones = {"invalidArea", "invalidLevel", "invalidPosition"};
        String invalidStagingZone;
        String reStageZone;
        String sameTempContainer = null;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INVALID_STAGING_LOCATION_VALIDATION))) {
            List<String> containerTemperature = new ArrayList<>();
            for (Map.Entry<String, String> values : containerMap.entrySet()) {
                if (!(containerTemperature.contains(values.getValue()))) {
                    containerTemperature.add(values.getValue());
                } else {
                    sameTempContainer = values.getValue();
                }
            }
        }
        for (String invalidZone : invalidZones) {
            if (invalidZone.equals("invalidArea")) {
                invalidStagingZone = stagingZoneBarcode.replace(stagingZoneBarcode.substring(4, 5), "0");
            } else if (invalidZone.equals("invalidLevel")) {
                invalidStagingZone = stagingZoneBarcode.replace(stagingZoneBarcode.substring(6, 7), "0");
            } else {
                invalidStagingZone = stagingZoneBarcode.replace(stagingZoneBarcode.substring(8), "0");
            }
            harvesterNativePage.enterBarcode(invalidStagingZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
            String message = "The scanned staging location " + invalidStagingZone + " was not found.";
            Assert.assertEquals(message, mobileCommands.getElementText(harvesterNativeStagingMap.toastMessage()));
        }
        if (!Container.equals(sameTempContainer)) {
            int area = Integer.parseInt(stagingZoneBarcode.substring(4, 5));
            area = (area > 2) ? (area - 1) : (area + 1);
            reStageZone = stagingZoneBarcode.replace(stagingZoneBarcode.substring(4, 5), Integer.toString(area));
            harvesterNativePage.enterBarcode(reStageZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
            String reStageContainerMessage = "Are you sure you want to re-stage this container?";
            Assert.assertEquals(reStageContainerMessage, mobileCommands.getElementText(harvesterNativeStagingMap.reStageContainerMessage()), "found");
            mobileCommands.tap(harvesterNativeStagingMap.reStageContainerContinueButton());
            stagingZones.put(Temperature, reStageZone);
            testOutputData.put(ExcelUtils.CONTAINER_STAGING_ZONE_MAP, String.valueOf(stagingZones));
            String containerAlreadyStaged = "This container is already staged in the scanned location " + reStageZone + ".";
            harvesterNativePage.enterBarcode(reStageZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
            Assert.assertEquals(containerAlreadyStaged, mobileCommands.getElementText(harvesterNativeStagingMap.toastMessage()));
        }
    }

}
