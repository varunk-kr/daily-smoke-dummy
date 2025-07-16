package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.api.HarvesterServicesHelper;
import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeContainerLookUpMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeOrderLookUpMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeStagingMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.pages.cue.CueHomePage;
import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HarvesterNativeOrderLookUpPage {
    private static HarvesterNativeOrderLookUpPage instance;
    static PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    String ORDER_TYPE_MISMATCH = "Order type didn't match in Harvester";
    String ORDER_STATUS_MISMATCH = "Order status didn't match in Harvester";
    String PICK = "Pick";
    String SELECT = "Select";
    HarvesterNativeMap harvesterNativeMap = HarvesterNativeMap.getInstance();
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeContainerLookUpMap harvesterNativeContainerLookupMap = HarvesterNativeContainerLookUpMap.getInstance();
    HarvesterNativeOrderLookUpMap harvesterNativeOrderLookUpMap = HarvesterNativeOrderLookUpMap.getInstance();
    HarvesterServicesHelper harvesterServicesHelper = HarvesterServicesHelper.getInstance();
    HarvesterNativePage harvesterNativePage = HarvesterNativePage.getInstance();
    HarvesterNativeStagingMap harvesterNativeStagingMap = HarvesterNativeStagingMap.getInstance();

    private HarvesterNativeOrderLookUpPage() {
    }

    public synchronized static HarvesterNativeOrderLookUpPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeOrderLookUpPage.class) {
            if (instance == null) {
                instance = new HarvesterNativeOrderLookUpPage();
            }
        }
        return instance;
    }

    public void clickOnOrderLookUp() {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.hamburgerMenuIcon());
        mobileCommands.tap(harvesterNativeOrderLookUpMap.selectOrderLookup());
    }

    public void clickOnInformationLookUp() {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.hamburgerMenuIcon());
        mobileCommands.tap(harvesterNativeOrderLookUpMap.clickOnInformationLookupButton());
    }

    public void enterBarcode(String barcode) {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpEnterBarcodeLink());
        mobileCommands.enterText(harvesterNativeOrderLookUpMap.orderLookUpBarcodeInput(), barcode, true);
        mobileCommands.tap(harvesterNativeOrderLookUpMap.continueButton());
    }

    public void enterBarcodeItemMovementTo(String barcode) {
        mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.orderLookUpEnterBarcodeToContainerLink());
        mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpEnterBarcodeToContainerLink());
        try {
            mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.orderLookUpBarcodeInput());
        } catch (Exception | AssertionError e) {
            mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.orderLookUpEnterBarcodeToContainerLink());
            mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpEnterBarcodeToContainerLink());
            mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.orderLookUpBarcodeInput());
        }
        mobileCommands.enterText(harvesterNativeOrderLookUpMap.orderLookUpBarcodeInput(), barcode, true);
        mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.continueBtn());
        mobileCommands.tap(harvesterNativeOrderLookUpMap.continueButton());
    }

    public HashMap<String, String> moveItems(String itemsToMove, HashMap<String, String> testOutputData, String fromContainerId, String toContainerId) {
        HashMap<String, String> osContainerMap = new HashMap<>();
        if (!testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).equals("{}")) {
            osContainerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER));
        }

        mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpMoveItems());
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            if (fromContainerId.contains(Constants.PickCreation.OVERSIZE) && !fromContainerId.contains(testOutputData.get(ExcelUtils.STORE_DIVISION_ID))) {
                fromContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + fromContainerId;
            }
            String pclId = fromContainerId.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && fromContainerId.contains(Constants.PickCreation.OVERSIZE)) {
                String toContainerLabel = toContainerId.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
                mobileCommands.tap(harvesterNativeOrderLookUpMap.collapseContainer(toContainerLabel));
            }
            if (!mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.orderLookUpMoveAllItemsCheckbox())) {
                mobileCommands.tap(harvesterNativeOrderLookUpMap.expandContainer(pclId));
            }
        }
        if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpMoveAllItemsCheckbox());
        } else {
            String upc;
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_OOS_PICKED_ITEMS))) {
                upc = (mobileCommands.getElementText(harvesterNativeOrderLookUpMap.getUpcOnItemMovementForPickedItems()).split(" "))[1];
            } else {
                upc = (mobileCommands.getElementText(harvesterNativeOrderLookUpMap.getUpcOnItemMovement()).split(" "))[1];
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && !testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).equals("{}")) {
                for (Map.Entry<String, String> ele : osContainerMap.entrySet()) {
                    if (ele.getValue().equals(upc)) {
                        testOutputData.put(ExcelUtils.MOVED_OS_ITEM, ele.getValue());
                        break;
                    }
                }
            }
            mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
            mobileCommands.tap(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
        }
        mobileCommands.tap(harvesterNativeOrderLookUpMap.nextButton());
        return testOutputData;
    }

    public void waitForToastInvisibility() {
        mobileCommands.waitForElementInvisible(harvesterNativeContainerLookupMap.toastPopup());
    }

    public void verifyOrderTypeAndOrderStatus(HashMap<String, String> testOutputData) {
        JsonPath jsonPath = new JsonPath(pickingServicesHelper.queryItemContainers(testOutputData.get(ExcelUtils.ORDER_NUMBER)));
        String orderType = jsonPath.getString("data.pick-lists.additionalAttributes.orderType");
        String orderStatus = jsonPath.getString("data.pick-lists.additionalAttributes.orderStatus");
        if (orderType.equalsIgnoreCase(Constants.PickCreation.SISTER_STORE)) {
            orderType = CueHomePage.CUE_SOURCE_HOME_STORE;
        }
        if (orderStatus.contains(PICK)) {
            orderStatus = orderStatus.replace(PICK, SELECT);
        }
        mobileCommands.waitForElementVisibility(harvesterNativeMap.orderType());
        mobileCommands.waitForElementVisibility(harvesterNativeMap.orderStatus());
        Assert.assertEquals(orderType, mobileCommands.getElementText(harvesterNativeMap.orderType()), ORDER_TYPE_MISMATCH);
        Assert.assertEquals(orderStatus, mobileCommands.getElementText(harvesterNativeMap.orderStatus()), ORDER_STATUS_MISMATCH);
    }

    public HashMap<String, String> moveFromOneContainerToAnother(String scenario, String itemMovementFrom, String itemsToMove, String itemMovementTo, String fromContainerTemp, String toContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom;
        if (!itemMovementFrom.contains(Constants.PickCreation.OVERSIZE)) {
            pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            clickOnInformationLookUp();
            if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
                enterBarcode(itemMovementTo);
                mobileCommands.waitForElementVisibility(harvesterNativeMap.viewOSOrderDetails());
                mobileCommands.tap(harvesterNativeMap.viewOSOrderDetails());
            } else
                enterBarcode(itemMovementFrom);
        } else {
            clickOnOrderLookUp();
            enterBarcode(itemMovementFrom);
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.CROSS_TEMP_ITEM_MOVEMENT))) {
            verifyOrderTypeAndOrderStatus(testOutputData);
        }
        if (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT)) {
            moveMultipleContainerItems(pclIdTemperatureMap, itemMovementFrom, testOutputData);
        } else {
            moveItems(itemsToMove, testOutputData, itemMovementFrom, itemMovementTo);
        }
        enterBarcodeItemMovementTo(itemMovementTo);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeOrderLookUpMap.toastMessage()), Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE);
            waitForToastInvisibility();
        } else {
            if (!fromContainerTemp.equalsIgnoreCase(toContainerTemp) && pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                mobileCommands.tap(harvesterNativeOrderLookUpMap.continueBtn());
            }
            mobileCommands.tap(harvesterNativeOrderLookUpMap.moveItems());
            if (PermanentContainerLabelHelper.fromContainerStatus == null) {
                PermanentContainerLabelHelper.fromContainerStatus = "";
            }
            if (!PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.toastMessage()))
                    Assert.assertEquals(mobileCommands.getElementText(harvesterNativeOrderLookUpMap.toastMessage()), Constants.PickCreation.ITEM_MOVEMENT_TOAST_MESSAGE);
                waitForToastInvisibility();
            }
            if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
                pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
                pclTemperatureContainerMap.remove(itemMovementFrom);
                pclIdTemperatureMap.remove(pclLocationFromContainer);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.SOME) && PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                mobileCommands.tap(harvesterNativeContainerLookupMap.closeItemMovementPopup());
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_OOS_PICKED_ITEMS))) {
                pclTemperatureContainerMap.remove(itemMovementFrom);
                pclIdTemperatureMap.remove(pclLocationFromContainer);
            }
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
            mobileCommands.browserBack();
            mobileCommands.browserBack();
        }
        return testOutputData;
    }

    public void moveMultipleContainerItems(HashMap<String, String> containerMap, String itemMovementFrom, HashMap<String, String> testData) {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.orderLookUpMoveItems());
        String upc = (mobileCommands.getElementText(harvesterNativeOrderLookUpMap.getUpcOnItemMovement()).split(" "))[1];
        mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
        mobileCommands.tap(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
        mobileCommands.tap(harvesterNativeOrderLookUpMap.itemMovementContainer(itemMovementFrom.substring(6)));
        String noItemMovementContainer = testData.get(ExcelUtils.NO_ITEM_MOVEMENT_CONTAINER);
        for (Map.Entry<String, String> container : containerMap.entrySet()) {
            if (!container.getValue().equals(noItemMovementContainer)) {
                if (!container.getKey().contains(Constants.PickCreation.OVERSIZE) && !container.getKey().equals(itemMovementFrom.substring(6))) {
                    mobileCommands.tap(harvesterNativeOrderLookUpMap.itemMovementContainer(container.getKey()));
                    upc = (mobileCommands.getElementText(harvesterNativeOrderLookUpMap.getUpcOnItemMovement()).split(" "))[1];
                    mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
                    mobileCommands.tap(harvesterNativeOrderLookUpMap.someItemsCheckbox(upc));
                    mobileCommands.tap(harvesterNativeOrderLookUpMap.itemMovementContainer(container.getKey()));
                }
            }
        }
        mobileCommands.tap(harvesterNativeOrderLookUpMap.nextButton());
        if (testData.containsKey(ExcelUtils.IS_MULTIPLE_TEMP_CONTAINER_ITEM_MOVEMENT)) {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.continueButton());
        }
    }

    public HashMap<String, String> itemMovementFromOSToAnother(String scenario, String itemMovementFrom, String itemsToMove, String itemMovementTo, String fromContainerTemp, String toContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom;
        if (!itemMovementFrom.contains(Constants.PickCreation.OVERSIZE) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            try {
                clickOnInformationLookUp();
            } catch (Exception e) {
                clickOnInformationLookUp();
            }
        } else {
            clickOnOrderLookUp();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_AFTER_STAGING)) && itemMovementFrom.contains(Constants.PickCreation.OVERSIZE)) {
            enterBarcode(itemMovementTo);
        } else {
            enterBarcode(itemMovementFrom);
        }
        String fetchVisualId = mobileCommands.getElementText(harvesterNativeOrderLookUpMap.SearchVisualOrderID()).split(Constants.PickCreation.TEXT_FROM_VISUAL_ID)[1];
        String visualOrderId = testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
        if (fetchVisualId.equalsIgnoreCase(visualOrderId)) {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.clickOnView());
        }
        if (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_CONTAINER_ITEM_MOVEMENT)) {
            moveMultipleContainerItems(pclIdTemperatureMap, itemMovementFrom, testOutputData);
        } else {
            testOutputData = moveItems(itemsToMove, testOutputData, itemMovementFrom, itemMovementTo);
        }
        enterBarcodeItemMovementTo(itemMovementTo);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.toastMessage())) {
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeOrderLookUpMap.toastMessage()), Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE);
                waitForToastInvisibility();
            }
        } else {
            if (!fromContainerTemp.equalsIgnoreCase(toContainerTemp) && pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.continueBtn());
                mobileCommands.waitForElementClickability(harvesterNativeOrderLookUpMap.continueBtn());
                mobileCommands.tap(harvesterNativeOrderLookUpMap.continueBtn());
            }
            mobileCommands.tap(harvesterNativeOrderLookUpMap.moveItems());
            if (PermanentContainerLabelHelper.fromContainerStatus == null) {
                PermanentContainerLabelHelper.fromContainerStatus = "";
            }
            if (!PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.toastMessage()))
                    Assert.assertEquals(mobileCommands.getElementText(harvesterNativeOrderLookUpMap.toastMessage()), Constants.PickCreation.ITEM_MOVEMENT_TOAST_MESSAGE);
                waitForToastInvisibility();
            }
            if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
                pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
                pclIdTemperatureMap.remove(pclLocationFromContainer);
                pclTemperatureContainerMap.remove(itemMovementFrom);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.SOME) && PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
                mobileCommands.tap(harvesterNativeContainerLookupMap.closeItemMovementPopup());
            }
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
            mobileCommands.browserBack();
            ExcelUtils.writeToExcel(scenario, testOutputData);
            mobileCommands.browserBack();
        }
        return testOutputData;
    }


    public void itemMovementViaStagingLocation(String scenario, String itemMovementFrom, String itemsToMove, String itemMovementTo, String fromContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        String visualOrderId = testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        HashMap<String, String> stagingZones = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.UPDATED_STAGING_ZONE_MAP));
        clickOnInformationLookUp();
        for (Map.Entry<String, String> stagingZonesMap : stagingZones.entrySet()) {
            String stagingZonesFromMap = stagingZonesMap.getKey();
            enterBarcode(stagingZonesFromMap);
            break;
        }
        scrollToOrder(visualOrderId);
        tapViewButton(visualOrderId);
        moveItems(itemsToMove, testOutputData, itemMovementFrom, itemMovementTo);
        enterBarcodeItemMovementTo(itemMovementTo);
        mobileCommands.tap(harvesterNativeOrderLookUpMap.moveItems());
        if (PermanentContainerLabelHelper.fromContainerStatus == null)
            PermanentContainerLabelHelper.fromContainerStatus = "";
        if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
            pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
            pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
        }
        if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
            pclTemperatureContainerMap.remove(itemMovementFrom);
            pclIdTemperatureMap.remove(pclLocationFromContainer);
        }

        if (itemsToMove.equalsIgnoreCase(ExcelUtils.SOME) && PermanentContainerLabelHelper.fromContainerStatus.equals(ExcelUtils.STAGED)) {
            mobileCommands.tap(harvesterNativeContainerLookupMap.closeItemMovementPopup());
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
        ExcelUtils.writeToExcel(scenario, testOutputData);
        mobileCommands.browserBack();
        mobileCommands.browserBack();
    }

    public String getReStagingZones(String storeId, HashMap<String, String> testOutputData, String osStagingZone) {
        return harvesterServicesHelper.getReStagingZoneForOverSize(storeId, testOutputData, osStagingZone);
    }

    public void performReStagingViaInformationLookup(HashMap<String, String> testOutputData) {
        testOutputData = ExcelUtils.getTestDataMap(testOutputData.get(ExcelUtils.SCENARIO), testOutputData.get(ExcelUtils.SCENARIO));
        String storeId = testOutputData.get(ExcelUtils.STORE_ID);
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_STAGE_FROM_LOOKUP))) {
            mobileCommands.tap(harvesterNativeStagingMap.selectStaging());
            mobileCommands.tap(harvesterNativeStagingMap.selectInformationLookup());
        } else {
            clickOnInformationLookUp();
        }
        String visualOrderId = testOutputData.get(ExcelUtils.VISUAL_ORDER_ID);
        String osStagingZone = testOutputData.get(ExcelUtils.OS_STAGING_ZONE);
        String overSizeReStageZone = getReStagingZones(storeId, testOutputData, osStagingZone);
        enterBarcode(osStagingZone);
        scrollToOrder(visualOrderId);
        tapKebabMenu(visualOrderId);
        clickOnReStage();
        harvesterNativePage.enterBarcode(overSizeReStageZone, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        if (mobileCommands.elementDisplayed(harvesterNativeOrderLookUpMap.locationOverrideContinueButton())) {
            mobileCommands.tap(harvesterNativeOrderLookUpMap.locationOverrideContinueButton());
        }
        mobileCommands.browserBack();
    }

    public void tapViewButton(String visualId) {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.viewButton(visualId));
    }

    public void clickOnReStage() {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.reStageButton());
    }

    public void tapKebabMenu(String orderId) {
        mobileCommands.tap(harvesterNativeOrderLookUpMap.kebabMenu(orderId));
    }

    public void scrollToOrder(String visualId) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        mobileCommands.waitForElementVisibility(harvesterNativeOrderLookUpMap.noOfContainers());
        int numberOfContainers = Integer.parseInt(mobileCommands.getElementText(harvesterNativeOrderLookUpMap.noOfContainers()));
        try {
            for (int i = 0; i < (numberOfContainers); i++) {
                String orderId = mobileCommands.getWebDriver().getPageSource();
                if (orderId != null && orderId.contains(visualId)) {
                    break;
                } else {
                    for (int j = 0; j < 4; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail(Constants.PickCreation.ORDER_NOT_FOUND);
        }
    }
}



