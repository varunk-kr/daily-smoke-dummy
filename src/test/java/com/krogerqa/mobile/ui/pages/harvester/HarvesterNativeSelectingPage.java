package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.api.ApiUtils;
import com.krogerqa.api.HarvesterServicesHelper;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeContainerLookUpMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeSelectingMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HarvesterNativeSelectingPage {

    private static final String ITEM_FULFILLED_TOAST = "Item successfully fulfilled.";
    private static final String TROLLEY_COMPLETE_TITLE = "Trolley Complete";
    private static final String A = "A";
    private static final String R = "R";
    private static final String F = "F";
    public static boolean isFirstTrolleyPicked = true;
    public static boolean dbOsSplitTrolleyStaging = false;
    private static final String ITEM_SUBSTITUTED_TOAST = "Item has been successfully substituted.";
    private static final String UPDATE_PCL_TOAST = "Error assigning some containers. Please tap these containers to update.";
    private static final String UPDATE_PCL_TOAST_SINGLE_THREAD = "Failed to assign containers. Please try again.";
    private static final String ASSIGNING = "Assigning";
    private static final String TAKE_OVER_RUN = "Take over run";
    public static List<String> scannedTrolleys = new ArrayList<>();
    public static Boolean isMultipleOS = false;
    public static String preWeighedItemOldContainer = "";
    public static String preWeighedItemContainer = "";
    public static String preWeighedItemContainerTemp = "";
    public static String preWeighedItemMovementToContainer = "";
    public static String preWeighedItemMovementFromContainer = "";
    static int numberOfContainerInTrolley;
    static String pclContainerId = "";
    static List<String> takeOverTrolleys = new ArrayList<>();
    public static List<String> pclAssignedContainers = new ArrayList<>();
    static HashMap<String, String> multiContainerTrolley = new HashMap<>();
    static String OversizeContainer;
    static boolean moveItemsViaPicking = false;
    static String ADDITIONAL_ATTRIBUTES = "additionalAttributes";
    static String CONTAINER_COUNT = "containerCount";
    static String TROLLEY_NAME = "name";
    private static HarvesterNativeSelectingPage instance;
    public int mafpItemQuantity;
    static int itemQuantity;
    public static HashMap<String, String> pclLabelTempMapForDBOsSplitTrolley = new HashMap<>();
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeSelectingMap harvesterNativeSelectingMap = HarvesterNativeSelectingMap.getInstance();
    HarvesterNativePage harvesterNativePage = HarvesterNativePage.getInstance();
    HarvesterNativeMap harvesterNativeMap = HarvesterNativeMap.getInstance();
    HarvesterNativeContainerLookUpMap harvesterNativeContainerLookupMap = HarvesterNativeContainerLookUpMap.getInstance();
    String upcId = "";
    List<String> multipleOverSizeUpcList = new ArrayList<>();
    String upcIdForMAFP = "";

    private HarvesterNativeSelectingPage() {
    }

    public synchronized static HarvesterNativeSelectingPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeSelectingPage.class) {
            if (instance == null) {
                instance = new HarvesterNativeSelectingPage();
            }
        }
        return instance;
    }

    public void moveItems() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.kebabMenuIcon());
        mobileCommands.tap(harvesterNativeSelectingMap.kebabMenuIcon());
        mobileCommands.waitForElementClickability(harvesterNativeSelectingMap.moveItemsToAnotherContainerOption());
        mobileCommands.tap(harvesterNativeSelectingMap.moveItemsToAnotherContainerOption());
    }

    public HashMap<String, String> moveFromOneContainerToAnother(String scenario, String itemMovementFrom, String itemsToMove, String itemMovementTo, String fromContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> pclLabelTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        String pclLocationToContainer = itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        String pclLocationFromContainer = itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1];
        moveItems();
        harvesterNativePage.enterBarcode(itemMovementTo, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.toastMessage()), Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE);
            waitForToastInvisibility();
        } else {
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.toastMessage()), ITEM_FULFILLED_TOAST);
            waitForToastInvisibility();
            if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
                pclIdTemperatureMap.put(pclLocationToContainer, fromContainerTemp);
            }
            if (itemsToMove.equalsIgnoreCase(ExcelUtils.ALL)) {
                pclTemperatureContainerMap.remove(itemMovementFrom);
                pclLabelTrolleyMap.remove(itemMovementFrom);
                pclIdTemperatureMap.remove(pclLocationFromContainer);
            }
            if (!testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES).contains(itemMovementFrom)) {
                pclTemperatureContainerMap.remove(itemMovementFrom);
                pclIdTemperatureMap.remove(pclLocationFromContainer);
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER))) {
                preWeighedItemOldContainer = pclLocationFromContainer;
                preWeighedItemContainer = pclLocationToContainer;
                preWeighedItemContainerTemp = fromContainerTemp;
                preWeighedItemMovementToContainer = itemMovementTo;
                preWeighedItemMovementFromContainer = itemMovementFrom;
            }
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclLabelTrolleyMap));
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        }
        return testOutputData;
    }

    public void waitForToastInvisibility() {
        mobileCommands.waitForElementInvisible(harvesterNativeSelectingMap.toastPopup());
    }

    private void scrollToTrolley(String trolleyId, Keys keys) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.newSectionTrolleysText());
        String trolleys = mobileCommands.getElementText(harvesterNativeSelectingMap.newSectionTrolleysText());
        String totalTrolleys = ((trolleys.split("\\("))[1].split("\\)"))[0];
        int numberOfTrolleys = Integer.parseInt(totalTrolleys);
        if (numberOfTrolleys > 4) {
            try {
                for (int i = 0; i < (numberOfTrolleys / 2); i++) {
                    String text = mobileCommands.getWebDriver().getPageSource();
                    if (text != null && text.contains(trolleyId)) {
                        break;
                    } else {
                        for (int j = 0; j < 4; j++) {
                            actions.sendKeys(keys).build().perform();
                        }
                    }
                }

            } catch (Exception e) {
                Assert.fail(Constants.PickCreation.TROLLEY_NOT_FOUND);
            }
        }
    }

    public void scrollToTrolleyFromTop(String trolleyId) {
        scrollToTrolley(trolleyId, Keys.DOWN);
    }

    public void scrollToTrolleyFromBottom(String trolleyId) {
        scrollToTrolley(trolleyId, Keys.UP);
    }

    public void verifyHarvesterSelecting(HashMap<String, String> testData, List<HashMap<String, String>> itemsList, boolean isCarryover) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        List<String> containerIdList;
        HashMap<String, String> containerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.CONTAINER_MAP));
        if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_FLEX_SCENARIO))) {
            containerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.DE_METER_CONTAINER_MAP));
        }
        if (isCarryover) {
            containerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.NOT_PICKED_CONTAINER_MAP));
        }
        //First perform scanning for all the collapse temp for the no pcl
        if (testData.containsKey(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS) && (!Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_TEMP_COLLAPSE)))) {
            containerIdList = ExcelUtils.convertStringToList(testData.get(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS));
            collapseTemperatureTrolleysSelecting(containerIdList);
            //remove the already scanned collapse temperature trolleys
            for (String container : containerIdList) {
                containerMap.remove(container);
            }
        }
        for (String containerId : containerMap.keySet()) {
            if (testData.containsKey(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS) && (Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_TEMP_COLLAPSE)))) {
                multipleTrolleySelecting(upcLabel, itemsList, testData);
                break;
            }
            scanFirstContainer(containerId);
            if (!Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                if (testData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) && testData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                    if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_MAFP))) {
                        multiThreadedTrolleySelecting(containerId, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, testData);
                    } else {
                        multipleOrderTrolleySelecting(containerId);
                    }
                } else {
                    multiThreadedTrolleySelecting(containerId, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, testData);
                }
            } else {
                if (mobileCommands.elementDisplayed(harvesterNativeMap.startRunForSingleThreadStore())) {
                    clickOnStartRunForSingleThreadStore();
                }
                singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                break;
            }
        }
    }

    public void clickOnStartRunForSingleThreadStore() {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.startRunForSingleThreadStore());
        try {
            mobileCommands.tap(harvesterNativeMap.startRunForSingleThreadStore());
        } catch (Exception e) {
            mobileCommands.tap(harvesterNativeMap.startRunForSingleThreadStore());
        }
    }

    public void verifyHarvesterSelectingPcl(String scenario, HashMap<String, String> testData, List<HashMap<String, String>> itemsList, boolean isCarryover) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        HashMap<String, Integer> bagCountMap = new HashMap<>();
        multiContainerTrolley = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        if (testData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
            if (ExcelUtils.convertStringToMap(testData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY)).size() > multiContainerTrolley.size()) {
                multiContainerTrolley = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
            }
        }
        HashMap<String, String> pclTrolleyMap = updatePclTrolleyMap(testData, isCarryover);
        boolean onlyOsContainer = false;
        clickOnSelectingButton();
        for (Map.Entry<String, String> pclLabelMap : pclTrolleyMap.entrySet()) {
            isMultipleOS = false;
            boolean isMultiOrderLastData = Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_CANCEL_SCENARIO));
            if (!scannedTrolleys.contains(pclLabelMap.getValue())) {
                scannedTrolleys.add(pclLabelMap.getValue());
                moveToPclTrolley(pclLabelMap.getValue());
                try {
                    getNumberOfContainersInTrolleyThroughApi(testData, pclLabelMap.getValue());
                } catch (Exception | AssertionError e) {
                    getNumberOfContainersInTrolleyThroughUi(pclLabelMap.getValue());
                }
                /*BYOB*/
                bagCountMap = verifyBagIconsBeforePicking(testData, pclTrolleyMap, pclLabelMap, bagCountMap);
                if (numberOfContainerInTrolley > 1) {
                    scannedTrolleys.add(pclLabelMap.getValue());
                    if (mobileCommands.getElementText(harvesterNativeMap.getSkittleTemperature(pclLabelMap.getValue())).equals(Constants.PickCreation.OVERSIZE)) {
                        tapStartButtonForSingleOversizeContainer(pclLabelMap.getValue(), testData);
                        onlyOsContainer = true;
                    } else {
                        tapStartButton(pclLabelMap.getValue());
                    }
                    if (Boolean.parseBoolean((testData.get(ExcelUtils.IS_MULTIPLE_OS_ORDER))) && (pclLabelMap.getKey().contains(Constants.PickCreation.OVERSIZE)) && !Boolean.parseBoolean(testData.get(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES))) {
                        checkForMultipleOSOrders();
                        multipleOrderTrolleySelecting(pclLabelMap.getKey());
                        continue;
                    }
                    verifyBagIconsDuringContainerAssignment(testData, bagCountMap, pclLabelMap.getValue());
                    for (Map.Entry<String, String> multiContainer : multiContainerTrolley.entrySet()) {
                        if (multiContainer.getValue().equals(pclLabelMap.getValue())) {
                            if (!Boolean.parseBoolean(testData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER)) && !onlyOsContainer) {
                                getTemperatureTypeAndScanContainers(testData);
                            }
                            break;
                        }
                    }
                    harvesterNativePage.startRunButton();
                    if (Boolean.parseBoolean(testData.get(ExcelUtils.HEADS_UP_SCREEN))) {
                        headsUpScreenConfirmation();
                    }
                    verifyCheckBoxForByob(testData, pclTrolleyMap, pclLabelMap, bagCountMap);
                    if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                        if (Boolean.parseBoolean(testData.get(ExcelUtils.HARRIS_TEETER_DYB))) {
                            clickOnStartRunForSingleThreadStore();
                        }
                        singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                        if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS)) && isFirstTrolleyPicked) {
                            updateMapsForSecondTrolley(pclLabelMap.getValue(), pclLabelMap.getKey(), testData);
                            HarvesterNativeSelectingPage.pclAssignedContainers.clear();
                            isFirstTrolleyPicked = false;
                            break;
                        }
                        continue;
                    }
                    if (Boolean.parseBoolean(testData.get(ExcelUtils.HARRIS_TEETER_DYB))) {
                        clickOnStartRunForSingleThreadStore();
                    }
                    multiThreadedPclTrolleySelecting(scenario, testData, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, pclLabelMap.getValue());
                    if (isMultiOrderLastData) {
                        break;
                    }
                } else {
                    if (mobileCommands.getElementText(harvesterNativeMap.getSkittleTemperature(pclLabelMap.getValue())).equals(Constants.PickCreation.OVERSIZE)) {
                        tapStartButtonForSingleOversizeContainer(pclLabelMap.getValue(), testData);
                    } else {
                        tapStartButton(pclLabelMap.getValue());
                    }
                    if (!Boolean.parseBoolean(testData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER))) {
                        scanPclLabel(pclLabelMap.getKey(), testData);
                    }
                    harvesterNativePage.startRunButton();
                    if ((Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!pclLabelMap.getKey().contains("OS")) && (!Boolean.parseBoolean(testData.get(ExcelUtils.REJECT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)))) {
                        bagCountMap.forEach((key, value) -> Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeSelectingMap.containerWithBags(pclLabelMap.getKey().substring(6))), "Containers not matching"));
                        checkForBagsConfirmation();
                    }
                    if (testData.containsKey(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)) {
                        bagCountMap.forEach((key, value) -> Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeSelectingMap.containerWithBags(pclLabelMap.getKey().substring(6))), "Containers not matching"));
                        checkForBagsConfirmation();
                    }
                    if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                        singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                        continue;
                    }
                    multiThreadedPclTrolleySelecting(scenario, testData, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, pclLabelMap.getValue());
                }
            }
            if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS)) && isFirstTrolleyPicked) {
                if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_DB_OS_REUSE_PCL_SPLIT_TROLLEY))) {
                    updateMapsForDbSplitSecondTrolley(pclLabelMap.getValue(), pclLabelMap.getKey(), testData);
                } else {
                    updateMapsForSecondTrolley(pclLabelMap.getValue(), pclLabelMap.getKey(), testData);
                }
                HarvesterNativeSelectingPage.pclAssignedContainers.clear();
                isFirstTrolleyPicked = false;
                break;
            }
            if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_DB_OS_REUSE_PCL_STAGING)) && dbOsSplitTrolleyStaging) {
                HashMap<String, String> pclLabelTempMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
                HashMap<String, String> updatedPclTempMap = new HashMap<>();
                for (Map.Entry<String, String> map : pclLabelTempMap.entrySet()) {
                    if (map.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        updatedPclTempMap.put(map.getKey(), map.getValue());
                    }
                }
                testData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclTempMap));
                ExcelUtils.writeToExcel(scenario, testData);
            }
            onlyOsContainer = false;
        }
        bagCountMap.clear();
    }

    private void verifyCheckBoxForByob(HashMap<String, String> testData, HashMap<String, String> pclTrolleyMap, Map.Entry<String, String> pclLabelMap, HashMap<String, Integer> bagCountMap) {
        if ((Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!pclLabelMap.getKey().contains("OS")) && (!Boolean.parseBoolean(testData.get(ExcelUtils.REJECT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)) && (!Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))))) {
            bagCountMap.forEach((key, value) -> Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeSelectingMap.containerWithBags(pclLabelMap.getKey().substring(6))), "Containers not matching"));
            checkForBagsConfirmation();
        }
        if (testData.containsKey(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)) {
            bagCountMap.forEach((key, value) -> Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeSelectingMap.containerWithBags(pclLabelMap.getKey().substring(6))), "Containers not matching"));
            checkForBagsConfirmation();
        }
        if ((Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES))) && Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
            for (Map.Entry<String, String> pclContainer : pclTrolleyMap.entrySet()) {
                if (!pclContainer.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    Assert.assertTrue(mobileCommands.elementDisplayed(harvesterNativeSelectingMap.containerWithBags(pclContainer.getKey().substring(6))), "Containers not matching");
                }
            }
            checkForBagsConfirmation();
        }
    }

    private void verifyBagIconsDuringContainerAssignment(HashMap<String, String> testData, HashMap<String, Integer> bagCountMap, String value) {
        if ((Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.REJECT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)))) {
            for (int i = 1; i == bagCountMap.size(); i++) {
                mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagCountContainerAssignment(i));
            }
        }
        if (Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
            HashMap<String, String> commonTrolleyBagMap = getBagCountForCommonTrolley(ExcelUtils.convertStringToMap(testData.get(ExcelUtils.BAG_MAP)), testData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY), value);
            for (int i = 1; i == commonTrolleyBagMap.size(); i++) {
                mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagCountContainerAssignment(i));
            }
        }
    }

    private HashMap<String, Integer> verifyBagIconsBeforePicking(HashMap<String, String> testData, HashMap<String, String> pclTrolleyMap, Map.Entry<String, String> pclLabelMap, HashMap<String, Integer> bagCountMap) {
        if ((Boolean.parseBoolean(testData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.REJECT_BAG_FEES))) && (!Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG)))) {
            if (!Boolean.parseBoolean(testData.get(ExcelUtils.BYOB_ALL_COLLAPSE))) {
                for (Map.Entry<String, String> pclLabels : pclTrolleyMap.entrySet()) {
                    if ((pclLabels.getValue().equals(pclLabelMap.getValue())) && (!pclLabels.getKey().contains(Constants.PickCreation.OVERSIZE))) {
                        bagCountMap.put(pclLabels.getKey(), bagCountMap.getOrDefault(pclLabelMap.getValue(), 0) + 1);
                    }
                }
            } else {
                for (Map.Entry<String, String> pcl : pclTrolleyMap.entrySet()) {
                    if (!pcl.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        bagCountMap.put(pcl.getKey(), bagCountMap.getOrDefault(pclLabelMap.getValue(), 0) + 1);
                    }
                }
            }
            if (!pclLabelMap.getKey().contains(Constants.PickCreation.OVERSIZE) && !Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.getBagCountInTrolley(pclLabelMap.getValue())), bagCountMap.size() + " w/ bags", "bag count is not matching");
            }
            if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.getBagCountInTrolley(pclLabelMap.getValue())), bagCountMap.size() + " w/ bags", "bag count is not matching");
            }
        }
        if (testData.containsKey(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG) && Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
            HashMap<String, String> bagMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.BAG_MAP));
            HashMap<String, String> bagMapCommonTrolley = getBagCountForCommonTrolley(bagMap, testData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY), pclLabelMap.getValue());
            Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.getBagCountInTrolley(pclLabelMap.getValue())), bagMapCommonTrolley.size() + " w/ bags", "bag count is not matching");
        }
        return bagCountMap;
    }

    private HashMap<String, String> updatePclTrolleyMap(HashMap<String, String> testData, boolean isCarryover) {
        HashMap<String, String> pclTrolleyMap;
        if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_FLEX_SCENARIO))) {
            pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.DE_METER_CONTAINER_MAP));
        } else {
            pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        }
        if (isCarryover) {
            if (testData.containsKey(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS) && Boolean.parseBoolean(testData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
                pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
            } else {
                pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.NOT_PICKED_CONTAINER_MAP));
            }
        }
        return pclTrolleyMap;
    }

    public static void updateMapsForDbSplitSecondTrolley(String trolley, String osLabel, HashMap<String, String> testOutputData) {
        String temp;
        if (osLabel.contains(Constants.PickCreation.OVERSIZE)) {
            temp = HarvesterServicesHelper.AMBIENT_TEMPERATURE;
        } else {
            temp = HarvesterServicesHelper.FROZEN_TEMPERATURE;
        }
        boolean frozenTrolleyPicked = false;
        boolean overSizeTrolleyPicked = false;
        pclLabelTempMapForDBOsSplitTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> updatedPclTrolleyMap = new HashMap<>();
        HashMap<String, String> updatedPclTempMap = new HashMap<>();
        String trolleysFromExcel = String.valueOf(testOutputData.get(ExcelUtils.TROLLEYS));
        Set<String> trolleys = Arrays.stream(trolleysFromExcel.substring(1, trolleysFromExcel.length() - 1).split(",")).collect(Collectors.toSet());
        String unpickedTrolley = "";
        for (Map.Entry<String, String> map : ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)).entrySet()) {
            if (map.getKey().startsWith(Constants.PickCreation.OVERSIZE) && map.getKey().substring(8, 13).equalsIgnoreCase(trolley)) {
                if (temp.equalsIgnoreCase(HarvesterServicesHelper.AMBIENT_TEMPERATURE)) {
                    overSizeTrolleyPicked = true;
                }
            } else if (map.getKey().substring(7, 12).equalsIgnoreCase(trolley)) {
                if (temp.equalsIgnoreCase(HarvesterServicesHelper.FROZEN_TEMPERATURE)) {
                    frozenTrolleyPicked = true;
                }
            }
        }
        for (String key : trolleys) {
            if (!key.contains(trolley)) {
                unpickedTrolley = key;
            }
        }
        if (frozenTrolleyPicked) {
            for (Map.Entry<String, String> map : pclTrolleyMap.entrySet()) {
                if (map.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    updatedPclTrolleyMap.put(map.getKey(), unpickedTrolley);
                }
            }
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(updatedPclTrolleyMap));
            dbOsSplitTrolleyStaging = true;
        }
        if (overSizeTrolleyPicked) {
            for (Map.Entry<String, String> map : pclLabelTempMapForDBOsSplitTrolley.entrySet()) {
                if (map.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    updatedPclTempMap.put(map.getKey(), unpickedTrolley);
                }
            }
            for (Map.Entry<String, String> map : pclTrolleyMap.entrySet()) {
                if (map.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    updatedPclTrolleyMap.put(map.getKey(), unpickedTrolley);
                }
            }
            testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(updatedPclTrolleyMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPclTempMap));
        }
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
    }

    public void updateMapsForSecondOrderPCLOS(HashMap<String, String> firstOrderData, HashMap<String, String> secondOrderData) {
        HashMap<String, String> firstOrderPCLLabelTrolleyMap = ExcelUtils.convertStringToMap(firstOrderData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> secondOrderPCLLabelTrolleyMap = ExcelUtils.convertStringToMap(secondOrderData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> updatedPCLLabelTrolleyMap = new HashMap<>();
        HashMap<String, String> updatedPCLIdTempMap = new HashMap<>();
        HashMap<String, String> updatedPCLLabelTempMap = new HashMap<>();
        String pclLabel = "";
        String trolley = "";
        String oldPCLLabel = "";
        for (Map.Entry<String, String> pcl : firstOrderPCLLabelTrolleyMap.entrySet()) {
            pclLabel = pcl.getKey();
            oldPCLLabel = pcl.getKey();
        }
        for (Map.Entry<String, String> pcl : secondOrderPCLLabelTrolleyMap.entrySet()) {
            trolley = pcl.getValue();
        }
        updatedPCLLabelTrolleyMap.put(pclLabel, trolley);
        HashMap<String, String> secondOrderPCLLabelTempMap = ExcelUtils.convertStringToMap(secondOrderData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> secondOrderPCLIdTempMap = ExcelUtils.convertStringToMap(secondOrderData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        for (Map.Entry<String, String> pcl : secondOrderPCLLabelTempMap.entrySet()) {
            updatedPCLLabelTempMap.put(oldPCLLabel, pcl.getValue());
        }
        for (Map.Entry<String, String> pcl : secondOrderPCLIdTempMap.entrySet()) {
            updatedPCLIdTempMap.put(oldPCLLabel.substring(6), pcl.getValue());
        }
        secondOrderData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(updatedPCLLabelTrolleyMap));
        secondOrderData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(updatedPCLLabelTempMap));
        secondOrderData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(updatedPCLIdTempMap));
        ExcelUtils.writeToExcel(secondOrderData.get(ExcelUtils.SCENARIO), secondOrderData);
    }

    /**
     * Updating the pclTrolleyMap for Pcl OS Prod bug scenarios
     *
     * @param trolley        Trolley number
     * @param pclLabel       PclLabel assigned
     * @param testOutputData TestOutPutData
     */
    public static void updateMapsForSecondTrolley(String trolley, String pclLabel, HashMap<String, String> testOutputData) {
        HashMap<String, String> pclLabelTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> copyOfPclLabelTrolleyMap = new HashMap<>();
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> copyOfPclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        HashMap<String, String> pclLabelTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> copyOfPclLabelTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        for (Map.Entry<String, String> pcl : pclLabelTrolleyMap.entrySet()) {
            if (!pcl.getValue().equals(trolley)) {
                copyOfPclLabelTrolleyMap.put(pclLabel, pcl.getValue());
            }
        }
        for (Map.Entry<String, String> pcl : pclIdTemperatureMap.entrySet()) {
            if (!pclLabel.substring(6).equals(pcl.getKey())) {
                copyOfPclIdTemperatureMap.remove(pcl.getKey());
            }
        }
        for (Map.Entry<String, String> pcl : pclLabelTemperatureMap.entrySet()) {
            if (!pclLabel.equals(pcl.getKey())) {
                copyOfPclLabelTemperatureMap.remove(pcl.getKey());
            }
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(copyOfPclLabelTrolleyMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(copyOfPclIdTemperatureMap));
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(copyOfPclLabelTemperatureMap));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
    }

    public void headsUpScreenConfirmation() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.gotItButton());
        mobileCommands.tap(harvesterNativeSelectingMap.gotItButton());
    }

    public void checkForBagsConfirmation() {
        mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagsConfirmation());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.bagsConfirmationCheckbox());
        mobileCommands.tap(harvesterNativeSelectingMap.bagsConfirmationCheckbox());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.continueButtonForBagsConfirmation());
        mobileCommands.tap(harvesterNativeSelectingMap.continueButtonForBagsConfirmation());
    }

    public void checkForMultipleOSOrders() {
        isMultipleOS = true;
        harvesterNativePage.printOversizeTrolleyLabels();
        waitForToastInvisibility();
        harvesterNativePage.startRunButton();
    }

    public void clickOnSelectingButton() {
        try {
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
            mobileCommands.waitForElementClickability(harvesterNativeSelectingMap.selecting());
            mobileCommands.tap(harvesterNativeSelectingMap.selecting());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.newSectionTrolleysText());
        } catch (Exception | AssertionError e) {
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
            mobileCommands.tap(harvesterNativeSelectingMap.selecting());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.newSectionTrolleysText());
        }
    }

    public void takeOverTrolley(String scenario, HashMap<String, String> testOutputData) {
        String pclTakeOverTrolley = "";
        String pclTakeOverContainer = "";
        if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
            multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        }
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        for (Map.Entry<String, String> pclLabelMap : pclTrolleyMap.entrySet()) {
            if (!scannedTrolleys.contains(pclLabelMap.getValue())) {
                scannedTrolleys.add(pclLabelMap.getValue());
                moveToPclTrolley(pclLabelMap.getValue());
                try {
                    getNumberOfContainersInTrolleyThroughApi(testOutputData, pclLabelMap.getValue());
                } catch (Exception | AssertionError e) {
                    getNumberOfContainersInTrolleyThroughUi(pclLabelMap.getValue());
                }
                if (numberOfContainerInTrolley > 1) {
                    scannedTrolleys.add(pclLabelMap.getValue());
                    tapStartButton(pclLabelMap.getValue());
                    for (Map.Entry<String, String> multiContainer : multiContainerTrolley.entrySet()) {
                        if (multiContainer.getValue().equals(pclLabelMap.getValue())) {
                            getTemperatureTypeAndScanContainers(testOutputData);
                            break;
                        }
                    }
                    harvesterNativePage.startRunButton();
                } else {
                    tapStartButton(pclLabelMap.getValue());
                    scanPclLabel(pclLabelMap.getKey(), testOutputData);
                    harvesterNativePage.startRunButton();
                }
            }
        }
        for (Map.Entry<String, String> takeOverTrolley : pclTrolleyMap.entrySet()) {
            pclTakeOverTrolley = takeOverTrolley.getValue();
            pclTakeOverContainer = takeOverTrolley.getKey();
            break;
        }
        testOutputData.put(ExcelUtils.TAKE_OVER_TROLLEY, pclTakeOverTrolley);
        testOutputData.put(ExcelUtils.TAKE_OVER_CONTAINER_NO, pclTakeOverContainer);
        ExcelUtils.writeToExcel(scenario, testOutputData);
    }

    public void tapStartButton(String trolleyId) {
        try {
            harvesterNativePage.clickStartButton(trolleyId);
        } catch (Exception | AssertionError e) {
            Actions actions = new Actions(mobileCommands.getWebDriver());
            for (int j = 0; j < 10; j++) {
                actions.sendKeys(Keys.DOWN).build().perform();
            }
            mobileCommands.waitForElementVisibility(harvesterNativeMap.trolleyIdText(trolleyId));
            mobileCommands.tap(harvesterNativeMap.trolleyIdText(trolleyId));
        }
    }

    public void tapStartButtonForSingleOversizeContainer(String trolleyId, HashMap<String, String> testOutputData) {
        try {
            mobileCommands.waitForElementClickability(harvesterNativeMap.startTrolleyButton(trolleyId));
            mobileCommands.tap(harvesterNativeMap.startTrolleyButton(trolleyId));

            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                if (!mobileCommands.elementDisplayed(harvesterNativeMap.startRunButton())) {
                    mobileCommands.tap(harvesterNativeMap.startTrolleyButton(trolleyId));
                }
            } else {
                if (!mobileCommands.elementDisplayed(harvesterNativeMap.enterBarcodeButton())) {
                    mobileCommands.tap(harvesterNativeMap.startTrolleyButton(trolleyId));
                }
            }
        } catch (Exception | AssertionError e) {
            mobileCommands.waitForElementVisibility(harvesterNativeMap.trolleyIdText(trolleyId));
            mobileCommands.tap(harvesterNativeMap.trolleyIdText(trolleyId));
        }
    }

    public Boolean scanPclLabel(String pclLabel, HashMap<String, String> testOutputData) {
        if (pclLabel.contains(Constants.PickCreation.OVERSIZE)) {
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                harvesterNativePage.printOversizeTrolleyLabels();
                waitForToastInvisibility();
            } else {
                scanFirstTrolley(pclLabel);
            }
        } else {
            scanFirstTrolley(pclLabel);
        }
        return true;
    }

    public void scanFirstContainer(String containerId) {
        harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
    }

    public void scanFirstTrolley(String trolleyId) {
        harvesterNativePage.enterBarcode(trolleyId, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
    }

    public void moveToPclTrolley(String trolleyId) {
        try {
            scrollToTrolleyFromTop(trolleyId);
        } catch (Exception | AssertionError e) {
            scrollToTrolleyFromBottom(trolleyId);
        }
    }

    public void moveToInProgressTrolley(String trolleyId) {
        scrollToInProgressTrolley(trolleyId);
    }

    public void moveToRequiredTrolleyInProgress() {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        for (int i = 0; i < 3; i++) {
            actions.sendKeys(Keys.DOWN).build().perform();
        }
    }

    private void scrollToInProgressTrolley(String trolleyId) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        String trolleys = mobileCommands.getElementText(harvesterNativeMap.inProgressTrolleyButton());
        String totalTrolleys = ((trolleys.split("\\("))[1].split("\\)"))[0];
        int numberOfTrolleys = Integer.parseInt(totalTrolleys);
        try {
            for (int i = 0; i < 4 * (numberOfTrolleys); i++) {
                String text = mobileCommands.getWebDriver().getPageSource();
                if (text != null && text.contains(trolleyId)) {
                    actions.sendKeys(Keys.DOWN).build().perform();
                    break;
                } else {
                    for (int j = 0; j < 5; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail(Constants.PickCreation.TROLLEY_NOT_FOUND);
        }
    }

    public void getUpcId() {
        upcId = mobileCommands.getElementText(harvesterNativeSelectingMap.itemUpcText()).replace("UPC: ", "").trim();
        upcIdForMAFP = mobileCommands.getElementText(harvesterNativeSelectingMap.itemUpcText());
    }

    public HashMap<String, Integer> getUpcQuantityMap(List<HashMap<String, String>> itemsList) {
        HashMap<String, Integer> upcQuantityMap = new HashMap<>();
        for (HashMap<String, String> itemMap : itemsList) {
            upcQuantityMap.put(itemMap.get(ExcelUtils.ITEM_UPC), Integer.parseInt(itemMap.get(ExcelUtils.ITEM_QUANTITY)));
        }
        return upcQuantityMap;
    }

    public boolean itemsInContainer() {
        return mobileCommands.elementDisplayed(harvesterNativeSelectingMap.itemsToPick());
    }

    public int remainingItemsInTrolley() {
        return mobileCommands.elements(harvesterNativeSelectingMap.itemUpcTextList()).size();
    }

    public void multiThreadedTrolleySelecting(String containerId, String itemWeight, List<HashMap<String, String>> itemsList, HashMap<String, String> upcLabel, HashMap<String, String> testOutputData) {
        String sellByWeightItem = getSellByWeightUpc(itemsList);
        if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
            multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        }
        boolean trolleyId = false;
        while (itemsInContainer()) {
            for (Map.Entry<String, String> multiContainer : multiContainerTrolley.entrySet()) {
                if (multiContainer.getKey().equals(containerId)) {
                    trolleyId = true;
                    break;
                }
            }
            if (sellByWeightItem.isEmpty()) {
                scanItem(upcLabel, itemsList, testOutputData);
                if (!upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY)) && !upcId.equals(upcLabel.get(Constants.PickCreation.OUT_OF_STOCK))) {
                    harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                    waitForToastInvisibility();
                }
            } else {
                try {
                    selectMAFPItems(upcLabel, itemsList, testOutputData, itemWeight);
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
                        if (upcIdForMAFP.equals(upcLabel.get(ExcelUtils.SELL_BY_WEIGHT_ITEM)) || upcIdForMAFP.equals(upcLabel.get(ExcelUtils.SELL_BY_UNIT)) || upcIdForMAFP.equals(upcLabel.get(ExcelUtils.IS_RANDOM_WEIGHT_ITEM))) {
                            if (trolleyId) {
                                String containerNumber = (mobileCommands.getElementText(harvesterNativeSelectingMap.containerNumber()).split(" ")[0]);
                                for (Map.Entry<String, String> e : multiContainerTrolley.entrySet()) {
                                    if (e.getKey().endsWith(containerNumber)) {
                                        containerId = e.getKey();
                                        break;
                                    }
                                }
                            }
                            harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                            waitForToastInvisibility();
                        }
                    }
                } catch (Exception | AssertionError e) {
                    tapEnterWeightManually();
                    scanItem(upcLabel, itemsList, testOutputData);
                    submitItemWeight(itemWeight);
                    harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                    Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.weighScreenUpcText()), sellByWeightItem);
                }
            }
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER))) {
            completePickRun();
        }
    }

    public void collapseTemperatureTrolleysSelecting(List<String> containerList) {
        scanFirstContainer(containerList.getFirst());
        for (String containerId : containerList) {
            scanEachItem();
            harvesterNativePage.enterBarcode(containerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        }
        completePickRun();
    }

    private void scanEachItem() {
        upcId = mobileCommands.getElementText(harvesterNativeSelectingMap.itemUpcText());
        harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
    }

    public HashMap<String, String> multiThreadedPclTrolleySelecting(String scenario, HashMap<String, String> testOutputData, String itemWeight, List<HashMap<String, String>> itemsList, HashMap<String, String> upcLabel, String trolleyName) {
        HashMap<String, String> overSizeTrolleyMap = new HashMap<>();
        if (testOutputData.containsKey(ExcelUtils.OVER_SIZED_CONTAINER) && testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).contains(Constants.PickCreation.OVERSIZE)) {
            overSizeTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER));
            for (Map.Entry<String, String> osContainer : overSizeTrolleyMap.entrySet()) {
                OversizeContainer = osContainer.getKey();
            }
        }
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        boolean pclOverSizeContainer;
        boolean overSized;
        String sellByWeightItem = getSellByWeightUpc(itemsList);
        boolean isLastItemHandled = false;
        boolean isOnlyOneItemInTrolley = remainingItemsInTrolley() == 1;
        do {
            pclOverSizeContainer = false;
            if (sellByWeightItem.isEmpty()) {
                if (testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
                    checkBagIconDuringPickingCommonTrolley(testOutputData.get(ExcelUtils.BAG_MAP));
                }
                overSized = scanItem(upcLabel, itemsList, testOutputData);
                if (overSized && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                    pclOverSizeContainer = true;
                }
                if (!overSized && (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.REJECT_BAG_FEES))) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
                    mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagsIcon());
                }
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DB_OS_REUSE_PCL_SPLIT_TROLLEY))) {
                    if (!mobileCommands.elementDisplayed(harvesterNativeSelectingMap.itemsToPick())) {
                        if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.getPclLabelFromUi())) {
                            pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[1];
                            harvesterNativePage.enterBarcode(pclContainerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                        }
                    }
                }
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toolTipIcon()) || mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toolTipForSelecting())) {
                        if (overSized && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                            String osContainerNumber = (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[0].trim();
                            List<String> multipleOverSizeTrolleyList = new ArrayList<>();
                            for (Map.Entry<String, String> osContainer : overSizeTrolleyMap.entrySet()) {
                                String container = osContainer.getKey();
                                String containerNumber = container.substring(container.length() - 2);
                                if (containerNumber.equals(osContainerNumber)) {
                                    multipleOverSizeTrolleyList.add(osContainer.getKey());
                                }
                            }
                            if (multipleOverSizeTrolleyList.size() == 1)
                                pclContainerId = multipleOverSizeTrolleyList.getFirst();
                            else {
                                for (String oversizeContainer : multipleOverSizeTrolleyList) {
                                    for (Map.Entry<String, String> trolleyMap : pclTrolleyMap.entrySet()) {
                                        if (trolleyName.equals(trolleyMap.getValue()) && oversizeContainer.equals(trolleyMap.getKey())) {
                                            pclContainerId = trolleyMap.getKey();
                                            break;
                                        }
                                    }
                                }
                            }

                        } else {
                            if (upcId.equals(upcLabel.get(Constants.PickCreation.OUT_OF_STOCK)) || upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY)) || upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY_OSS))) {
                                testOutputData.put(upcId, Constants.PickCreation.ITEM_FULFILLMENT_TYPE);
                            } else {
                                if (!overSized) {
                                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.getPclLabelFromUi())) {
                                        try {
                                            pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[1];
                                        } catch (Exception | AssertionError e) {
                                            pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.preWeighPclLabel())).split("- ")[1];
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                boolean substituteAndMove = performItemMovementDuringPickingFromHarvester(testOutputData, scenario, upcLabel);
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
                    if (!upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY)) && !(PermanentContainerLabelHelper.fromContainerId.equals(pclContainerId)) && !upcId.equals(upcLabel.get(Constants.PickCreation.OUT_OF_STOCK)) && !upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY_OSS))) {
                        if (!overSized && (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES))) && (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.REJECT_BAG_FEES))) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_ORDER_TROLLEY_BAG))) {
                            mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagsIcon());
                        }
                        if (!substituteAndMove) {
                            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.PICK_SERVICE_COUNTER_ITEM))) {
                                int totalItems = Integer.parseInt((mobileCommands.getElementText(harvesterNativeSelectingMap.itemsQtyText()).split(" "))[3]);
                                for (int i = 0; i < totalItems - 1; i++) {
                                    harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                                }
                                mobileCommands.tap(harvesterNativeSelectingMap.continueButton());
                            }
                        }
                        if (!pclOverSizeContainer) {
                            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.SC_ITEMS_MT_SCALE))) {
                                harvesterNativePage.enterBarcode(pclContainerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                            }
                        }
                    }
                }
            } else {
                selectMAFPItems(upcLabel, itemsList, testOutputData, itemWeight);
                if (upcIdForMAFP.equals(upcLabel.get(ExcelUtils.SELL_BY_WEIGHT_ITEM)) || upcIdForMAFP.equals(upcLabel.get(ExcelUtils.SELL_BY_UNIT)) || upcIdForMAFP.equals(upcLabel.get(ExcelUtils.IS_RANDOM_WEIGHT_ITEM)) || (upcLabel.containsKey(Constants.PickCreation.SHORTED) && upcLabel.get(Constants.PickCreation.SHORTED).contains(upcIdForMAFP))) {
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toolTipForSelecting())) {
                        pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[1];
                        harvesterNativePage.enterBarcode(pclContainerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                    }
                }
            }
            if (isOnlyOneItemInTrolley)
                break;
            if (isLastItemHandled)
                break;
            if (itemsInContainer() && remainingItemsInTrolley() == 1) {
                isLastItemHandled = true;
            }
        } while (isLastItemHandled || remainingItemsInTrolley() > 1);
        try {
            completePickRun();
        } catch (Exception |
                 AssertionError e) {
            mobileCommands.waitForElementClickability(harvesterNativeMap.enterButton());
            mobileCommands.tap(harvesterNativeMap.enterButton());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.goToSelectingButton());
            mobileCommands.tap(harvesterNativeSelectingMap.goToSelectingButton());
            if (mobileCommands.getElementText(harvesterNativeSelectingMap.screenTitle()).contains(TROLLEY_COMPLETE_TITLE)) {
                mobileCommands.tap(harvesterNativeSelectingMap.goToSelectingButton());
            }
        }
        return testOutputData;
    }

    private boolean performItemMovementDuringPickingFromHarvester(HashMap<String, String> testOutputData, String scenario, HashMap<String, String> upcLabel) {
        boolean substituteAndMove = false;
        if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT) && upcId.equals(upcLabel.get(Constants.PickCreation.SUBSTITUTE_ACCEPT))) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.SUBSTITUTION)) {
                substituteAndMove = true;
            }
        }
        if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT)) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && !moveItemsViaPicking && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.PICKING)) {
                moveItemsViaPicking = true;
            }
        }
        if (testOutputData.containsKey(ExcelUtils.ITEM_MOVEMENT) && upcId.endsWith("0001") || upcId.endsWith("00010")) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT)) && testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.PRE_WEIGHED)) {
                pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + mobileCommands.getElementText(harvesterNativeSelectingMap.preWeighPclLabel()).split("- ")[1];
                testOutputData = PermanentContainerLabelHelper.performItemMovementViaPreWeighed(scenario, testOutputData, pclContainerId);
            }
        }
        if (substituteAndMove) {
            PermanentContainerLabelHelper.performItemMovementViaSubstitution(scenario, testOutputData);
        }
        if (moveItemsViaPicking && !PermanentContainerLabelHelper.pickingMovementDone) {
            PermanentContainerLabelHelper.performItemMovementViaPicking(scenario, testOutputData, pclContainerId);
        }
        return substituteAndMove;

    }

    public void selectMAFPItems(HashMap<String, String> upcLabel, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData, String itemWeight) {
        Boolean overSized;
        try {
            scanItem(upcLabel, itemsList, testOutputData);
        } catch (Exception | AssertionError e) {
            tapEnterWeightManually();
            overSized = scanItem(upcLabel, itemsList, testOutputData);
            harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
            if (overSized) {
                pclContainerId = OversizeContainer;
            } else {
                pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[1];
            }
            harvesterNativePage.enterBarcode(pclContainerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
            submitItemWeight(itemWeight);
            harvesterNativePage.enterBarcode(pclContainerId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        }
    }

    public void multipleOrderTrolleySelecting(String containerId) {
        while (itemsInContainer()) {
            getUpcId();
            harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
            String containerNumber = mobileCommands.getElementText(harvesterNativeSelectingMap.containerNumberText()).split("-")[0].trim();
            String containerText = mobileCommands.getElementText(harvesterNativeSelectingMap.tempSkittleText());
            String tempSkittle = containerId.substring(1, containerId.length() - 2) + containerNumber;
            if (isMultipleOS) {
                harvesterNativePage.enterBarcode(containerId.substring(0, containerId.length() - 2) + containerNumber, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
            } else {
                if (containerText.equals(Constants.PickCreation.OVERSIZE)) {
                    harvesterNativePage.enterBarcode((Constants.PickCreation.OVERSIZE) + tempSkittle, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                } else {
                    harvesterNativePage.enterBarcode(Constants.PickCreation.NORMAL_CONTAINER + tempSkittle, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                }
            }
        }
        completePickRun();
    }

    public void singleThreadedTrolleySelecting(String itemWeight, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        String sellByWeightItem = getSellByWeightUpc(itemsList);
        if (sellByWeightItem.isEmpty()) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.MOVE_TO_SAME_ITEM_CONTAINER))) {
                scanItemSingleThreadedBasedOnContainer(itemsList, testOutputData);
            } else {
                scanItemSingleThreadedStore(itemsList, testOutputData);
            }
        } else {
            try {
                scanItemSingleThreadedStore(itemsList, testOutputData);
            } catch (AssertionError | Exception e) {
                tapEnterWeightManually();
                scanItemSingleThreadedStore(itemsList, testOutputData);
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.weighScreenUpcText()), sellByWeightItem);
                submitItemWeight(itemWeight);
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
            int weighItems = Integer.parseInt((mobileCommands.getElementAttribute(harvesterNativeSelectingMap.weightItemsText(), "content-desc").split(" "))[1]);
            for (int i = 0; i < weighItems; i++) {
                submitItemWeight(testOutputData.get(ExcelUtils.CIAO_COUPON_AMOUNT));
            }
        }
        completePickRun();
    }

    public void markAsUnavailable(String unavailableReason) {
        switch (unavailableReason) {
            case Constants.PickCreation.OUT_OF_STOCK:
                if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.substituteOoSButton())) {
                    mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.substituteOoSButton());
                    mobileCommands.tap(harvesterNativeSelectingMap.substituteOoSButton());
                }
                mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.markOutOfStockButton());
                mobileCommands.tap(harvesterNativeSelectingMap.markOutOfStockButton());
                break;
            case Constants.PickCreation.NOT_READY:
                try {
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.itemUnavailableButton())) {
                        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemUnavailableButton());
                        mobileCommands.tap(harvesterNativeSelectingMap.itemUnavailableButton());
                        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemNotReadyLayout());
                        mobileCommands.tap(harvesterNativeSelectingMap.itemNotReadyLayout());
                    }
                } catch (Exception e) {
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.orderIdText()))
                        break;
                }
                break;
            case Constants.PickCreation.NOT_READY_SUBS:
                mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemUnavailableButton());
                mobileCommands.tap(harvesterNativeSelectingMap.itemUnavailableButton());
                mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemNotReadySubstitution());
                mobileCommands.tap(harvesterNativeSelectingMap.itemNotReadySubstitution());
                break;
            case Constants.PickCreation.NOT_READY_OSS:
                mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemUnavailableButton());
                mobileCommands.tap(harvesterNativeSelectingMap.itemUnavailableButton());
                mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.itemNotReadyOOS());
                mobileCommands.tap(harvesterNativeSelectingMap.itemNotReadyOOS());
                mobileCommands.waitForElementClickability(harvesterNativeSelectingMap.markOutOfStockButton());
                mobileCommands.tap(harvesterNativeSelectingMap.markOutOfStockButton());
                waitForToastInvisibility();
                break;
        }
    }

    public void substituteItem(List<HashMap<String, String>> itemList, HashMap<String, String> testOutputData) {
        String scenario = testOutputData.get(ExcelUtils.SCENARIO);
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.NOT_READY_SUBSTITUTION))) {
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.substituteOoSButton())) {
                mobileCommands.tap(harvesterNativeSelectingMap.substituteOoSButton());
            }
        }
        for (HashMap<String, String> itemDetails : itemList) {
            if (itemDetails.get(ExcelUtils.ITEM_UPC).equalsIgnoreCase(upcId)) {
                String substituteItems = itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC);
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NOT_READY_SUBSTITUTION))) {
                    updateSubstitutionUpcForOSContainers(testOutputData, substituteItems);
                }
                if (substituteItems.contains(",")) {
                    testOutputData.put(ExcelUtils.MULTIPLE_SUBS, String.valueOf(true));
                    ExcelUtils.writeToExcel(scenario, testOutputData);
                    String[] substituteList = substituteItems.split(",");
                    for (String subItems : substituteList) {
                        scanSubstitutedItem(subItems);
                    }
                } else {
                    scanSubstitutedItem(itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC));
                }
                if (itemDetails.get(ExcelUtils.FULFILLMENT_TYPE).equalsIgnoreCase(Constants.PickCreation.SUBSTITUTE_SHORT)) {
                    mobileCommands.tap(harvesterNativeSelectingMap.qtyMinusButton());
                }
            }
        }

        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE)) && mobileCommands.elementDisplayed(harvesterNativeSelectingMap.doneSubstitutionButton())) {
            mobileCommands.tap(harvesterNativeSelectingMap.doneSubstitutionButton());
        }
    }

    private void scanSubstitutedItem(String subItems) {
        boolean isNoSuggestionAvailable = mobileCommands.elementDisplayed(harvesterNativeSelectingMap.noSuggestionAvailableText());
        harvesterNativePage.enterBarcode(subItems.trim(), Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        if (!isNoSuggestionAvailable && mobileCommands.elementDisplayed(harvesterNativeSelectingMap.confirmButton())) {
            mobileCommands.tap(harvesterNativeSelectingMap.confirmButton());
        }
    }

    private static void updateSubstitutionUpcForOSContainers(HashMap<String, String> testOutputData, String substituteItems) {
        String val = "";
        HashMap<String, String> overSizeUpc = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTIPLE_OS_UPC_TEMP));
        for (Map.Entry<String, String> value : overSizeUpc.entrySet()) {
            val = value.getKey();
        }
        String upcVal = overSizeUpc.get(val);
        overSizeUpc.remove(val);
        overSizeUpc.put(substituteItems, upcVal);
        testOutputData.put(ExcelUtils.MULTIPLE_OS_UPC_TEMP, String.valueOf(overSizeUpc));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
    }

    public void shortRemainingItems(String upcId) {
        harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.quantityDialogEnterButton());
        mobileCommands.tap(harvesterNativeSelectingMap.quantityDialogEnterButton());
        mobileCommands.click(harvesterNativeSelectingMap.quantityDialogToFill());
        mobileCommands.tap(harvesterNativeSelectingMap.shortRemainingItemsMenuOption());
    }


    public Boolean scanItem(HashMap<String, String> upcLabel, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        boolean isOverSized = false;
        getUpcId();
        boolean isMafpItem = false;
        if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.mafpItemQuantity())) {
            itemQuantity = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.mafpItemQuantity()));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
            if (upcId.endsWith("0000")) {
                upcId = upcId.substring(0, upcId.length() - 1) + "1";
                isMafpItem = true;
            }
            if (!upcLabel.containsKey(Constants.PickCreation.NOT_READY) && (upcLabel.get(ExcelUtils.SELL_BY_WEIGHT_ITEM).contains(upcIdForMAFP) || upcLabel.get(ExcelUtils.IS_RANDOM_WEIGHT_ITEM).contains(upcIdForMAFP) || upcLabel.get(ExcelUtils.SELL_BY_UNIT).contains(upcIdForMAFP)) || (upcLabel.containsKey(Constants.PickCreation.SHORTED) && upcLabel.get(Constants.PickCreation.SHORTED).contains(upcIdForMAFP))) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.MAFP_NOT_READY_FOR_ORDER_ADJUSTMENT_WEIGHT))) {
                    markAsUnavailable(Constants.PickCreation.NOT_READY);
                } else {
                    harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                    serviceCounterSellByWeightOrUnit(upcLabel, upcId);
                }
                return false;
            }
        }
        if (upcId.endsWith("0000") && !upcLabel.containsKey(Constants.PickCreation.NOT_READY)) {
            upcId = upcId.substring(0, upcId.length() - 1) + "1";
        }
        if (upcLabel.containsKey((Constants.PickCreation.NOT_READY_OSS))) {
            upcId = upcId.replace("0001", "0000");
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OS_DIFFERENT_UPC))) {
            if (testOutputData.containsKey(ExcelUtils.OVER_SIZED_CONTAINER) && testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).contains(Constants.PickCreation.OVERSIZE)) {
                HashMap<String, String> overSizeTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER));
                for (Map.Entry<String, String> osContainer : overSizeTrolleyMap.entrySet()) {
                    multipleOverSizeUpcList = new ArrayList<>(overSizeTrolleyMap.values());
                    if (overSizeTrolleyMap.size() == 1) {
                        if (upcId.equalsIgnoreCase(osContainer.getValue())) {
                            isOverSized = true;
                            break;
                        }
                    } else {
                        if (multipleOverSizeUpcList.contains(upcId)) {
                            isOverSized = true;
                        }
                    }
                }
            }
        }

        if (upcLabel.containsKey(Constants.PickCreation.OUT_OF_STOCK) && upcLabel.get(Constants.PickCreation.OUT_OF_STOCK).contains(upcId)) {
            markAsUnavailable(Constants.PickCreation.OUT_OF_STOCK);
        } else if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP)) && upcLabel.containsKey(Constants.PickCreation.NOT_READY) && upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY))) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NOT_READY_SUBSTITUTION))) {
                markAsUnavailable(Constants.PickCreation.NOT_READY_SUBS);
                substituteItem(itemsList, testOutputData);
            } else {
                markAsUnavailable(Constants.PickCreation.NOT_READY);
            }
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.SC_ITEMS_MT_SCALE)) && upcLabel.get(Constants.PickCreation.NOT_READY).contains(upcId)) {
            markAsUnavailable(Constants.PickCreation.NOT_READY);
        } else if (upcLabel.containsKey(Constants.PickCreation.NOT_READY_OSS) && upcLabel.get(Constants.PickCreation.NOT_READY_OSS).contains(upcId)) {
            markAsUnavailable(Constants.PickCreation.NOT_READY_OSS);
        } else if (upcLabel.containsKey(Constants.PickCreation.SHORTED) && upcId.equals(upcLabel.get(Constants.PickCreation.SHORTED))) {
            shortRemainingItems(upcId);
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER)) && upcLabel.containsKey(Constants.PickCreation.SUBSTITUTE_REJECT) && upcLabel.get(Constants.PickCreation.SUBSTITUTE_REJECT).contains(upcId)) {
            substituteItem(itemsList, testOutputData);
        } else if ((upcLabel.containsKey(Constants.PickCreation.SUBSTITUTE_ACCEPT) || upcLabel.containsKey(Constants.PickCreation.SUBSTITUTE_REJECT) || upcLabel.containsKey(Constants.PickCreation.SUBSTITUTE_SHORT)) && upcId.equals(upcLabel.get(Constants.PickCreation.SUBSTITUTE_ACCEPT)) || upcId.equals(upcLabel.get(Constants.PickCreation.SUBSTITUTE_REJECT)) || upcId.equals(upcLabel.get(Constants.PickCreation.SUBSTITUTE_SHORT))) {
            substituteItem(itemsList, testOutputData);
        } else if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
            mafpItemQuantity = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.mafpItemQuantity()));
            harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        } else if (!isMafpItem) {
            harvesterNativePage.enterBarcode(upcId, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
        }
        return isOverSized;
    }

    public void serviceCounterSellByWeightOrUnit(HashMap<String, String> upcLabel, String upcId) {
        clickAllBarcodesScannedButton();
        if (upcLabel.containsKey(Constants.PickCreation.SHORTED) && (upcLabel.get(Constants.PickCreation.SHORTED).contains(upcId)) || upcLabel.get(Constants.PickCreation.SHORTED).contains(upcId.substring(0, upcId.length() - 1) + "0")) {
            selectPartialFulfill();
        } else {
            selectMarkAsFullyFilled();
        }
    }

    public void clickAllBarcodesScannedButton() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.scannedBarcodesButton());
        mobileCommands.tap(harvesterNativeSelectingMap.scannedBarcodesButton());
    }

    public void selectMarkAsFullyFilled() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.markAsFullySelected());
        mobileCommands.tap(harvesterNativeSelectingMap.markAsFullySelected());
    }

    public void selectPartialFulfill() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.partialFulfillButton());
        mobileCommands.tap(harvesterNativeSelectingMap.partialFulfillButton());
    }

    public void fulfillPreWeighItem() {
        clickAllBarcodesScannedButton();
        selectMarkAsFullyFilled();
    }


    public void scanItemSingleThreadedStore(List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        HashMap<String, Integer> upcQuantityMap = getUpcQuantityMap(itemsList);
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
            tapEnterWeightManually();
        }
        boolean isLastItemHandled = false;
        boolean isOnlyOneItemInTrolley = remainingItemsInTrolley() == 1;
        do {
            scanItem(upcLabel, itemsList, testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MAFP))) {
                if (upcId.endsWith("0001")) {
                    upcId = upcId.substring(0, upcId.length() - 1) + "0";
                }
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                if (itemQuantity > 1) {
                    mobileCommands.tap(harvesterNativeSelectingMap.completeButton());
                }
            } else {
                if (upcQuantityMap.size() > 1) {
                    if (itemQuantity > 1 && mobileCommands.elementDisplayed(harvesterNativeSelectingMap.completeButton())) {
                        mobileCommands.tap(harvesterNativeSelectingMap.completeButton());
                    }
                }
            }
            if (isLastItemHandled)
                break;
            if (isOnlyOneItemInTrolley)
                break;
            if (remainingItemsInTrolley() == 1) {
                isLastItemHandled = true;
            }
        } while ((isLastItemHandled || remainingItemsInTrolley() > 1));
    }

    public void scanItemSingleThreadedBasedOnContainer(List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        HashMap<String, Integer> upcQuantityMap = getUpcQuantityMap(itemsList);
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        for (int i = 0; i < ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)).size(); i++) {
            scanItem(upcLabel, itemsList, testOutputData);
            if (upcQuantityMap.get(this.upcId) > 1) {
                mobileCommands.tap(harvesterNativeSelectingMap.completeButton());
            }
            harvesterNativePage.verifyToastMessage(ITEM_FULFILLED_TOAST);
        }
    }

    public String getSellByWeightUpc(List<HashMap<String, String>> itemsList) {
        String sellByWeightItem = "";
        for (HashMap<String, String> itemMap : itemsList) {
            if (Boolean.parseBoolean(itemMap.get(ExcelUtils.IS_RANDOM_WEIGHT)) || Boolean.parseBoolean(itemMap.get(ExcelUtils.IS_RANDOM_WEIGHT_ITEM)) || Boolean.parseBoolean(itemMap.get(ExcelUtils.SELL_BY_UNIT)) || Boolean.parseBoolean(itemMap.get(ExcelUtils.SELL_BY_WEIGHT_ITEM))) {
                sellByWeightItem = itemMap.get(ExcelUtils.ITEM_UPC);
            }
        }
        return sellByWeightItem;
    }

    public HashMap<String, String> getItemsWithLabelsFromTestData(List<HashMap<String, String>> itemsList) {
        HashMap<String, String> upcLabel = new HashMap<>();
        List<String> multipleOrderOutOfStock = new ArrayList<>();
        List<String> multipleOrderSubstituteReject = new ArrayList<>();
        List<String> multipleNotReady = new ArrayList<>();
        for (HashMap<String, String> itemDetails : itemsList) {
            if (itemDetails.containsKey(ExcelUtils.IS_RANDOM_WEIGHT)) {
                if (Boolean.parseBoolean(String.valueOf(itemDetails.get(ExcelUtils.IS_RANDOM_WEIGHT).equals("true")))) {
                    upcLabel.put(Constants.PickCreation.SELL_BY_WEIGHT, itemDetails.get(ExcelUtils.ITEM_UPC));
                }
            }
            if (itemDetails.containsKey(ExcelUtils.IS_RANDOM_WEIGHT_ITEM)) {
                if (Boolean.parseBoolean(String.valueOf(itemDetails.get(ExcelUtils.IS_RANDOM_WEIGHT_ITEM).equals("true")))) {
                    upcLabel.put(ExcelUtils.IS_RANDOM_WEIGHT_ITEM, itemDetails.get(ExcelUtils.ITEM_UPC));
                }
            }
            if (itemDetails.containsKey(ExcelUtils.SELL_BY_UNIT)) {
                if (Boolean.parseBoolean(String.valueOf(itemDetails.get(ExcelUtils.SELL_BY_UNIT).equals("true")))) {
                    upcLabel.put(ExcelUtils.SELL_BY_UNIT, itemDetails.get(ExcelUtils.ITEM_UPC));
                }
            }
            if (itemDetails.containsKey(ExcelUtils.SELL_BY_WEIGHT_ITEM)) {
                if (Boolean.parseBoolean(String.valueOf(itemDetails.get(ExcelUtils.SELL_BY_WEIGHT_ITEM).equals("true")))) {
                    upcLabel.put(ExcelUtils.SELL_BY_WEIGHT_ITEM, itemDetails.get(ExcelUtils.ITEM_UPC));
                }
            }
            if (itemDetails.containsKey(ExcelUtils.PRE_WEIGH_PKG_COUNT) && !itemDetails.get(ExcelUtils.PRE_WEIGH_PKG_COUNT).isEmpty()) {
                upcLabel.put(ExcelUtils.PRE_WEIGH_PKG_COUNT, itemDetails.get(ExcelUtils.PRE_WEIGH_PKG_COUNT));
            }
            if (itemDetails.containsKey(ExcelUtils.MAFP_PACKAGE_COUNT) && !itemDetails.get(ExcelUtils.MAFP_PACKAGE_COUNT).isEmpty()) {
                upcLabel.put(ExcelUtils.MAFP_PACKAGE_COUNT, itemDetails.get(ExcelUtils.MAFP_PACKAGE_COUNT));
            }
            if (itemDetails.containsKey(ExcelUtils.MAFP_PACKAGE_COUNT_SELL_BY_UNIT) && !itemDetails.get(ExcelUtils.MAFP_PACKAGE_COUNT_SELL_BY_UNIT).isEmpty()) {
                upcLabel.put(ExcelUtils.MAFP_PACKAGE_COUNT_SELL_BY_UNIT, itemDetails.get(ExcelUtils.MAFP_PACKAGE_COUNT_SELL_BY_UNIT));
            }
            if (itemDetails.containsKey(ExcelUtils.SUBSTITUTED_ITEM_UPC) && !itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC).isEmpty()) {
                upcLabel.put(ExcelUtils.SUBSTITUTED_ITEM_UPC, itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC));
            }
            if (itemDetails.containsKey(ExcelUtils.FULFILLMENT_TYPE)) {
                switch ((itemDetails.get(ExcelUtils.FULFILLMENT_TYPE))) {
                    case Constants.PickCreation.OUT_OF_STOCK:
                        if (!multipleOrderOutOfStock.contains(itemDetails.get(ExcelUtils.ITEM_UPC))) {
                            multipleOrderOutOfStock.add(itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        if (multipleOrderOutOfStock.size() > 1) {
                            upcLabel.put(Constants.PickCreation.OUT_OF_STOCK, String.valueOf(multipleOrderOutOfStock));
                        } else {
                            upcLabel.put(Constants.PickCreation.OUT_OF_STOCK, itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        break;
                    case Constants.PickCreation.SHORTED:
                        upcLabel.put(Constants.PickCreation.SHORTED, itemDetails.get(ExcelUtils.ITEM_UPC));
                        break;
                    case Constants.PickCreation.SUBSTITUTE_ACCEPT:
                        upcLabel.put(Constants.PickCreation.SUBSTITUTE_ACCEPT, itemDetails.get(ExcelUtils.ITEM_UPC));
                        break;
                    case Constants.PickCreation.SUBSTITUTE_REJECT:
                        if (!multipleOrderSubstituteReject.contains(itemDetails.get(ExcelUtils.ITEM_UPC))) {
                            multipleOrderSubstituteReject.add(itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        if (multipleOrderSubstituteReject.size() > 1) {
                            upcLabel.put(Constants.PickCreation.SUBSTITUTE_REJECT, String.valueOf(multipleOrderSubstituteReject));
                        } else {
                            upcLabel.put(Constants.PickCreation.SUBSTITUTE_REJECT, itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        break;
                    case Constants.PickCreation.SUBSTITUTE_SHORT:
                        upcLabel.put(Constants.PickCreation.SUBSTITUTE_SHORT, itemDetails.get(ExcelUtils.ITEM_UPC));
                        break;
                    case Constants.PickCreation.NOT_READY:
                        if (!multipleNotReady.contains(itemDetails.get(ExcelUtils.ITEM_UPC))) {
                            multipleNotReady.add(itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        if (multipleNotReady.size() > 1) {
                            upcLabel.put(Constants.PickCreation.NOT_READY, String.valueOf(multipleNotReady));
                        } else {
                            upcLabel.put(Constants.PickCreation.NOT_READY, itemDetails.get(ExcelUtils.ITEM_UPC));
                        }
                        break;
                    case Constants.PickCreation.NOT_READY_OSS:
                        upcLabel.put(Constants.PickCreation.NOT_READY_OSS, itemDetails.get(ExcelUtils.ITEM_UPC));
                        break;
                }
            }
        }
        return upcLabel;
    }

    public void tapEnterWeightManually() {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.toolTipIcon());
        mobileCommands.tap(harvesterNativeSelectingMap.toolTipIcon());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.enterWeightManuallyOption());
        mobileCommands.tap(harvesterNativeSelectingMap.enterWeightManuallyOption());
    }

    public void submitItemWeight(String itemWeight) {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.inputField());
        mobileCommands.enterText(harvesterNativeMap.inputField(), itemWeight, true);
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.submitWeightButton());
        mobileCommands.tap(harvesterNativeSelectingMap.submitWeightButton());
    }

    public void completePickRun() {
        mobileCommands.wait(3);
        mobileCommands.waitForElementClickability(harvesterNativeSelectingMap.completeRunButton());
        mobileCommands.tap(harvesterNativeSelectingMap.completeRunButton());
        try {
            mobileCommands.waitForElementClickability(harvesterNativeMap.enterButton());
            mobileCommands.tap(harvesterNativeMap.enterButton());
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.goToSelectingButton());
        } catch (Exception e) {
            mobileCommands.waitForElementClickability(harvesterNativeMap.enterButton());
            mobileCommands.tap(harvesterNativeMap.enterButton());
        }
        mobileCommands.tap(harvesterNativeSelectingMap.goToSelectingButton());
        if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.screenTitle())) {
            if (mobileCommands.getElementText(harvesterNativeSelectingMap.screenTitle()).contains(TROLLEY_COMPLETE_TITLE)) {
                mobileCommands.tap(harvesterNativeSelectingMap.goToSelectingButton());
            }
        }
    }

    public void getNumberOfContainersInTrolleyThroughApi(HashMap<String, String> testOutputData, String trolleyId) {
        String path = "data.pick-lists.";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = df.format(new Date());
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("filter.locationId", testOutputData.get(ExcelUtils.STORE_ID));
        queryParams.put("filter.pickupDate", formatted);
        Response response = new ApiUtils().getApi(null, null, null, PropertyUtils.getPickRunsHeaderEndpoint(), new HashMap<>(), queryParams);
        Assert.assertEquals(response.getStatusCode(), 200, "Pick run api call is not successful");
        JsonPath jsonPath = new JsonPath(response.asString());
        for (int i = 0; i < jsonPath.getList(path).size(); i++) {
            if (((HashMap<?, ?>) jsonPath.getList(path).get(i)).get(TROLLEY_NAME).equals(trolleyId)) {
                JsonPath jsonPathChild = new JsonPath(new ApiUtils().convertObjectToString(jsonPath.getList(path).get(i)));
                Map<String, String> apiItemDetails = jsonPathChild.getMap(ADDITIONAL_ATTRIBUTES);
                numberOfContainerInTrolley = Integer.parseInt(apiItemDetails.get(CONTAINER_COUNT));
            }
        }
    }

    public void getNumberOfContainersInTrolleyThroughUi(String trolleyId) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        try {
            numberOfContainerInTrolley = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.getContainerCountInTrolley(trolleyId)).split("containers")[0].replaceAll(" ", ""));
        } catch (Exception e) {
            actions.sendKeys(Keys.DOWN).build().perform();
            actions.sendKeys(Keys.DOWN).build().perform();
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.getContainerCountInTrolley(trolleyId))) {
                numberOfContainerInTrolley = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.getContainerCountInTrolley(trolleyId)).split("containers")[0].replaceAll(" ", ""));
            } else {
                for (int i = 1; i <= 5; i++) {
                    actions.sendKeys(Keys.UP).build().perform();
                    if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.getContainerCountInTrolley(trolleyId))) {
                        numberOfContainerInTrolley = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.getContainerCountInTrolley(trolleyId)).split("containers")[0].replaceAll(" ", ""));
                        break;
                    }
                }
            }
        }
    }

    public void getTemperatureTypeAndScanContainers(HashMap<String, String> testData) {
        HashMap<String, String> containerTemperatureMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        if (testData.containsKey(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS) && Boolean.parseBoolean(testData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
            if (Boolean.parseBoolean(testData.get(ExcelUtils.IS_PCL_OVERSIZE)) && Boolean.parseBoolean(testData.get(ExcelUtils.ITEM_MOVEMENT))) {
                containerTemperatureMap = updateMapsForPclOsMultipleTrolley(testData, containerTemperatureMap);
            }
        }
        if (testData.containsKey(ExcelUtils.CANCEL_TROLLEY_PCL_LABEL_TEMPERATURE_MAP)) {
            boolean isMultiOrderLastData = Boolean.parseBoolean(testData.get(ExcelUtils.MULTIPLE_ORDER_CANCEL_SCENARIO));
            if (isMultiOrderLastData) {
                containerTemperatureMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.CANCEL_TROLLEY_PCL_LABEL_TEMPERATURE_MAP));
            }
        }
        int totalContainers = mobileCommands.numberOfElements(harvesterNativeMap.getTotalContainer());
        int count = 0;
        for (int i = 1; i <= totalContainers; i++) {
            if (count == totalContainers) {
                break;
            }
            for (Map.Entry<String, String> multiContainer : containerTemperatureMap.entrySet()) {
                if (testData.containsKey(ExcelUtils.SINGLE_TROLLEY)) {
                    if (!mobileCommands.elementDisplayed(harvesterNativeMap.getContainerText(i)))
                        break;
                }
                if (!pclAssignedContainers.contains(multiContainer.getKey())) {
                    if (mobileCommands.getElementText(harvesterNativeMap.getContainerText(i)).equals(A)) {
                        if (multiContainer.getValue().equals(Constants.PickCreation.AMBIENT) && !multiContainer.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                            if (scanPclLabel(multiContainer.getKey(), testData)) {
                                pclAssignedContainers.add(multiContainer.getKey());
                            }
                            count++;
                        }
                    }
                }
                if (mobileCommands.getElementText(harvesterNativeMap.getContainerText(i)).equals(R)) {
                    if (!pclAssignedContainers.contains(multiContainer.getKey())) {
                        if (multiContainer.getValue().equals(Constants.PickCreation.REFRIGERATED) && !multiContainer.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                            if (scanPclLabel(multiContainer.getKey(), testData)) {
                                pclAssignedContainers.add(multiContainer.getKey());
                            }
                            count++;
                        }
                    }
                }
                if (mobileCommands.getElementText(harvesterNativeMap.getContainerText(i)).equals(F)) {
                    if (!pclAssignedContainers.contains(multiContainer.getKey())) {
                        if (multiContainer.getValue().equals(Constants.PickCreation.FROZEN_TEMP) && !multiContainer.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                            if (scanPclLabel(multiContainer.getKey(), testData)) {
                                pclAssignedContainers.add(multiContainer.getKey());
                            }
                            count++;
                        }
                    }
                }
                if (mobileCommands.getElementText(harvesterNativeMap.getContainerText(i)).equals(Constants.PickCreation.OVERSIZE)) {
                    if (!pclAssignedContainers.contains(multiContainer.getKey())) {
                        if (multiContainer.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                            if (scanPclLabel(multiContainer.getKey(), testData)) {
                                pclAssignedContainers.add(multiContainer.getKey());
                            }
                            if ((Boolean.parseBoolean(testData.get(ExcelUtils.IS_ALL_UPCS_AND_TEMP_TYPES)) && Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) || Boolean.parseBoolean(testData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
                                break;
                            }
                            count++;
                        }
                    }
                }
            }
        }
        if (!Boolean.parseBoolean(testData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            int osCount = mobileCommands.numberOfElements(harvesterNativeMap.getTotalSkittleTemperatureCount());
            for (int i = 1; i <= osCount; i++) {
                if (mobileCommands.getElementText(harvesterNativeMap.getSkittleTemperatureText(i)).contains(Constants.PickCreation.OVERSIZE)) {
                    harvesterNativePage.printOversizeTrolleyLabels();
                }
            }
        }
    }


    public HashMap<String, String> updateMapsForPclOsMultipleTrolley(HashMap<String, String> testOutputData, HashMap<String, String> pclLabelTemperatureMap) {
        HashMap<String, String> pclLabelTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> pclIDTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        String updatedContainerTemperature = "";
        String updatedPclLabel = "";
        for (Map.Entry<String, String> pclLabel : pclLabelTrolleyMap.entrySet()) {
            updatedPclLabel = pclLabel.getKey();
            break;
        }
        for (Map.Entry<String, String> temperature : pclLabelTemperatureMap.entrySet()) {
            updatedContainerTemperature = temperature.getValue();
            break;
        }
        pclLabelTemperatureMap.put(updatedPclLabel, updatedContainerTemperature);
        pclIDTemperatureMap.put(updatedPclLabel.substring(6), updatedContainerTemperature);
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclLabelTemperatureMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIDTemperatureMap));
        ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        return pclLabelTemperatureMap;
    }

    public void moveFromOneContainerToAnotherThroughSubs(String scenario, String itemMovementFrom, String itemMovementTo, String fromContainerTemp, HashMap<String, String> pclTemperatureContainerMap) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testOutputData = new HashMap<>(testData);
        HashMap<String, String> pclIdTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        moveItemsThroughSubs();
        if (testOutputData.get(ExcelUtils.ITEM_MOVEMENT_VIA_SCREEN).equalsIgnoreCase(ExcelUtils.SUBSTITUTION) && (PermanentContainerLabelHelper.typeOfContainer.equals(ExcelUtils.EXISTING))) {
            itemMovementTo = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + mobileCommands.getElementText(harvesterNativeSelectingMap.existingContainer()).substring(5);
        }
        harvesterNativePage.enterBarcode(itemMovementTo, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
        if (itemMovementTo.contains(Constants.PickCreation.OVERSIZE)) {
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.toastMessage()), Constants.PickCreation.OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE);
            waitForToastInvisibility();
        } else {
            if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.toastMessage()))
                Assert.assertEquals(mobileCommands.getElementText(harvesterNativeSelectingMap.toastMessage()), ITEM_SUBSTITUTED_TOAST);
            waitForToastInvisibility();
            if (!pclTemperatureContainerMap.containsKey(itemMovementTo)) {
                pclTemperatureContainerMap.put(itemMovementTo, fromContainerTemp);
                pclIdTemperatureMap.put((itemMovementTo.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1]), fromContainerTemp);
            }
            if (!testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES).contains(itemMovementFrom)) {
                pclTemperatureContainerMap.remove(itemMovementFrom);
                pclIdTemperatureMap.remove((itemMovementFrom.split(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID))[1]));
            }
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureContainerMap));
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        }
    }

    public void moveItemsThroughSubs() {
        mobileCommands.tap(harvesterNativeSelectingMap.kebabIconSubstitution());
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.moveToContainerSubstitution());
        mobileCommands.tap(harvesterNativeSelectingMap.moveToContainerSubstitution());
        if (PermanentContainerLabelHelper.typeOfContainer.equals(ExcelUtils.EXISTING)) {
            mobileCommands.tap(harvesterNativeSelectingMap.existingContainer());
        } else {
            mobileCommands.tap(harvesterNativeSelectingMap.addNewButton());
        }
    }

    public HashMap<String, HashMap<String, String>> generateMaps(HashMap<String, String> testOutputData) {
        HashMap<String, HashMap<String, String>> maps = new HashMap<>();
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        HashMap<String, String> pclTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        HashMap<String, String> pclIdMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        maps.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, pclTrolleyMap);
        maps.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, pclTemperatureMap);
        maps.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, pclIdMap);
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        return maps;
    }

    public void takeOverTrolleyInAssigning(String scenario, HashMap<String, String> testOutputData, List<HashMap<String, String>> itemsList) {
        String pclTakeOverTrolley;
        String ContainerToRemove = "";
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        HashMap<String, HashMap<String, String>> maps = generateMaps(testOutputData);
        if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
            multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        }
        for (Map.Entry<String, String> pclLabelMap : maps.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP).entrySet()) {
            if (!takeOverTrolleys.contains(pclLabelMap.getValue())) {
                pclTakeOverTrolley = getTrolley(testOutputData, pclLabelMap);
                if (numberOfContainerInTrolley > 1) {
                    takeOverTrolleys.add(pclLabelMap.getValue());
                    tapStartButton(pclLabelMap.getValue());
                    assignPclLabels(testOutputData, pclLabelMap);
                    harvesterNativePage.startRunButton();
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                        harvesterNativePage.verifyToastMessage(UPDATE_PCL_TOAST_SINGLE_THREAD);
                    } else {
                        harvesterNativePage.verifyToastMessage(UPDATE_PCL_TOAST);
                    }
                    mobileCommands.pressBack();
                    mobileCommands.tap(harvesterNativeSelectingMap.yesExitButton());
                    ContainerToRemove = pclLabelMap.getKey();
                } else {
                    tapStartButton(pclLabelMap.getValue());
                    scanPclLabel(pclLabelMap.getKey(), testOutputData);
                    harvesterNativePage.startRunButton();
                    if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING))) {
                        multiThreadedPclTrolleySelecting(scenario, testOutputData, testOutputData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, pclLabelMap.getValue());
                    }
                }
                testOutputData.put(ExcelUtils.TAKE_OVER_TROLLEY, pclTakeOverTrolley);
                ExcelUtils.writeToExcel(scenario, testOutputData);
            }
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING))) {
            updateMapsForPclOsTakeOver(testOutputData, maps.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP), maps.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP), maps.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP), ContainerToRemove);
        }
    }

    public void updateMapsForPclOsTakeOver(HashMap<String, String> testOutputData, HashMap<String, String> pclTrolleyMap, HashMap<String, String> pclTemperatureMap, HashMap<String, String> pclIdMap, String containerToRemove) {
        pclTemperatureMap.remove(containerToRemove);
        pclTrolleyMap.remove(containerToRemove);
        pclIdMap.remove(containerToRemove.substring(6));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdMap));
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(pclTrolleyMap));
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclTemperatureMap));
    }

    public void takeOverTrolleyInAssigningSingleThread(String scenario, HashMap<String, String> testOutputData, List<HashMap<String, String>> itemsList) {
        String pclTakeOverTrolley;
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        multiContainerTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));  //**      1563-1589, 1623-1648*
        HashMap<String, HashMap<String, String>> maps = generateMaps(testOutputData);
        for (Map.Entry<String, String> pclLabelMap : maps.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP).entrySet()) {
            if (!takeOverTrolleys.contains(pclLabelMap.getValue())) {
                pclTakeOverTrolley = getTrolley(testOutputData, pclLabelMap);
                if (numberOfContainerInTrolley > 1) {
                    tapStartButton(pclLabelMap.getValue());
                    takeOverTrolleys.add(pclLabelMap.getValue());
                    assignPclLabels(testOutputData, pclLabelMap);
                    harvesterNativePage.startRunButton();
                    harvesterNativePage.verifyToastMessage(UPDATE_PCL_TOAST);
                    mobileCommands.pressBack();
                    mobileCommands.tap(harvesterNativeSelectingMap.yesExitButton());
                    testOutputData.put(ExcelUtils.TAKE_OVER_TROLLEY, pclTakeOverTrolley);
                    ExcelUtils.writeToExcel(scenario, testOutputData);
                } else {
                    tapStartButton(pclLabelMap.getValue());
                    scanPclLabel(pclLabelMap.getKey(), testOutputData);
                    harvesterNativePage.startRunButton();
                    multiThreadedPclTrolleySelecting(scenario, testOutputData, testOutputData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, pclLabelMap.getValue());
                }
            }
        }
        testOutputData.put(ExcelUtils.PCL_LABEL_TROLLEY_MAP, String.valueOf(maps.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP)));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(maps.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP)));
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(maps.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
    }

    private void assignPclLabels(HashMap<String, String> testOutputData, Map.Entry<String, String> pclLabelMap) {
        for (Map.Entry<String, String> multiContainer : multiContainerTrolley.entrySet()) {
            if (multiContainer.getValue().equals(pclLabelMap.getValue())) {
                getTemperatureTypeAndScanContainers(testOutputData);
                break;
            }
        }
    }

    private String getTrolley(HashMap<String, String> testOutputData, Map.Entry<String, String> pclLabelMap) {
        String pclTakeOverTrolley;
        takeOverTrolleys.add(pclLabelMap.getValue());
        pclTakeOverTrolley = pclLabelMap.getValue();
        moveToPclTrolley(pclLabelMap.getValue());
        try {
            getNumberOfContainersInTrolleyThroughApi(testOutputData, pclLabelMap.getValue());
        } catch (Exception | AssertionError e) {
            getNumberOfContainersInTrolleyThroughUi(pclLabelMap.getValue());
        }
        return pclTakeOverTrolley;
    }

    public void verifyTakeOverHarvesterSelectingPcl(String scenario, HashMap<String, String> testData, List<HashMap<String, String>> itemsList, String status) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        if (testData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
            multiContainerTrolley = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
        } else {
            multiContainerTrolley = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        }
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        for (Map.Entry<String, String> takeOverTrolleyLabelMap : multiContainerTrolley.entrySet()) {
            if (!scannedTrolleys.contains(takeOverTrolleyLabelMap.getValue())) {
                scannedTrolleys.add(takeOverTrolleyLabelMap.getValue());
                harvesterNativePage.clickInProgressTrolleysButton();
                moveToInProgressTrolley(takeOverTrolleyLabelMap.getValue());
                Assert.assertEquals(harvesterNativePage.getProgressTagText(takeOverTrolleyLabelMap.getValue()), status);
                harvesterNativePage.clickInProgressTrolleyMenuButton(takeOverTrolleyLabelMap.getValue());
                Assert.assertEquals(harvesterNativePage.getTakeOverRunLabelText(), TAKE_OVER_RUN);
                harvesterNativePage.clickTakeOverRunButton();
                mobileCommands.tap(harvesterNativeContainerLookupMap.continueButton());
                scannedTrolleys.add(takeOverTrolleyLabelMap.getValue());
                for (Map.Entry<String, String> multiContainer : multiContainerTrolley.entrySet()) {
                    if (multiContainer.getValue().equals(takeOverTrolleyLabelMap.getValue())) {
                        getTemperatureTypeAndScanContainers(testData);
                        break;
                    }
                }
                if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                    singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                } else {
                    harvesterNativePage.startRunButton();
                    multiThreadedPclTrolleySelecting(scenario, testData, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, takeOverTrolleyLabelMap.getValue());
                }
                if (!Boolean.parseBoolean(testData.get(ExcelUtils.IS_PCL_OVERSIZE_TAKE_OVER_ASSIGNING))) {
                    harvesterNativePage.startRunButton();
                }
            }
        }
    }

    public void verifyTakeoverHarvesterSelectingSingleThreadPcl(String scenario, HashMap<String, String> testData, List<HashMap<String, String>> itemsList) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        HashMap<String, String> pclTrolleyMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.selecting());
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        for (Map.Entry<String, String> takeOverTrolleyLabelMap : pclTrolleyMap.entrySet()) {
            if (!scannedTrolleys.contains(takeOverTrolleyLabelMap.getValue())) {
                scannedTrolleys.add(takeOverTrolleyLabelMap.getValue());
                harvesterNativePage.clickInProgressTrolleysButton();
                moveToInProgressTrolley(takeOverTrolleyLabelMap.getValue());
                Assert.assertEquals(harvesterNativePage.getProgressTagText(takeOverTrolleyLabelMap.getValue()), ASSIGNING);
                harvesterNativePage.clickInProgressTrolleyMenuButton(takeOverTrolleyLabelMap.getValue());
                Assert.assertEquals(harvesterNativePage.getTakeOverRunLabelText(), TAKE_OVER_RUN);
                harvesterNativePage.clickTakeOverRunButton();
                mobileCommands.tap(harvesterNativeSelectingMap.continueButton());
                for (Map.Entry<String, String> updatedPclMap : pclTrolleyMap.entrySet()) {
                    if (testData.get(ExcelUtils.UPDATED_PCL_VALUE).contains(updatedPclMap.getKey())) {
                        getTemperatureTypeAndScanContainers(testData);
                        break;
                    }
                }
                harvesterNativePage.startRunButton();
                if (Boolean.parseBoolean(testData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE))) {
                    singleThreadedTrolleySelecting(testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, testData);
                } else {
                    multiThreadedPclTrolleySelecting(scenario, testData, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, takeOverTrolleyLabelMap.getValue());
                }
            }
        }
    }

    public void verifyHarvesterSelectingForSingleContainer(HashMap<String, String> testData, List<HashMap<String, String>> itemsList) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        HashMap<String, String> notPickedContainerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.NOT_PICKED_CONTAINER_MAP));
        HashMap<String, String> pickedContainerMap = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = notPickedContainerMap.entrySet().iterator();
        String firstContainerId = iterator.next().getKey();
        String tempType = notPickedContainerMap.get(firstContainerId);
        pickedContainerMap.put(firstContainerId, tempType);
        scanFirstContainer(firstContainerId);
        multiThreadedTrolleySelecting(firstContainerId, testData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, testData);
        notPickedContainerMap.remove(firstContainerId);
        testData.put(ExcelUtils.NOT_PICKED_CONTAINER_MAP, String.valueOf(notPickedContainerMap));
        testData.put(ExcelUtils.PICKED_CONTAINER_MAP, String.valueOf(pickedContainerMap));
    }

    public void verifyHarvesterSelectingForCollapseTempAll(List<HashMap<String, String>> itemsList, HashMap<String, String> firstOrderData, HashMap<String, String> secondOrderData) {
        HashMap<String, String> upcLabel = getItemsWithLabelsFromTestData(itemsList);
        List<HashMap<String, String>> orderMap = new ArrayList<>();
        orderMap.add(firstOrderData);
        orderMap.add(secondOrderData);
        mobileCommands.tap(harvesterNativeSelectingMap.selecting());
        boolean scanContainer = true;
        for (HashMap<String, String> testData : orderMap) {
            HashMap<String, String> containerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.CONTAINER_MAP));
            for (String containerId : containerMap.keySet()) {
                if (scanContainer) {
                    scanFirstContainer(containerId);
                    scanContainer = false;
                }
                String temperature = mobileCommands.getElementText(harvesterNativeSelectingMap.tempSkittleText());
                String orderId = mobileCommands.getElementText(harvesterNativeMap.orderId()).substring(5);
                String container = getContainerId(orderId, temperature, firstOrderData, secondOrderData);
                firstOrderData.get(ExcelUtils.VISUAL_ORDER_ID);
                multiThreadedTrolleySelecting(container, firstOrderData.get(ExcelUtils.ITEM_WEIGHT), itemsList, upcLabel, firstOrderData);
            }
        }
        completePickRun();
    }

    public String getContainerId(String orderID, String temperature, HashMap<String, String> firstOrderData, HashMap<String, String> secondOrderData) {
        String containerId = null;
        List<HashMap<String, String>> testDataMaps = new ArrayList<>();
        testDataMaps.add(firstOrderData);
        testDataMaps.add(secondOrderData);
        for (HashMap<String, String> testData : testDataMaps) {
            if (testData.get(ExcelUtils.VISUAL_ORDER_ID).equals(orderID)) {
                HashMap<String, String> containerMap = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.CONTAINER_MAP));
                for (Map.Entry<String, String> entry : containerMap.entrySet()) {
                    String temp = entry.getValue();
                    String container = entry.getKey();
                    if (temp.substring(0, 1).contains(temperature) && (!container.contains(Constants.PickCreation.OVERSIZE))) {
                        containerId = entry.getKey();
                    } else if (entry.getKey().substring(0, 2).equals(temperature)) {
                        containerId = entry.getKey();
                    }
                }
            }
        }
        return containerId;
    }

    public void multipleTrolleySelecting(HashMap<String, String> upcLabel, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        List<String> containerMapNew = ExcelUtils.convertStringToList(testOutputData.get(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS));
        String scannedContainer;
        List<String> scannedContainerList = new ArrayList<>();
        for (String container : containerMapNew) {
            if (!scannedContainerList.contains(container)) {
                harvesterNativePage.enterBarcode(container, Constants.PickCreation.ENTER_BARCODE_WITHOUT_TOOL_TIP);
                int upNextCount = 0;
                if (mobileCommands.elementDisplayed(harvesterNativeSelectingMap.upNextText())) {
                    upNextCount = Integer.parseInt(mobileCommands.getElementText(harvesterNativeSelectingMap.upNextText()).split("\\(")[1].split("\\)")[0]);
                }
                for (int i = 0; i <= upNextCount; i++) {
                    getUpcId();
                    scanItem(upcLabel, itemsList, testOutputData);
                    if (!this.upcId.equals(upcLabel.get(Constants.PickCreation.NOT_READY))) {
                        String containerNumber = mobileCommands.getElementText(harvesterNativeSelectingMap.containerNumberText()).split("-")[0].trim();
                        String containerText = mobileCommands.getElementText(harvesterNativeSelectingMap.tempSkittleText());
                        String tempSkittle = container.substring(1, container.length() - 2) + containerNumber;
                        if (containerText.equals(Constants.PickCreation.OVERSIZE)) {
                            harvesterNativePage.enterBarcode((Constants.PickCreation.OVERSIZE) + tempSkittle, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                            scannedContainer = Constants.PickCreation.OVERSIZE + tempSkittle;
                            scannedContainerList.add(scannedContainer);
                        } else {
                            harvesterNativePage.enterBarcode(Constants.PickCreation.NORMAL_CONTAINER + tempSkittle, Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP);
                            scannedContainer = Constants.PickCreation.NORMAL_CONTAINER + tempSkittle;
                            scannedContainerList.add(scannedContainer);
                        }
                    }
                }
                completePickRun();
            }
        }
    }

    private HashMap<String, String> getBagCountForCommonTrolley(HashMap<String, String> bagMap, String multiContainerTrolley, String trolley) {
        HashMap<String, String> commonTrolleyMap = ExcelUtils.convertStringToMap(multiContainerTrolley);
        HashMap<String, String> commonTrolleyBagMap = new HashMap<>();
        commonTrolleyMap.entrySet().removeIf(entry -> !entry.getValue().equals(trolley));
        Set<String> commonKeys = new HashSet<>(commonTrolleyMap.keySet());
        commonKeys.retainAll(bagMap.keySet());
        for (String key : commonKeys) {
            commonTrolleyBagMap.put(key, "1");
        }
        return commonTrolleyBagMap;
    }

    private void checkBagIconDuringPickingCommonTrolley(String containerWithBag) {
        String pclContainerDisplayed = mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi()).split("- ")[1];
        if (containerWithBag.contains(pclContainerDisplayed)) {
            mobileCommands.elementDisplayed(harvesterNativeSelectingMap.bagsIcon());
        }
    }
}
