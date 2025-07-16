package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeContainerLookUpMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import org.testng.Assert;

import java.util.HashMap;

public class HarvesterNativeContainerLookUpPage {

    private static HarvesterNativeContainerLookUpPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeContainerLookUpMap harvesterNativeContainerLookupMap = HarvesterNativeContainerLookUpMap.getInstance();

    private HarvesterNativeContainerLookUpPage() {
    }

    public synchronized static HarvesterNativeContainerLookUpPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeContainerLookUpPage.class) {
            if (instance == null) {
                instance = new HarvesterNativeContainerLookUpPage();
            }
        }
        return instance;
    }

    public void clickOnContainerLookUp() {
        mobileCommands.tap(harvesterNativeContainerLookupMap.hamburgerMenuIcon());
        mobileCommands.tap(harvesterNativeContainerLookupMap.selectContainerLookup());
    }

    public void enterBarcode(String barcode) {
        mobileCommands.tap(harvesterNativeContainerLookupMap.containerLookUpEnterBarcodeLink());
        mobileCommands.enterText(harvesterNativeContainerLookupMap.containerLookUpBarcodeInput(), barcode, true);
        mobileCommands.tap(harvesterNativeContainerLookupMap.continueButton());
    }

    public void moveItems(String itemsToMove) {
        mobileCommands.tap(harvesterNativeContainerLookupMap.containerLookUpMoveItems());
        if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
            mobileCommands.tap(harvesterNativeContainerLookupMap.containerLookUpMoveAllItemsCheckbox());
        } else {
            String upc = (mobileCommands.getElementText(harvesterNativeContainerLookupMap.getUpcOnItemMovement()).split(" "))[1];
            mobileCommands.tap(harvesterNativeContainerLookupMap.someItemsCheckbox(upc));
        }
        mobileCommands.tap(harvesterNativeContainerLookupMap.nextButton());
    }

    public void waitForToastInvisibility() {
        mobileCommands.waitForElementInvisible(harvesterNativeContainerLookupMap.toastPopup());
    }

    /** currently it is not being used, but developed for future usages*/
    public void moveFromOneContainerToAnother(String scenario, String itemMovementFrom, String itemsToMove, String itemMovementTo, String fromContainerTemp, String toContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom;
        if (!itemMovementFrom.contains(Constants.PickCreation.OVERSIZE)) {
            pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        }
        clickOnContainerLookUp();
        enterBarcode(itemMovementFrom);
        moveItems(itemsToMove);
        enterBarcode(itemMovementTo);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeContainerLookupMap.toastMessage()))
                Assert.assertEquals(Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE, mobileCommands.getElementText(harvesterNativeContainerLookupMap.toastMessage()));
            waitForToastInvisibility();
        } else {
            if (!fromContainerTemp.equalsIgnoreCase(toContainerTemp) && pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                mobileCommands.tap(harvesterNativeContainerLookupMap.continueButton());
            }
            mobileCommands.tap(harvesterNativeContainerLookupMap.moveItems());
            if (PermanentContainerLabelHelper.fromContainerStatus == null) {
                PermanentContainerLabelHelper.fromContainerStatus = "";
            }
            if (!PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                if (mobileCommands.elementDisplayed(harvesterNativeContainerLookupMap.toastMessage()))
                    Assert.assertEquals(Constants.PickCreation.ITEM_MOVEMENT_TOAST_MESSAGE, mobileCommands.getElementText(harvesterNativeContainerLookupMap.toastMessage()));
                waitForToastInvisibility();
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
                pclIdTemperatureMap.remove(pclLocationFromContainer);
                pclTemperatureContainerMap.remove(itemMovementFrom);
            }
            if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
                pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.SOME) && PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                mobileCommands.tap(harvesterNativeContainerLookupMap.closeItemMovementPopup());
            }
            mobileCommands.browserBack();
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        }
    }

    public void updatePclMaps(String scenario, String itemMovementTo, String toContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        String newPcl;
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclTrolleyMapOld = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        pclTemperatureContainerMap.remove(itemMovementTo);
        pclIdTemperatureMap.remove(pclLocationToContainer);
        newPcl = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.PERMANENT_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
        String Trolley = testOutputData.get(ExcelUtils.TAKE_OVER_TROLLEY);
        pclTemperatureContainerMap.put(newPcl, toContainerTemp);
        pclIdTemperatureMap.put((newPcl.substring(6)), toContainerTemp);
        multiContainerTrolley.remove(itemMovementTo, Trolley);
        multiContainerTrolley.put(newPcl, Trolley);
        pclTrolleyMapOld.remove(itemMovementTo);
        pclTrolleyMapOld.put(newPcl, Trolley);
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMapOld));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
        testOutputData.put(ExcelUtils.MULTI_CONTAINER_TROLLEY, String.valueOf(multiContainerTrolley));
        ExcelUtils.writeToExcel(scenario, testOutputData);
        mobileCommands.browserBack();
    }
}
