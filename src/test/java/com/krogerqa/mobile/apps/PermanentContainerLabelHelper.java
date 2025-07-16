package com.krogerqa.mobile.apps;

import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeSelectingMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeStagingMap;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeContainerLookUpPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeOrderLookUpPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeSelectingPage;
import com.krogerqa.mobile.ui.pages.harvester.HarvesterNativeStagingPage;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.MobileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PermanentContainerLabelHelper {

    public static String itemMovementTo;
    public static String itemsToMove;
    public static String itemMovementFrom;
    public static String typeOfContainer;
    public static String fromContainerId = "";
    public static String toContainerId = "";
    public static String fromContainerTemp;
    public static String toContainerTemp;
    public static String fromContainerStatus = "";
    public static String toContainerStatus = "";
    public static String fromContainerLabel = "";
    public static String toContainerLabel = "";
    public static String itemMovementType = "";
    public static boolean pickingMovementDone = false;
    public static MobileCommands mobileCommands = new MobileCommands();
    public static String preWeighedContainer = "";
    static HarvesterNativeSelectingMap harvesterNativeSelectingMap = HarvesterNativeSelectingMap.getInstance();
    static HarvesterNativeStagingPage harvesterNativeStagingPage = HarvesterNativeStagingPage.getInstance();
    static HarvesterNativeContainerLookUpPage harvesterNativeContainerLookUpPage = HarvesterNativeContainerLookUpPage.getInstance();
    static HarvesterNativeOrderLookUpPage harvesterNativeOrderLookUpPage = HarvesterNativeOrderLookUpPage.getInstance();
    static HarvesterNativeSelectingPage harvesterNativeSelectingPage = HarvesterNativeSelectingPage.getInstance();
    static HarvesterNativeStagingMap harvesterNativeStagingMap = HarvesterNativeStagingMap.getInstance();
    static MobileUtils mobileUtils = MobileUtils.getInstance();
    static HarvesterSelectingAndStaging harvesterSelectingAndStaging = HarvesterSelectingAndStaging.getInstance();
    private static PermanentContainerLabelHelper instance;

    private PermanentContainerLabelHelper() {
    }

    public synchronized static PermanentContainerLabelHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (PermanentContainerLabelHelper.class) {
            if (instance == null) {
                instance = new PermanentContainerLabelHelper();
            }
        }
        return instance;
    }

    public static int generateRandomPclNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(999);
    }

    public static void getMovementContainerDetailsFromExcel(HashMap<String, String> testOutputData) {
        String itemMovement = testOutputData.get(ExcelUtils.ITEM_MOVEMENT_MAP);
        HashMap<String, String> itemMovementDetailsMap = ExcelUtils.convertStringToMap(itemMovement);
        itemsToMove = itemMovementDetailsMap.get(ExcelUtils.ITEMS_TO_MOVE);
        itemMovementFrom = itemMovementDetailsMap.get(ExcelUtils.MOVEMENT_FROM_CONTAINER);
        typeOfContainer = itemMovementDetailsMap.get(ExcelUtils.TYPE_OF_CONTAINER);
        preWeighedContainer = itemMovementDetailsMap.get(ExcelUtils.PRE_WEIGHED_CONTAINER);
        itemMovementTo = itemMovementDetailsMap.get(ExcelUtils.MOVEMENT_TO_CONTAINER);
        fromContainerStatus = itemMovementDetailsMap.get(ExcelUtils.FROM_CONTAINER_STATUS);
        toContainerStatus = itemMovementDetailsMap.get(ExcelUtils.TO_CONTAINER_STATUS);
        itemMovementType = itemMovementDetailsMap.get(ExcelUtils.ITEM_MOVEMENT_TYPE);
    }

    public static void getMovementContainerIdAndTemperature(HashMap<String, String> testOutputData, String itemMovementFrom, String typeOfContainer, String itemMovementTo, HashMap<String, String> pclTemperatureContainerMap) {
        for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
            if (!itemMovementFrom.equalsIgnoreCase(ExcelUtils.OVERSIZE)) {
                if (entry.getValue().equalsIgnoreCase(itemMovementFrom) && !entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_FROM_PICKED_TO_STAGED))) {
                        HashMap<String, String> containerStatusMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_STATUS_MAP));
                        for (Map.Entry<String, String> entry1 : containerStatusMap.entrySet()) {
                            if (!(entry.getKey().equalsIgnoreCase(entry1.getKey()) && entry1.getValue().equalsIgnoreCase(ExcelUtils.STAGED))) {
                                fromContainerId = entry.getKey();
                                fromContainerTemp = entry.getValue();
                                break;
                            }
                        }
                    } else {
                        if (fromContainerId.isEmpty()) {
                            fromContainerId = entry.getKey();
                            fromContainerTemp = entry.getValue();
                        }
                    }
                }
            } else {
                if (entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    fromContainerId = entry.getKey();
                    fromContainerTemp = entry.getValue();
                }
            }
            if (typeOfContainer.equalsIgnoreCase(ExcelUtils.EXISTING)) {
                if (!itemMovementTo.equalsIgnoreCase(ExcelUtils.OVERSIZE)) {
                    if (entry.getValue().equalsIgnoreCase(itemMovementTo) && !entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        if (!entry.getKey().equalsIgnoreCase(fromContainerId)) {
                            toContainerId = entry.getKey();
                            toContainerTemp = entry.getValue();
                        }
                    }
                } else {
                    if (entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                        toContainerId = entry.getKey();
                        toContainerTemp = entry.getValue();
                    }
                }
            }
        }
        if (typeOfContainer.equalsIgnoreCase(ExcelUtils.NEW)) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PICK_TO_BAG)) || itemMovementTo.equalsIgnoreCase(ExcelUtils.FROZEN)) {
                toContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.FLOATING_LABEL + generateRandomPclNumber();
            } else {
                toContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.PERMANENT_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
            }
        }
        if (itemMovementFrom.equals(itemMovementTo) && typeOfContainer.equals(ExcelUtils.EXISTING)) {
            for (Map.Entry<String, String> entry : pclTemperatureContainerMap.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(itemMovementFrom) && !entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_FROM_PICKED_TO_STAGED))) {
                        HashMap<String, String> containerStatusMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_STATUS_MAP));
                        for (Map.Entry<String, String> entry1 : containerStatusMap.entrySet()) {
                            if (!(entry.getKey().equalsIgnoreCase(entry1.getKey()) && entry1.getValue().equalsIgnoreCase(ExcelUtils.STAGED))) {
                                fromContainerId = entry.getKey();
                                fromContainerTemp = entry.getValue();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void getAllMovementDetails(HashMap<String, String> testOutputData) {
        getMovementContainerDetailsFromExcel(testOutputData);
        getMovementContainerIdAndTemperature(testOutputData, itemMovementFrom, typeOfContainer, itemMovementTo, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
    }

    public static void performItemMovementViaStaging(String scenario, HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        String fromContainer = fromContainerId;
        if (fromContainerStatus != null && fromContainerStatus.equals(ExcelUtils.STAGED))
            fromContainer = fromContainerLabel;
        harvesterNativeStagingPage.moveFromOneContainerToAnother(scenario, fromContainer, itemsToMove, toContainerId, fromContainerTemp, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
        navigateToHomeScreen();
    }

    public static void navigateToHomeScreen() {
        int i = 0;
        while (!mobileCommands.elementDisplayed(harvesterNativeStagingMap.selectStaging())) {
            mobileCommands.browserBack();
            i++;
            if (i > 10) {
                break;
            }
        }
    }

    public static HashMap<String, String> performItemMovementViaOrderLookup(String scenario, HashMap<String, String> testOutputData) {
        try {
            getAllMovementDetails(testOutputData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_RE_STAGE_BEFORE_ITEM_MOVEMENT))) {
                harvesterSelectingAndStaging.performReStaging(testOutputData.get(ExcelUtils.SCENARIO));
                mobileCommands.browserBack();
                mobileCommands.browserBack();
            }
            testOutputData = harvesterNativeOrderLookUpPage.moveFromOneContainerToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
            navigateToHomeScreen();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.HARVESTER, String.valueOf(e));
        }
        return testOutputData;
    }

    public static void performItemMovementViaPicking(String scenario, HashMap<String, String> testOutputData, String pclContainerId) {
        getAllMovementDetails(testOutputData);
        if (pclContainerId.equals(fromContainerId) && !pickingMovementDone) {
            harvesterNativeSelectingPage.moveFromOneContainerToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
            pickingMovementDone = true;
        }
    }

    public static void performItemMovementAfterStaging(String scenario, HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        harvesterNativeStagingPage.moveFromOneContainerToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
        mobileCommands.browserBack();
        mobileCommands.browserBack();
    }

    public static HashMap<String, String> performItemMovementViaPreWeighed(String scenario, HashMap<String, String> testOutputData, String containerId) {
        getAllMovementDetails(testOutputData);
        if (containerId.equals(fromContainerId) && !fromContainerId.contains(Constants.PickCreation.OVERSIZE)) {
            harvesterNativeSelectingPage.fulfillPreWeighItem();
        }
        if (typeOfContainer.equalsIgnoreCase(ExcelUtils.EXISTING)) {
            String pclContainerId = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + (mobileCommands.getElementText(harvesterNativeSelectingMap.getPclLabelFromUi())).split("- ")[1];
            if (!pclContainerId.equals(testOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER))) {
                fromContainerId = pclContainerId;
                toContainerId = testOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER);
            } else {
                fromContainerId = testOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER);
            }
        }
        testOutputData = harvesterNativeSelectingPage.moveFromOneContainerToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));

        return testOutputData;
    }

    public static String getItemMovementFromContainerStatus(HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        return fromContainerStatus;
    }

    public static String getItemMovementToContainerStatus(HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        return toContainerStatus;
    }

    public static void performItemMovementViaSubstitution(String scenario, HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        harvesterNativeSelectingPage.moveFromOneContainerToAnotherThroughSubs(scenario, fromContainerId, toContainerId, fromContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
    }

    public static void updateContainer(String scenario, HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        harvesterNativeContainerLookUpPage.updatePclMaps(scenario, toContainerId, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
    }

    public static HashMap<String, String> itemMovementFromInformationLookup(String scenario, HashMap<String, String> testOutputData) {
        getAllMovementDetails(testOutputData);
        if (itemMovementType == null) {
            itemMovementType = "";
        }
        if (itemMovementType.equalsIgnoreCase(ExcelUtils.PCL_LABEL)) {
            if (fromContainerId.contains(Constants.PickCreation.PERMANENT_LABEL) || fromContainerId.contains(Constants.PickCreation.FLOATING_LABEL)) {
                testOutputData = harvesterNativeOrderLookUpPage.moveFromOneContainerToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
            } else {
                testOutputData = harvesterNativeOrderLookUpPage.itemMovementFromOSToAnother(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, toContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
            }
        } else if (itemMovementType.equalsIgnoreCase(ExcelUtils.STAGING_LOCATION)) {
            harvesterNativeOrderLookUpPage.itemMovementViaStagingLocation(scenario, fromContainerId, itemsToMove, toContainerId, fromContainerTemp, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP)));
        }
        navigateToHomeScreen();
        return testOutputData;
    }
}
