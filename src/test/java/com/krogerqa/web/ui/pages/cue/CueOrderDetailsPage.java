package com.krogerqa.web.ui.pages.cue;

import com.krogerqa.api.PickingServicesHelper;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.utils.WebUtils;
import com.krogerqa.web.ui.maps.cue.CueContainerDetailsMap;
import com.krogerqa.web.ui.maps.cue.CueOrderDetailsMap;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CueOrderDetailsPage {
    private static final String TEMP_TYPE_COLUMN = "Temp Type";
    private static final String ITEMS_COLUMN = "Items";
    private static final String CONTAINER_COLUMN = "Container #";
    private static final String PROGRESS_COLUMN = "Progress";
    private static final String STATUS_COLUMN = "Status";
    private static final String STAGING_ZONE_COLUMN = "Staging Zone";
    private static final String TROLLEY_NUMBER_COLUMN = "Trolley #";
    private static final String CUE_CONTAINER_PICKED = "PICKED";
    private static final String CUE_CONTAINER_OUT_OF_STOCK = "—";
    private static final String CUE_CONTAINER_PICKED_PROGRESS = "100%";
    private static final String CUE_NR_CONTAINER_PICKED_PROGRESS = "0%";
    private static final String CUE_CONTAINER_STAGED = "STAGED";
    private static final String OS_FR_CommonTrolleysNotFoundError = "No common trolleys are identified for frozen and oversize containers";
    private static final String pureOverSizeTrolleyNotFoundError = "Pure OS trolley didn't generated for multiple orders";
    private static final String AGE_CHECK_LABEL_MESSAGE = "Age check label is not present in Cue";

    public static HashMap<String, String> trolleyTemperatureMap = new HashMap<>();
    public static String osLabel = "";
    public static boolean secondOsLabel = true;
    static PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();
    static String pclLabelGenerated;
    static HashMap<String, String> containerMapPcl = new HashMap<>();
    static String trolleyNotToPick;
    static String incorrectContainerStatus = "Incorrect container status";
    static String incorrectContainerProgress = "Incorrect container progress";
    static String incorrectItemPickedCount = "Incorrect item picked count";
    static String itemTemperatureNotFoundInCueTrolley = "Item temperature not found in cue trolley";
    static String outOfStockContainerNotFoundOrMoreThanOneFound = "Out of stock container not found or more than one found";
    static String shortedContainerNotFoundOrMoreThanOneFound = "Shorted container not found or more than one found";
    static String notReadyContainerNotFoundOrMoreThanOneFound = "Not Ready container not found or more than one found";
    static String outOfStockItemLabelMissing = "Out of stock item label missing";
    static String shortedItemLabelMissing = "Shorted item label missing";
    static String notReadyItemLabelMissing = "Not Ready item label missing";
    static String outOfStockItemNotFound = "Out of stock item not found";
    static String shortedItemNotFound = "Out of stock item not found";
    static String notReadyItemNotFound = "Out of stock item not found";
    static String partial = "Partial";
    static String incorrectShortedContainerStatus = "Incorrect shorted container status";
    static String AmbientAndRefrigeratedTrolleysAreNotSame = "Ambient and Refrigerated trolley's are not same";
    static String FrozenAndRefrigeratedTrolleysAreNotSame = "Frozen and Refrigerated trolley's are not same";
    static String FrozenAndOverSizedTrolleysAreNotSame = "Frozen and OverSized trolley's are not same";
    static String FrozenAndOverSizedTrolleysAreSame = "Frozen and OverSized trolley's are same";
    static String ambientFrozenTrolleyMessage = "Common trolleys generated for Ambient and frozen trolley";
    static String refrigeratedFrozenTrolleyMessage = "Common trolleys generated for Refrigerated and frozen trolley";
    static String overSizeRefrigeratedTrolleyMessage = "Common trolleys generated for OverSize and refrigerated trolley";
    static String overSizeFrozenTrolleyMessage = "Common trolleys generated for OverSize and frozen trolley";
    static String ambientRefrigeratedTrolleyMessage = "Common trolleys generated for Ambient and Refrigerated trolley";
    static String ambientOverSizeTrolleyMessage = "Common trolleys generated for Ambient and Oversize trolley";
    private static CueOrderDetailsPage instance;
    WebUtils webUtils = WebUtils.getInstance();
    BaseCommands baseCommands = new BaseCommands();
    WebUtils utilities = WebUtils.getInstance();
    CueOrderDetailsMap cueOrderDetailsMap = CueOrderDetailsMap.getInstance();
    CueContainerDetailsPage cueContainerDetailsPage = CueContainerDetailsPage.getInstance();
    CueContainerDetailsMap cueContainerDetailsMap = CueContainerDetailsMap.getInstance();
    int outOfStockCheck = 0, shortedCheck = 0;
    int notReady = 1;
    static String frozenContainerCombinedWithOSTrolleyWithDyBOSEnabled = "Frozen containers combined with OS trolley with DB OS+ enabled";
    static String smallOversizeOSFRTrolleyMessage = "small OS items in OS FR trolley";
    static String smallOversizeAMRETrolleyMessage = "AM items with SM OS items";
    static String smallOversizeOSTrolleyMessage = "small OS items in OS trolley";
    static String smallOversizeFRTrolleyMessage = "small OS items in FR trolley";


    private CueOrderDetailsPage() {
    }

    public synchronized static CueOrderDetailsPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueOrderDetailsPage.class) {
            if (instance == null) {
                instance = new CueOrderDetailsPage();
            }
        }
        return instance;
    }

    public static void getMultipleContainerTrolley(HashMap<String, String> containerMapPcl, HashMap<String, String> testOutputData) {
        Set<String> duplicateTrolley = new HashSet<>();
        Set<String> uniqueTrolleys = new HashSet<>();
        HashMap<String, String> multiContainerTrolley = new HashMap<>();
        for (Map.Entry<String, String> entry : containerMapPcl.entrySet()) {
            String value = entry.getValue();
            if (uniqueTrolleys.contains(value)) {
                duplicateTrolley.add(value);
            } else {
                uniqueTrolleys.add(value);
            }
        }
        for (Map.Entry<String, String> entry : containerMapPcl.entrySet()) {
            String pclLabel = entry.getKey();
            String trolley = entry.getValue();
            if (duplicateTrolley.contains(trolley)) {
                multiContainerTrolley.put(pclLabel, trolley);
                testOutputData.put(ExcelUtils.MULTI_CONTAINER_TROLLEY, String.valueOf(multiContainerTrolley));
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && (testOutputData.containsKey(ExcelUtils.IS_MULTIPLE_OVERSIZE)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_OVERSIZE))) {
            HashMap<String, String> multiContainerPclOsTrolley = new HashMap<>();
            if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_TROLLEY)) {
                multiContainerPclOsTrolley = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_TROLLEY));
            }
            for (Map.Entry<String, String> entry : containerMapPcl.entrySet()) {
                if (entry.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    String pclLabel = entry.getKey();
                    String trolley = entry.getValue();
                    multiContainerPclOsTrolley.put(pclLabel, trolley);
                    break;
                }
            }
            testOutputData.put(ExcelUtils.MULTI_CONTAINER_TROLLEY, String.valueOf(multiContainerPclOsTrolley));
        }
    }

    public String getVisualOrderIdAndOpenDetails() {
        String visualOrderId;
        try {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.visualOrderIdText());
            visualOrderId = baseCommands.getElementText(cueOrderDetailsMap.visualOrderIdText());
        } catch (Exception | AssertionError e) {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.visualOrderIdText());
            visualOrderId = baseCommands.getElementText(cueOrderDetailsMap.visualOrderIdText());
        }
        baseCommands.click(cueOrderDetailsMap.visualOrderIdText());
        return visualOrderId;
    }

    public void verifyPickedAndPaidStatusInOrderDetailsPage() {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.orderStatusText());
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.paymentStatusText());
        Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.orderStatusText()), CueHomePage.CUE_ORDER_PICKED_UP);
        Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.paymentStatusText()), CueHomePage.CUE_PAYMENT_PAID);
    }

    public void verifyCancelledStatusInOrderDetailsPage() {
        baseCommands.click(cueOrderDetailsMap.visualOrderIdText());
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.orderStatusText());
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.paymentStatusText());
        Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.orderStatusText()), CueHomePage.CUE_ORDER_DETAILS_CANCELLED);
        Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.paymentStatusText()), CueHomePage.CUE_ORDER_DETAILS_CANCELLED);
    }

    public String getVisualOrderId() {
        return baseCommands.getElementText(cueOrderDetailsMap.visualOrderIdText()).trim();
    }

    public void requestTrolleyForDynamicBatchingFromApi(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DB_PCL_SINGLE_THREAD))) {
            pickingServicesHelper.requestTrolley();
        }
        pickingServicesHelper.verifyAllContainerGeneratedForOrder(testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    /**
     * Creates a map with column heading, and its index from container details table
     *
     * @param columnHeadingList List of column headings to be included in the map
     * @return Map with container details column heading and index pairs
     */
    public HashMap<String, Integer> getColumnHeadingMap(String[] columnHeadingList) {
        HashMap<String, Integer> columnHeadingMap = new HashMap<>();
        List<WebElement> containerColumnList = baseCommands.elements(cueOrderDetailsMap.containerColumnList());
        for (int i = 0; i < containerColumnList.size(); i++) {
            for (String columnHeading : columnHeadingList) {
                if (containerColumnList.get(i).getText().equalsIgnoreCase(columnHeading)) {
                    columnHeadingMap.put(columnHeading, i);
                }
            }
        }
        return columnHeadingMap;
    }

    public List<WebElement> getContainerLists() {
        List<WebElement> containerIdList;
        try {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        }
        return containerIdList;
    }

    /*** @return map of container id and its temperature type */
    public HashMap<String, String> getAllContainersTemperature(HashMap<String, String> testOutputData) {
        HashMap<String, String> containerMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        baseCommands.webpageRefresh();
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        List<WebElement> containerIdList = getContainerLists();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCH_ORDER))) {
            try {
                baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
            } catch (Exception batchingFailure) {
                pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Constants.Date.TODAY);
                baseCommands.webpageRefresh();
                baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
            }
        }
        for (int i = 0; i < containerIdList.size(); i++) {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            containerMap.put(getContainerText(i, containerIdList), getContainerIdText(i, columnHeading));
        }
        return containerMap;
    }

    public String getContainerText(int i, List<WebElement> containerIdList) {
        int retry = 10;
        String text = "";
        while (retry != 0) {
            try {
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
                text = containerIdList.get(i).getText();
                retry = 0;
            } catch (Exception | AssertionError e) {
                retry--;
            }
        }
        return text;
    }

    public String getContainerIdText(int i, HashMap<String, Integer> columnHeading) {
        int retry = 5;
        String text = "";
        while (retry != 0) {
            try {
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
                text = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
                retry = 0;
            } catch (Exception | AssertionError e) {
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
                text = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
                retry--;
            }
        }
        return text;
    }

    public HashMap<String, String> getAllContainersTemperatureWithStoreLocation(String div, String store) {
        HashMap<String, String> containerMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < containerIdList.size(); i++) {
            String containerId;
            if (containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE)) {
                containerId = containerIdList.get(i).getText();
            } else {
                containerId = div + store + containerIdList.get(i).getText();
            }
            containerMap.put(containerId, baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
        }
        return containerMap;
    }

    public HashMap<String, String> getAllContainersTemperatureWithoutStoreLocation() {
        HashMap<String, String> containerMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < containerIdList.size(); i++) {
            String containerId = containerIdList.get(i).getText();
            containerMap.put(containerId, baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
        }
        return containerMap;
    }

    public void getContainerLabelForNonPcl(HashMap<String, String> testOutputData) {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerList;
        try {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            containerList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        } catch (Exception | AssertionError e) {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            containerList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        }
        List<String> multiItemContainer = new ArrayList<>();
        if (testOutputData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER)) {
            for (WebElement webElement : containerList) {
                if (testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER).contains(webElement.getText())) {
                    String containerIdForNonPcl = webElement.getText();
                    multiItemContainer.add(containerIdForNonPcl);
                }
            }
        }
        testOutputData.put(ExcelUtils.MULTI_UPC_CONTAINER_VALUES, String.valueOf(multiItemContainer));
    }

    public HashMap<String, String> getPclLabelsForContainers(HashMap<String, String> testOutputData) {
        HashMap<String, String> pclLabelTemperatureMap = new HashMap<>();
        HashMap<String, String> pclIdTemperatureMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        List<WebElement> containerIdList = getContainerLists();
        List<String> multiItemContainer = new ArrayList<>();
        List<WebElement> trolleyIdList = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        int osContainerCount = 0;
        for (int i = 0; i < containerIdList.size(); i++) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE)) && containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE)) {
                osContainerCount++;
            }
            pclLabelGenerated = generatePclContainerLabel(testOutputData, columnHeading, i, containerIdList, osContainerCount);
            if (containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE)) {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                    containerMapPcl.put(containerIdList.get(i).getText(), trolleyIdList.get(i).getText());
                    pclLabelTemperatureMap.put(containerIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                    pclIdTemperatureMap.put(containerIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                } else {
                    containerMapPcl.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), trolleyIdList.get(i).getText());
                    pclLabelTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                    pclIdTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated).substring(6), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                }
            } else {
                containerMapPcl.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), trolleyIdList.get(i).getText());
                pclLabelTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
                pclIdTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated).substring(6), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REUSE_PCL_SCENARIO))) {
                trolleyTemperatureMap.put(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))), trolleyIdList.get(i).getText());
            } else {
                trolleyTemperatureMap.put(trolleyIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
            }
            if (testOutputData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER)) {
                if (testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER).contains(containerIdList.get(i).getText())) {
                    String pclLabelForMultiContainer = containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated);
                    multiItemContainer.add(pclLabelForMultiContainer);
                }
            }
        }
        testOutputData.put(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES, String.valueOf(multiItemContainer));
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclLabelTemperatureMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
        getMultipleContainerTrolley(containerMapPcl, testOutputData);
        return containerMapPcl;
    }

    public HashMap<String, String> getReusePclLabelsForContainers(String baseScenario) {
        HashMap<String, String> baseTestOutputData = ExcelUtils.getTestDataMap(baseScenario, baseScenario);
        HashMap<String, String> baseOrderPclTemperatureMap = ExcelUtils.convertStringToMap(baseTestOutputData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        List<WebElement> trolleyIdList = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        HashMap<String, String> pclLabelTrolleyMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        HashMap<String, String> trolleyTemperatureMap = new HashMap<>();
        for (int i = 0; i <= baseCommands.numberOfElements(cueOrderDetailsMap.trolleyRowList()) - 1; i++) {
            trolleyTemperatureMap.put(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))), trolleyIdList.get(i).getText());
        }
        for (Map.Entry<String, String> trolleyTempMap : trolleyTemperatureMap.entrySet()) {
            for (Map.Entry<String, String> labelMap : baseOrderPclTemperatureMap.entrySet()) {
                if (trolleyTempMap.getKey().equals(labelMap.getValue())) {
                    pclLabelTrolleyMap.put(labelMap.getKey(), trolleyTempMap.getValue());
                }
            }
        }
        return pclLabelTrolleyMap;
    }

    List<String> overSizeLabelList = new ArrayList<>();

    public String generatePclContainerLabel(HashMap<String, String> testOutputData, HashMap<String, Integer> columnHeading, int i, List<WebElement> containerIdList, int osContainerCount) {
        String pclLabel = "";
        boolean overSizeTrolleyPresent = false;
        if (baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))).equalsIgnoreCase(Constants.PickCreation.FROZEN_TEMP)) {
            if (containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                    for (String osContainer : overSizeLabelList) {
                        if (osContainer.contains(Constants.PickCreation.OVERSIZE)) {
                            pclLabel = osLabel;
                            overSizeTrolleyPresent = true;
                            break;
                        }
                    }
                    if (!overSizeTrolleyPresent) {
                        pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.OVERSIZE + PermanentContainerLabelHelper.generateRandomPclNumber();
                        osLabel = pclLabel;
                        overSizeLabelList.add(pclLabel);
                    }
                }
            } else {
                pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.FLOATING_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
            }
        } else {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PICK_TO_BAG))) {
                pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.FLOATING_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
            } else {
                if (containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
                    if (osContainerCount == 1) {
                        pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.OVERSIZE + PermanentContainerLabelHelper.generateRandomPclNumber();
                        osLabel = pclLabel;
                    }
                    if (osContainerCount > 9 && secondOsLabel && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MULTIPLE_PCL_OS_TROLLEYS))) {
                        pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.OVERSIZE + PermanentContainerLabelHelper.generateRandomPclNumber();
                        osLabel = pclLabel;
                        secondOsLabel = false;
                    } else if (osContainerCount > 1) {
                        pclLabel = osLabel;
                    }
                } else {
                    pclLabel = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.PERMANENT_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
                }
            }
        }
        return pclLabel;
    }

    public void verifyTrolleysNotCreated() {
        try {
            String[] columnHeadingList = {TROLLEY_NUMBER_COLUMN};
            HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
            for (int i = 0; i < containerIdList.size(); i++) {
                Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1))), "—");
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public HashMap<String, String> verifyTrolleysCreated(HashMap<String, String> testOutputData) {
        String[] columnHeadingList = {TROLLEY_NUMBER_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        Set<String> trolleys = new HashSet<>();
        for (int i = 0; i < containerIdList.size(); i++) {
            String trolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            trolleys.add(trolley);
        }
        testOutputData.put(ExcelUtils.TROLLEYS, trolleys.toString());
        return testOutputData;
    }

    public void openTrolleyPageInCue() {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.trolleySection());
        baseCommands.click(cueOrderDetailsMap.trolleySection());
    }

    public void validateDetailsOnCue(HashMap<String, String> containerMap, HashMap<String, String> testOutputData) {
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
            for (int i = 1; i <= containerMap.size(); i++) {
                String containerId = clickOnContainers(i);
                cueContainerDetailsPage.addItemsUpc(containerId, testOutputData);
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)) + "/" + testOutputData.get(ExcelUtils.ORDER_NUMBER));
            }
        }
    }

    public void validateOrderDetailsOnCue(HashMap<String, String> containerMap, List<HashMap<String, String>> itemsList, HashMap<String, String> testOutputData) {
        verifyItemsUpcAndQty(testOutputData, itemsList);
        if (!(CueContainerDetailsPage.overSizeContainerUpcMap == null)) {
            CueContainerDetailsPage.overSizeContainerUpcMap.clear();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_OVERSIZE))) {
            HashMap<String, String> valueFromUPCTempMap = createMapForOverSizeItems();
            testOutputData.put(ExcelUtils.MULTIPLE_OS_UPC_TEMP, String.valueOf(valueFromUPCTempMap));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK))) {
            Assert.assertTrue(baseCommands.elementDisplayed(cueOrderDetailsMap.ageRestrictedLabel()), AGE_CHECK_LABEL_MESSAGE);
        }
        for (int i = 1; i <= containerMap.size(); i++) {
            String containerId = clickOnContainers(i);
            cueContainerDetailsPage.addItemsUpc(containerId, testOutputData);
            baseCommands.openNewUrl(PropertyUtils.getCueUrl(testOutputData.get(ExcelUtils.STORE_ID)) + "/" + testOutputData.get(ExcelUtils.ORDER_NUMBER));
        }
        cueContainerDetailsPage.verifyItemsUpc(itemsList);
        verifyTemperatureType(itemsList);
    }

    public void verifyTemperatureType(List<HashMap<String, String>> itemsList) {
        Set<String> expectedTempContainersSet = new HashSet<>();
        for (HashMap<String, String> items : itemsList) {
            expectedTempContainersSet.add(items.get(ExcelUtils.ITEM_TEMPERATURE));
        }
        List<String> expectedTempContainersList = new ArrayList<>(expectedTempContainersSet);
        Set<String> actualTempContainersSet = new HashSet<>();
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        int totalContainers = baseCommands.elements(cueOrderDetailsMap.containerRowList()).size();
        for (int i = 1; i <= totalContainers; i++) {
            String temperature = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(Integer.toString(i), "3"));
            actualTempContainersSet.add(temperature);
        }
        List<String> actualTempContainersList = new ArrayList<>(actualTempContainersSet);
        for (int i = 0; i < expectedTempContainersSet.size(); i++) {
            Assert.assertTrue(actualTempContainersList.contains(expectedTempContainersList.get(i)), itemTemperatureNotFoundInCueTrolley);
        }
    }

    public void verifyItemsUpcAndQty(HashMap<String, String> testOutputData, List<HashMap<String, String>> itemsList) {
        baseCommands.webpageRefresh();
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_SINGLE_ORDER_LEVEL_BATCH)) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_RUSH_ORDER))) {
            String text = baseCommands.getElementText(cueOrderDetailsMap.getAllItemsOrderedText());
            int i = 0;
            while (text.contains("0")) {
                baseCommands.webpageRefresh();
                i++;
                if (i == 15) {
                    break;
                }
            }
        }
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.itemDetailsSection());
        baseCommands.waitForElementClickability(cueOrderDetailsMap.printPageButton());
        int totalItems = baseCommands.elements(cueOrderDetailsMap.itemDetailsSection()).size();
        List<String> upcList = new ArrayList<>();
        List<String> qtyList = new ArrayList<>();
        for (int i = 1; i <= totalItems; i++) {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.itemUpc(Integer.toString(i)));
            String upc = (baseCommands.getElementText(cueOrderDetailsMap.itemUpc(Integer.toString(i))).split(": "))[1];
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.itemQty(Integer.toString(i)));
            String qty = (baseCommands.getElementText(cueOrderDetailsMap.itemQty(Integer.toString(i))).split(" "))[1];
            upcList.add(upc);
            qtyList.add(qty);
        }
        for (HashMap<String, String> itemDetailsMap : itemsList) {
            if (upcList.contains(itemDetailsMap.get(ExcelUtils.ITEM_UPC))) {
                int index = upcList.indexOf(itemDetailsMap.get(ExcelUtils.ITEM_UPC));
                Assert.assertEquals(qtyList.get(index), itemDetailsMap.get(ExcelUtils.ITEM_QUANTITY), "Quantity of item with UPC:" + itemDetailsMap.get(ExcelUtils.ITEM_UPC) + "not matched");
            } else {
                Assert.fail("Item with UPC:" + itemDetailsMap.get(ExcelUtils.ITEM_UPC) + " not found in order");
            }
        }
    }

    public String clickOnContainers(int container) {
        String containerIdUrl = baseCommands.getElementAttribute(cueOrderDetailsMap.containerIdDetails(Integer.toString(container), "1"), "href");
        String containerIdText = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(Integer.toString(container), "1"));
        baseCommands.openNewUrl(containerIdUrl);
        if (baseCommands.getUrl().contains("#")) {
            for (int i = 0; i <= 5; i++) {
                baseCommands.webpageRefresh();
                containerIdUrl = baseCommands.getElementAttribute(cueOrderDetailsMap.containerIdDetails(Integer.toString(container), "1"), "href");
                containerIdText = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(Integer.toString(container), "1"));
                if (!containerIdUrl.contains("#")) {
                    baseCommands.openNewUrl(containerIdUrl);
                    break;
                }
            }
        }
        baseCommands.waitForElementVisibility(cueContainerDetailsMap.printContainerlabel());
        baseCommands.waitForElementClickability(cueContainerDetailsMap.printContainerlabel());
        return containerIdText;
    }

    private HashMap<String, String> createMapForOverSizeItems() {
        HashMap<String, String> multipleOSMapReplaceUpc = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        for (int i = 1; i <= baseCommands.numberOfElements(cueOrderDetailsMap.CheckRowForContainer()); i++) {
            if (baseCommands.getElementText(cueOrderDetailsMap.checkForContainerOSUpc(i)).contains(Constants.PickCreation.OVERSIZE)) {
                baseCommands.click(cueOrderDetailsMap.checkForContainerOSUpc(i));
                String upc = baseCommands.getElementText(cueContainerDetailsMap.itemDetails("1", "3"));
                baseCommands.browserBack();
                String temp = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
                multipleOSMapReplaceUpc.put(upc, temp);
            }
        }
        return multipleOSMapReplaceUpc;
    }

    public HashMap<String, String> getItemLabels(List<HashMap<String, String>> itemsList) {
        HashMap<String, String> upcLabel = new HashMap<>();
        for (HashMap<String, String> itemDetails : itemsList) {
            if (itemDetails.containsKey(ExcelUtils.FULFILLMENT_TYPE)) {
                switch (itemDetails.get(ExcelUtils.FULFILLMENT_TYPE)) {
                    case Constants.PickCreation.OUT_OF_STOCK:
                        upcLabel.put(Constants.PickCreation.OUT_OF_STOCK, itemDetails.get(ExcelUtils.ITEM_UPC));
                        Assert.assertEquals(outOfStockCheck, 1, outOfStockContainerNotFoundOrMoreThanOneFound);
                        break;
                    case Constants.PickCreation.SHORTED:
                        upcLabel.put("Shorted", itemDetails.get(ExcelUtils.ITEM_UPC));
                        Assert.assertEquals(shortedCheck, 1, shortedContainerNotFoundOrMoreThanOneFound);
                        break;
                    case Constants.PickCreation.NOT_READY:
                        upcLabel.put(Constants.PickCreation.NOT_READY, itemDetails.get(ExcelUtils.ITEM_UPC));
                        Assert.assertEquals(notReady, 1, notReadyContainerNotFoundOrMoreThanOneFound);
                }
            }
        }
        return upcLabel;
    }

    /*** @param itemsList from input data json */
    public void verifyItemsNotFulfilled(List<HashMap<String, String>> itemsList) {
        HashMap<String, String> upcLabel = getItemLabels(itemsList);
        utilities.scrollToBottom();
        for (WebElement itemDetails : baseCommands.elements(cueOrderDetailsMap.itemDetailsSection())) {
            String itemSummary = itemDetails.getText();
            if (upcLabel.containsKey(Constants.PickCreation.OUT_OF_STOCK) && itemSummary.contains(upcLabel.get(Constants.PickCreation.OUT_OF_STOCK))) {
                Assert.assertTrue(itemSummary.contains(Constants.PickCreation.OUT_OF_STOCK), outOfStockItemLabelMissing);
                outOfStockCheck++;
            } else if (upcLabel.containsKey(Constants.PickCreation.SHORTED) && itemSummary.contains(upcLabel.get(Constants.PickCreation.SHORTED))) {
                Assert.assertTrue(itemSummary.contains(partial), shortedItemLabelMissing);
                shortedCheck++;
            } else if (upcLabel.containsKey(Constants.PickCreation.NOT_READY) && itemSummary.contains(upcLabel.get(Constants.PickCreation.NOT_READY))) {
                Assert.assertTrue(itemSummary.contains(Constants.PickCreation.NOT_READY), notReadyItemLabelMissing);
                notReady++;
            }
        }
        for (String label : upcLabel.keySet()) {
            if (label.equalsIgnoreCase(Constants.PickCreation.OUT_OF_STOCK)) {
                Assert.assertEquals(outOfStockCheck, 2, outOfStockItemNotFound);
            } else if (label.equalsIgnoreCase(Constants.PickCreation.SHORTED)) {
                Assert.assertEquals(shortedCheck, 2, shortedItemNotFound);
            } else if (label.equalsIgnoreCase(Constants.PickCreation.NOT_READY)) {
                Assert.assertEquals(notReady, 2, notReadyItemNotFound);
            } else {
                Assert.fail("Invalid label");
            }
        }
    }

    public void verifyCueStatusForInstacartOrder(HashMap<String, String> testOutputData, String status) {
        getVisualOrderIdAndOpenDetails();
        String[] columnHeadingList = {CONTAINER_COLUMN, STATUS_COLUMN};
        HashMap<String, Integer> columnHeadingMap = getColumnHeadingMap(columnHeadingList);
        int size;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
            try {
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerInstacartRowList());
            } catch (Exception | AssertionError e) {
                baseCommands.webpageRefresh();
                baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerInstacartRowList());
            }
            size = 3;
        } else {
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            size = baseCommands.elements(cueOrderDetailsMap.containerRowList()).size();
        }
        for (int i = 0; i < size; i++) {
            HashMap<String, String> containerDetails = new HashMap<>();
            for (Map.Entry<String, Integer> columnHeading : columnHeadingMap.entrySet()) {
                containerDetails.put(columnHeading.getKey(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.getValue() + 1))));
            }
            if (status.equals(Constants.Instacart.PICKED)) {
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
            } else if (status.equals(Constants.Instacart.PICKED_UP)) {
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), Constants.Instacart.PICKED_UP, incorrectContainerStatus);
            } else {
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_STAGED, incorrectContainerStatus);
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                    HashMap<String, String> stagingZonesForBulkStaging = new HashMap<>();
                    String[] stagingZoneList = {STAGING_ZONE_COLUMN, TEMP_TYPE_COLUMN, CONTAINER_COLUMN};
                    HashMap<String, Integer> stagingZone = getColumnHeadingMap(stagingZoneList);
                    String zone = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(stagingZone.get(STAGING_ZONE_COLUMN) + 1)));
                    stagingZonesForBulkStaging.put(zone, baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(stagingZone.get(TEMP_TYPE_COLUMN) + 1))));
                    testOutputData.put(ExcelUtils.BULK_STAGING_CONTAINER_MAP, String.valueOf(stagingZonesForBulkStaging.toString()));
                    ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
                }
            }
        }
    }

    public HashMap<String, Integer> openOrderAndValidateDetailsOnCue(HashMap<String, String> containerMap, HashMap<String, String> testOutputData) {
        getVisualOrderIdAndOpenDetails();
        String[] columnHeadingList = {CONTAINER_COLUMN, ITEMS_COLUMN, PROGRESS_COLUMN, STATUS_COLUMN, TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeadingMap = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        validateDetailsOnCue(containerMap, testOutputData);
        return columnHeadingMap;
    }

    public HashMap<String, String> getContainerDetails(HashMap<String, Integer> columnHeadingMap, int i) {
        HashMap<String, String> containerDetails = new HashMap<>();
        for (Map.Entry<String, Integer> columnHeading : columnHeadingMap.entrySet()) {
            containerDetails.put(columnHeading.getKey(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.getValue() + 1))));
        }
        return containerDetails;
    }

    /*** Verifies container status, progress and items picked count after Picking */
    public void verifyContainersAfterPicking(String scenario, HashMap<String, String> containerMap, HashMap<String, String> testOutputData) {
        HashMap<String, Integer> columnHeadingMap = openOrderAndValidateDetailsOnCue(containerMap, testOutputData);
        HashMap<String, String> updatedTestOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            HashMap<String, String> containerDetails = getContainerDetails(columnHeadingMap, i);
            String[] itemCount = containerDetails.get(ITEMS_COLUMN).split("/");
            if (!containerDetails.get(PROGRESS_COLUMN).equals("100%") && containerDetails.get(CONTAINER_COLUMN).equalsIgnoreCase(updatedTestOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER))) {
                HashMap<String, String> notReadyContainer = new HashMap<>();
                String notReadyContainerId = containerDetails.get(CONTAINER_COLUMN);
                notReadyContainer.put(notReadyContainerId, containerDetails.get(TEMP_TYPE_COLUMN));
                testOutputData.put(ExcelUtils.NOT_READY_CONTAINER, String.valueOf(notReadyContainer));
                if (!testOutputData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER)) {
                    Assert.assertEquals(containerDetails.get(PROGRESS_COLUMN), CUE_NR_CONTAINER_PICKED_PROGRESS, incorrectContainerProgress);
                }
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
                if (testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER).contains(notReadyContainerId)) {
                    HashMap<String, String> containerMapForMultipleUpc = new HashMap<>(containerMap);
                    testOutputData.put(ExcelUtils.MULTI_CONTAINER_MAP_WITH_NR_ITEM, String.valueOf(containerMapForMultipleUpc));
                }
                containerMap.remove(containerDetails.get(CONTAINER_COLUMN));
                testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
                notReady++;
            } else if (itemCount[0].equals("0")) {
                containerMap.remove(containerDetails.get(CONTAINER_COLUMN));
                testOutputData.put(ExcelUtils.CONTAINER_MAP, String.valueOf(containerMap));
                Assert.assertEquals(containerDetails.get(PROGRESS_COLUMN), CUE_CONTAINER_PICKED_PROGRESS, incorrectContainerProgress);
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_OUT_OF_STOCK, incorrectContainerStatus);
                outOfStockCheck++;
            } else if (Integer.parseInt(itemCount[0]) == (Integer.parseInt(itemCount[1]) - 1)) {
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectShortedContainerStatus);
                shortedCheck++;
            } else {
                if (!testOutputData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER)) {
                    Assert.assertEquals(Integer.parseInt(itemCount[0]), Integer.parseInt(itemCount[1]), incorrectItemPickedCount);
                }
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
            }
            ExcelUtils.writeToExcel(scenario, testOutputData);
        }
    }

    /*** Verifies pcl container status, progress and items picked count after Picking */
    public void verifyPclContainersAfterPicking(HashMap<String, String> containerMap, HashMap<String, String> testOutputData, String scenario) {
        List<HashMap<String, String>> itemData = ExcelUtils.getItemUpcData(scenario);
        boolean multipleSubstitution = false;
        for (HashMap<String, String> itemDetails : itemData) {
            if (itemDetails.containsKey(ExcelUtils.SUBSTITUTED_ITEM_UPC) && !itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC).isEmpty()) {
                if (itemDetails.get(ExcelUtils.SUBSTITUTED_ITEM_UPC).contains(",")) {
                    multipleSubstitution = true;
                }
            }
        }
        HashMap<String, Integer> columnHeadingMap = openOrderAndValidateDetailsOnCue(containerMap, testOutputData);
        HashMap<String, String> updatedTestOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            HashMap<String, String> containerDetails = getContainerDetails(columnHeadingMap, i);
            String[] itemCount = containerDetails.get(ITEMS_COLUMN).split("/");
            if (!containerDetails.get(PROGRESS_COLUMN).equals("100%") && containerDetails.get(CONTAINER_COLUMN).equalsIgnoreCase(updatedTestOutputData.get(ExcelUtils.PRE_WEIGHED_CONTAINER))) {
                HashMap<String, String> notReadyContainer = new HashMap<>();
                String notReadyContainerId = containerDetails.get(CONTAINER_COLUMN);
                String pclIdTemp = testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
                notReadyContainer.put(notReadyContainerId, containerDetails.get(TEMP_TYPE_COLUMN));
                testOutputData.put(ExcelUtils.NOT_READY_CONTAINER, String.valueOf(notReadyContainer));
                ExcelUtils.writeToExcel(scenario, testOutputData);
                if (!testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES).contains(notReadyContainerId))
                    if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.BATCHING_SINGLE_TROLLEY_SISTER_STORE)))
                        try {
                            Assert.assertEquals(containerDetails.get(PROGRESS_COLUMN), CUE_NR_CONTAINER_PICKED_PROGRESS, incorrectContainerProgress);
                        } catch (AssertionError e) {
                            Assert.assertNotEquals(containerDetails.get(PROGRESS_COLUMN), "100%");
                        }
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.SC_ITEMS_MT_SCALE))) {
                    HashMap<String, String> pclIdTempMap = ExcelUtils.convertStringToMap(pclIdTemp);
                    containerMap.remove(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + containerDetails.get(CONTAINER_COLUMN));
                    testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTempMap));
                    testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(containerMap));
                    ExcelUtils.writeToExcel(scenario, testOutputData);
                    notReady++;
                }
            } else if (itemCount[0].equals("0")) {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.MOVEMENT_TO_OOS)))
                    containerMap.remove(testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + containerDetails.get(CONTAINER_COLUMN));
                String pclIdTemp = testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
                HashMap<String, String> pclIdTempMap = ExcelUtils.convertStringToMap(pclIdTemp);
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.MOVEMENT_TO_OOS)))
                    pclIdTempMap.remove(containerDetails.get(CONTAINER_COLUMN));
                testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTempMap));
                testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(containerMap));
                ExcelUtils.writeToExcel(scenario, testOutputData);
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_OUT_OF_STOCK, incorrectContainerStatus);
                outOfStockCheck++;
            } else if (Integer.parseInt(itemCount[0]) <= (Integer.parseInt(itemCount[1]) - 1)) {
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
                shortedCheck++;
            } else {
                if (!multipleSubstitution) {
                    Assert.assertEquals(Integer.parseInt(itemCount[0]), Integer.parseInt(itemCount[1]), incorrectItemPickedCount);
                }
                Assert.assertEquals(containerDetails.get(STATUS_COLUMN), CUE_CONTAINER_PICKED, incorrectContainerStatus);
            }
        }
    }

    public void verifyOutOfStockContainerNotStaged(HashMap<String, String> testOutputData) {
        getVisualOrderIdAndOpenDetails();
        List<String> stagingStatus = validateAfterStagingCompletion(testOutputData);
        int count = 0;
        for (String status : stagingStatus) {
            if (status.equals("—")) {
                count++;
            }
        }
        Assert.assertEquals(count, 1, incorrectContainerStatus);
    }

    public void verifyOutOfStockPclContainerNotStaged(HashMap<String, String> testOutputData) {
        getVisualOrderIdAndOpenDetails();
        List<String> stagingStatus = validateAfterPclStagingCompletion(testOutputData);
        int count = 0;
        for (String status : stagingStatus) {
            if (status.equals("—")) {
                count++;
            }
        }
        Assert.assertEquals(count, 1, incorrectContainerStatus);
    }

    /*** Verifies container status after Staging */
    public List<String> validateAfterStagingCompletion(HashMap<String, String> testOutputData) {
        List<String> stagingStatus = new ArrayList<>();
        String[] columnHeadingList = {STATUS_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.NON_PCL_IS_OOS_SHORT))) {
                stagingStatus.add(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))));
            } else {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.ITEM_MOVEMENT_OOS_PICKED_ITEMS))) {
                    Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))), CUE_CONTAINER_STAGED);
                }
            }
        }
        baseCommands.browserBack();
        return stagingStatus;
    }

    /*** Verifies container status after Staging */
    public List<String> validateAfterPclStagingCompletion(HashMap<String, String> testOutputData) {
        HashMap<String, String> stagingZonesForBulkStaging = new HashMap<>();
        List<String> stagingStatus = new ArrayList<>();
        String[] columnHeadingList = {STATUS_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT))) {
                stagingStatus.add(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))));
            } else {
                Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))), CUE_CONTAINER_STAGED);
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
            StringBuilder rejectedItemPclContainer = new StringBuilder();
            String[] stagingZoneList = {STAGING_ZONE_COLUMN, TEMP_TYPE_COLUMN, CONTAINER_COLUMN};
            HashMap<String, Integer> stagingZone = getColumnHeadingMap(stagingZoneList);
            baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
            for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
                String zone = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(stagingZone.get(STAGING_ZONE_COLUMN) + 1)));
                if (!Objects.equals(zone, "—")) {
                    if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER))) {
                        String rejectedContainers = testOutputData.get(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER);
                        String containerFromCue = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf((stagingZone.get(CONTAINER_COLUMN) + 1))));
                        if (rejectedContainers.contains(containerFromCue)) {
                            rejectedItemPclContainer.append(zone).append(",");
                        }
                    }
                    stagingZonesForBulkStaging.put(zone, baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(stagingZone.get(TEMP_TYPE_COLUMN) + 1))));
                }
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REJECTED_ITEMS_MULTIPLE_CONTAINER))) {
                testOutputData.put(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER, rejectedItemPclContainer.toString());
            }
            testOutputData.put(ExcelUtils.BULK_STAGING_CONTAINER_MAP, String.valueOf(stagingZonesForBulkStaging.toString()));
            ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
        }
        baseCommands.browserBack();
        return stagingStatus;
    }


    /*** @param amountProcessed amount processed after checkout from Ciao */
    public void verifyTotalAfterCheckout(String amountProcessed) {
        String cueAmount = baseCommands.getElementText(cueOrderDetailsMap.orderDetailsSection()).split("\\$")[1].split("[\\r\\n]+")[0];
        if (!cueAmount.equals(amountProcessed.substring(1))) {
            baseCommands.webpageRefresh();
            cueAmount = baseCommands.getElementText(cueOrderDetailsMap.orderDetailsSection()).split("\\$")[1].split("[\\r\\n]+")[0];
        }
        Assert.assertEquals(cueAmount, amountProcessed.substring(1), "Cue amount paid does not match with amount processed in Ciao");
    }

    public HashMap<String, String> getAllContainerTrolleyDetails() {
        HashMap<String, String> containerMap = new HashMap<>();
        List<String> columnHeadingList = new ArrayList<>();
        columnHeadingList.add("Temp Type");
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerRowList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < containerRowList.size(); i++) {
            List<WebElement> containerColumnList = baseCommands.elements(cueOrderDetailsMap.containerColumnList());
            for (int j = 0; j < containerColumnList.size(); j++) {
                for (String columnHeading : columnHeadingList) {
                    if (containerColumnList.get(j).getText().equalsIgnoreCase(columnHeading)) {
                        containerMap.put(containerRowList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(j + 1))));
                    }
                }
            }
        }
        return containerMap;
    }

    public void validateFromContainerStagingCompletion(String fromContainerLabel) {
        String[] columnHeadingList = {CONTAINER_COLUMN, STATUS_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            if (baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(CONTAINER_COLUMN) + 1))).equals(fromContainerLabel)) {
                Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))), CUE_CONTAINER_STAGED);
                break;
            }
        }
    }

    public void validateToContainerStagingCompletion(String toContainerLabel) {
        String[] columnHeadingList = {CONTAINER_COLUMN, STATUS_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < baseCommands.elements(cueOrderDetailsMap.containerRowList()).size(); i++) {
            if (baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(CONTAINER_COLUMN) + 1))).equals(toContainerLabel)) {
                Assert.assertEquals(baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(STATUS_COLUMN) + 1))), CUE_CONTAINER_STAGED);
                break;
            }
        }
    }

    public HashMap<String, String> getReuseStagedPclLabelsForContainers(String scenario, HashMap<String, String> testOutputData) {
        HashMap<String, String> pclLabelTemperatureMap = new HashMap<>();
        HashMap<String, String> pclIdTemperatureMap = new HashMap<>();
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        HashMap<String, String> containerMapPcl = new HashMap<>();
        List<String> multiItemContainer = new ArrayList<>();
        List<WebElement> trolleyIdList = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        for (int i = 0; i < containerIdList.size(); i++) {
            pclLabelGenerated = generateStagedPclContainerLabel(scenario, columnHeading, i);
            if (testOutputData.containsKey(ExcelUtils.MULTI_UPC_CONTAINER)) {
                if (testOutputData.get(ExcelUtils.MULTI_UPC_CONTAINER).contains(containerIdList.get(i).getText())) {
                    String pclLabelForMultiContainer = containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated);
                    multiItemContainer.add(pclLabelForMultiContainer);
                }
            }
            containerMapPcl.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), trolleyIdList.get(i).getText());
            getMultipleContainerTrolley(containerMapPcl, testOutputData);
            pclLabelTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
            pclIdTemperatureMap.put(containerIdList.get(i).getText().replace(containerIdList.get(i).getText(), pclLabelGenerated).substring(6), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
            trolleyTemperatureMap.put(trolleyIdList.get(i).getText(), baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))));
        }
        testOutputData.put(ExcelUtils.MULTI_UPC_CONTAINER_PCL_VALUES, String.valueOf(multiItemContainer));
        testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclLabelTemperatureMap));
        testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
        return containerMapPcl;
    }

    public String generateStagedPclContainerLabel(String scenario, HashMap<String, Integer> columnHeading, int i) {
        String pclLabel;
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> testData1 = ExcelUtils.convertStringToMap(testData.get(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP));
        ArrayList<String> usedPcl = new ArrayList<>();
        for (Map.Entry<String, String> entry : testData1.entrySet()) {
            {
                if (entry.getValue().equals("REFRIGERATED")) usedPcl.addFirst(entry.getKey());
            }
        }
        if (baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))).equalsIgnoreCase("REFRIGERATED")) {
            pclLabel = usedPcl.getFirst();
        } else if (baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1))).equalsIgnoreCase("FROZEN")) {
            pclLabel = testData.get(ExcelUtils.STORE_DIVISION_ID) + testData.get(ExcelUtils.STORE_LOCATION_ID) + "FL" + PermanentContainerLabelHelper.generateRandomPclNumber();
        } else {
            pclLabel = testData.get(ExcelUtils.STORE_DIVISION_ID) + testData.get(ExcelUtils.STORE_LOCATION_ID) + "PL" + PermanentContainerLabelHelper.generateRandomPclNumber();
        }
        return pclLabel;
    }

    public void updateMapsForFlexPicking(HashMap<String, String> tesOutputData) {
        HashMap<String, String> containerMap;
        if (Boolean.parseBoolean(tesOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
            containerMap = ExcelUtils.convertStringToMap(tesOutputData.get(ExcelUtils.PCL_LABEL_TROLLEY_MAP));
        } else {
            containerMap = ExcelUtils.convertStringToMap(tesOutputData.get(ExcelUtils.CONTAINER_MAP));
        }
        HashMap<String, String> deMeterPickingMap = new HashMap<>();
        HashMap<String, String> notPickedDeMeterMap = new HashMap<>();
        for (Map.Entry<String, String> temperatureType : containerMap.entrySet()) {
            if (!Boolean.parseBoolean(tesOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
                if (!temperatureType.getKey().contains(Constants.PickCreation.OVERSIZE)) {
                    if (temperatureType.getValue().equals(ExcelUtils.AMBIENT) || temperatureType.getValue().equals(ExcelUtils.REFRIGERATED)) {
                        deMeterPickingMap.put(temperatureType.getKey(), temperatureType.getValue());
                    } else {
                        notPickedDeMeterMap.put(temperatureType.getKey(), temperatureType.getValue());
                    }
                } else {
                    notPickedDeMeterMap.put(temperatureType.getKey(), temperatureType.getValue());
                }
            } else {
                if (temperatureType.getKey().contains("PL")) {
                    deMeterPickingMap.put(temperatureType.getKey(), temperatureType.getValue());
                } else {
                    notPickedDeMeterMap.put(temperatureType.getKey(), temperatureType.getValue());
                }
            }
        }
        if (Boolean.parseBoolean(tesOutputData.get(ExcelUtils.IS_END_JOB_EARLY))) {
            updateMapsForEarlyJobEnd(tesOutputData, deMeterPickingMap, notPickedDeMeterMap);
        } else {
            tesOutputData.put(ExcelUtils.DE_METER_PICKING_MAP, String.valueOf(deMeterPickingMap));
            tesOutputData.put(ExcelUtils.DE_METER_CONTAINER_MAP, String.valueOf(notPickedDeMeterMap));
        }
    }

    public void getTrolleyTemperatureMapForFlex(HashMap<String, String> testOutputData) {
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        List<WebElement> trolleyIdList = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        String[] columnHeadingList = {TEMP_TYPE_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        HashMap<String, String> trolleyTemperatureMap = new HashMap<>();
        for (int i = 0; i < containerIdList.size(); i++) {
            String temperature = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
            if (!containerIdList.get(i).getText().contains(Constants.PickCreation.OVERSIZE)) {
                if (temperature.equalsIgnoreCase(ExcelUtils.AMBIENT) || temperature.equalsIgnoreCase(ExcelUtils.REFRIGERATED)) {
                    trolleyTemperatureMap.put(trolleyIdList.get(i).getText(), temperature);
                }
            }
            testOutputData.put(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP, String.valueOf(trolleyTemperatureMap));
        }
    }

    public void validateTrolleyNotGenerated(HashMap<String, String> testOutputData) {
        pickingServicesHelper.verifyTrolleyNotGenerated(testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    public void validateTrolleyGenerated(HashMap<String, String> testOutputData) {
        pickingServicesHelper.verifyTrolleyGenerated(testOutputData.get(ExcelUtils.ORDER_NUMBER));
    }

    public void updateMapsForEarlyJobEnd(HashMap<String, String> testOutputData, HashMap<String, String> deMeterPickingMap, HashMap<String, String> notPickedDeMeterMap) {
        HashMap<String, String> trolleyTemperatureMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP));
        Iterator<Map.Entry<String, String>> iteratorForTrolley = trolleyTemperatureMap.entrySet().iterator();
        Map.Entry<String, String> lastTrolley = null;
        while (iteratorForTrolley.hasNext()) {
            lastTrolley = iteratorForTrolley.next();
        }
        if (lastTrolley != null) {
            trolleyNotToPick = lastTrolley.getKey();
            trolleyTemperatureMap.remove(trolleyNotToPick);
        }
        for (Map.Entry<String, String> deMeterPicking : deMeterPickingMap.entrySet()) {
            if (deMeterPicking.getValue().equals(trolleyNotToPick)) {
                notPickedDeMeterMap.put(deMeterPicking.getKey(), deMeterPicking.getValue());
                deMeterPickingMap.remove(deMeterPicking.getKey());
                testOutputData.remove(ExcelUtils.DE_METER_PICKING_MAP);
                testOutputData.remove(ExcelUtils.DE_METER_CONTAINER_MAP);
                testOutputData.put(ExcelUtils.DE_METER_PICKING_MAP, String.valueOf(deMeterPickingMap));
                testOutputData.put(ExcelUtils.DE_METER_CONTAINER_MAP, String.valueOf(notPickedDeMeterMap));
                break;
            }
        }
        testOutputData.remove(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP);
        testOutputData.put(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP, String.valueOf(trolleyTemperatureMap));
    }

    public void performManualBatchingForRequiredTimeSlot(HashMap<String, String> testOutputData) {
        try {
            pickingServicesHelper.pickRunBatchPostApi(testOutputData.get(ExcelUtils.STORE_ID), Integer.parseInt(testOutputData.get(ExcelUtils.DAYS_FROM_TODAY)));
            if (baseCommands.elementDisplayed(cueOrderDetailsMap.timeSlotLevelCheckBox())) {
                selectCheckBox();
                clickManualBatchingButton();
                confirmManualBatching();
                waitForMessageInvisibility();
            }
        } catch (Exception e) {
            baseCommands.webpageRefresh();
        }
    }

    public void performManualBatchingForRushOrderAndNormalOrder() {
        clickManualBatchingButton();
        confirmManualBatching();
        waitForMessageInvisibility();
    }

    public void selectCheckBox() {
        if (baseCommands.elementDisplayed(cueOrderDetailsMap.timeSlotLevelCheckBox())) {
            baseCommands.click(cueOrderDetailsMap.timeSlotLevelCheckBox());
        }
    }

    public void clickManualBatchingButton() {
        baseCommands.waitForElement(cueOrderDetailsMap.manualBatchingButton());
        baseCommands.click(cueOrderDetailsMap.manualBatchingButton());
    }

    public void confirmManualBatching() {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.confirmManualBatching());
        baseCommands.click(cueOrderDetailsMap.confirmManualBatching());
    }

    public void waitForMessageInvisibility() {
        baseCommands.waitForElementInvisible(cueOrderDetailsMap.batchingMessage());
    }

    public HashMap<String, String> getPickupTime(HashMap<String, String> testOutputData) {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.pickupTime());
        testOutputData.put(ExcelUtils.PICKUP_TIME, baseCommands.getElementText(cueOrderDetailsMap.pickupTime()));
        return testOutputData;
    }

    public void verifyTrolleyDetailsForCollapseTempAll(HashMap<String, String> testOutPutData) {
        String[] columnHeadingList = {TROLLEY_NUMBER_COLUMN, TEMP_TYPE_COLUMN, CONTAINER_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        String frozenTrolley = "";
        String ambientTrolley = "";
        String refrigeratedTrolley = "";
        String oversizeTrolley = "";
        for (int i = 0; i < containerIdList.size(); i++) {
            String temperatureType = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
            String containerNo = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(CONTAINER_COLUMN) + 1)));
            if (containerNo.startsWith(Constants.PickCreation.OVERSIZE)) {
                oversizeTrolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            } else if (temperatureType.startsWith(Constants.PickCreation.AMBIENT)) {
                ambientTrolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            } else if (temperatureType.startsWith(Constants.PickCreation.REFRIGERATED) && (!(containerNo.startsWith(Constants.PickCreation.OVERSIZE)))) {
                refrigeratedTrolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            } else if (temperatureType.startsWith(Constants.PickCreation.FROZEN)) {
                frozenTrolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            }
        }
        if (Boolean.parseBoolean(testOutPutData.get(ExcelUtils.IS_COLLAPSE_TEMP_AMRE_ORDER))) {
            Assert.assertEquals(ambientTrolley, refrigeratedTrolley, AmbientAndRefrigeratedTrolleysAreNotSame);
            Assert.assertNotEquals(frozenTrolley, oversizeTrolley, FrozenAndOverSizedTrolleysAreSame);
        } else {
            Assert.assertEquals(ambientTrolley, refrigeratedTrolley, AmbientAndRefrigeratedTrolleysAreNotSame);
            Assert.assertEquals(refrigeratedTrolley, frozenTrolley, FrozenAndRefrigeratedTrolleysAreNotSame);
            Assert.assertEquals(frozenTrolley, oversizeTrolley, FrozenAndOverSizedTrolleysAreNotSame);
        }
    }

    public void openTrolleyPage() {
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.trolleyRowList());
        baseCommands.click(cueOrderDetailsMap.trolleyRowList());
    }

    public void verifyCollapseAllTrolley() {
        List<WebElement> trolleyIds = baseCommands.elements(cueOrderDetailsMap.trolleyRowList());
        List<String> trolley = new ArrayList<>();
        for (WebElement trolleyId : trolleyIds) {
            trolley.add(trolleyId.getText());
        }
        boolean allEqual = trolley.stream().distinct().limit(2).count() <= 1;
        Assert.assertTrue(allEqual);
    }

    public void verifyEnhancedBatchingTrolleys(HashMap<String, List<String>> trolleyTemperatureListMap, String trolleyCombinationType, boolean isDBOSEnabled, int totalOSContainers) {
        Set<String> ambientTrolley = new HashSet<>(trolleyTemperatureListMap.get("Ambient trolleys"));
        Set<String> frozenTrolley = new HashSet<>(trolleyTemperatureListMap.get("Frozen trolley"));
        Set<String> refrigeratedTrolley = new HashSet<>(trolleyTemperatureListMap.get("Refrigerated trolley"));
        Set<String> overSizeTrolley = new HashSet<>(trolleyTemperatureListMap.get("Oversize trolley"));
        Set<String> smallOverSizeTrolley = new HashSet<>();
        if (trolleyTemperatureListMap.get("Small Oversize trolley") != null)
            smallOverSizeTrolley = new HashSet<>(trolleyTemperatureListMap.get("Small Oversize trolley"));
        HashMap<String, Set<String>> trolleyValidationMap = new HashMap<>();
        trolleyValidationMap.put(Constants.AM_TROLLEY, ambientTrolley);
        trolleyValidationMap.put(Constants.RE_TROLLEY, refrigeratedTrolley);
        trolleyValidationMap.put(Constants.FR_TROLLEY, frozenTrolley);
        trolleyValidationMap.put(Constants.OS_TROLLEY, overSizeTrolley);
        trolleyValidationMap.put(Constants.SMOS_TROLLEY, smallOverSizeTrolley);
        switch (trolleyCombinationType.toUpperCase()) {
            case "COLLAPSE_NONE":
                for (String trolley : ambientTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), ambientFrozenTrolleyMessage);
                    Assert.assertFalse(refrigeratedTrolley.contains(trolley), ambientRefrigeratedTrolleyMessage);
                    Assert.assertFalse(overSizeTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                }
                for (String trolley : refrigeratedTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), refrigeratedFrozenTrolleyMessage);
                    Assert.assertFalse(overSizeTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                }
                for (String trolley : frozenTrolley) {
                    Assert.assertFalse(overSizeTrolley.contains(trolley), overSizeFrozenTrolleyMessage);
                }
                break;
            case "COLLAPSE_ALL":
                for (String trolley : ambientTrolley) {
                    Assert.assertTrue(frozenTrolley.contains(trolley), ambientFrozenTrolleyMessage);
                    Assert.assertTrue(refrigeratedTrolley.contains(trolley), ambientRefrigeratedTrolleyMessage);
                    Assert.assertTrue(overSizeTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                }
                for (String trolley : frozenTrolley) {
                    Assert.assertTrue(refrigeratedTrolley.contains(trolley), refrigeratedFrozenTrolleyMessage);
                    Assert.assertTrue(overSizeTrolley.contains(trolley), overSizeFrozenTrolleyMessage);
                }
                for (String trolley : refrigeratedTrolley) {
                    Assert.assertTrue(overSizeTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                }
                break;
            case "OSFR & AMRE":
                for (String trolley : overSizeTrolley) {
                    Assert.assertTrue(frozenTrolley.contains(trolley), "No" + overSizeFrozenTrolleyMessage);
                    Assert.assertFalse(ambientTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                    Assert.assertFalse(refrigeratedTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                }
                for (String trolley : ambientTrolley) {
                    Assert.assertTrue(refrigeratedTrolley.contains(trolley), "No" + ambientRefrigeratedTrolleyMessage);
                    Assert.assertFalse(overSizeTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                    Assert.assertFalse(frozenTrolley.contains(trolley), ambientFrozenTrolleyMessage);
                }
                for (String trolley : refrigeratedTrolley) {
                    Assert.assertFalse(overSizeTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                    Assert.assertFalse(frozenTrolley.contains(trolley), refrigeratedFrozenTrolleyMessage);
                }
                break;
            case "AMRE":
                for (String trolley : ambientTrolley) {
                    Assert.assertTrue(refrigeratedTrolley.contains(trolley), ambientRefrigeratedTrolleyMessage);
                }
                for (String trolley : overSizeTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), overSizeFrozenTrolleyMessage);
                }
                break;
            case "OSFR":
                for (String trolley : overSizeTrolley) {
                    Assert.assertTrue(frozenTrolley.contains(trolley), "No " + overSizeFrozenTrolleyMessage);
                    Assert.assertFalse(ambientTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                    Assert.assertFalse(refrigeratedTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                }
                for (String trolley : ambientTrolley) {
                    Assert.assertFalse(refrigeratedTrolley.contains(trolley), ambientRefrigeratedTrolleyMessage);
                    Assert.assertFalse(frozenTrolley.contains(trolley), ambientFrozenTrolleyMessage);
                }
                for (String trolley : refrigeratedTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), refrigeratedFrozenTrolleyMessage);
                }
                break;
            case "AM,RE & OSFR":
                for (String trolley : ambientTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), ambientFrozenTrolleyMessage);
                    Assert.assertFalse(refrigeratedTrolley.contains(trolley), ambientRefrigeratedTrolleyMessage);
                    Assert.assertFalse(overSizeTrolley.contains(trolley), ambientOverSizeTrolleyMessage);
                }
                for (String trolley : refrigeratedTrolley) {
                    Assert.assertFalse(frozenTrolley.contains(trolley), refrigeratedFrozenTrolleyMessage);
                    Assert.assertFalse(overSizeTrolley.contains(trolley), overSizeRefrigeratedTrolleyMessage);
                }
                for (String trolley : overSizeTrolley) {
                    Assert.assertTrue(frozenTrolley.contains(trolley), "No " + overSizeFrozenTrolleyMessage);
                }
            case "COLLAPSE_AMRE_OSFR_SMOS":
                if (!overSizeTrolley.isEmpty()) {
                    validateDyBOSTrolleyForNineTote(trolleyValidationMap, isDBOSEnabled, totalOSContainers);
                }
                validateSmallOversizeAMRETrolleyForNineTote(trolleyValidationMap);
                validateRETrolleyForNineTote(trolleyValidationMap, true);
                validateAMTrolleyForNineTote(trolleyValidationMap, true);
                break;
            case "COLLAPSE_AMRE_OS_FR_SMOS":
                if (!overSizeTrolley.isEmpty()) {
                    validateOSTrolleyForNineTote(trolleyValidationMap);
                }
                validateSmallOversizeAMRETrolleyForNineTote(trolleyValidationMap);
                validateRETrolleyForNineTote(trolleyValidationMap, true);
                validateAMTrolleyForNineTote(trolleyValidationMap, true);
                break;
            case "COLLAPSE_AM_RE_OS_FR_SMOS":
                if (!overSizeTrolley.isEmpty()) {
                    validateOSTrolleyForNineTote(trolleyValidationMap);
                }
                validateSmallOversizeAMTrolleyForNineTote(trolleyValidationMap);
                validateRETrolleyForNineTote(trolleyValidationMap, false);
                validateAMTrolleyForNineTote(trolleyValidationMap, false);
                break;
            case "COLLAPSE_AM_RE_OSFR_SMOS":
                if (!overSizeTrolley.isEmpty()) {
                    validateDyBOSTrolleyForNineTote(trolleyValidationMap, isDBOSEnabled, totalOSContainers);
                }
                validateSmallOversizeAMTrolleyForNineTote(trolleyValidationMap);
                validateRETrolleyForNineTote(trolleyValidationMap, false);
                validateAMTrolleyForNineTote(trolleyValidationMap, false);
                break;
        }
    }

    private void validateSmallOversizeAMTrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap) {
        for (String trolley : trolleyValidationMap.get(Constants.SMOS_TROLLEY)) {
            Assert.assertTrue(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), "No" + smallOversizeAMRETrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), smallOversizeAMRETrolleyMessage);
            if (!trolleyValidationMap.get(Constants.OS_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.OS_TROLLEY).contains(trolley), smallOversizeOSTrolleyMessage);
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), smallOversizeFRTrolleyMessage);
        }
    }

    private void validateOSTrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap) {
        for (String trolley : trolleyValidationMap.get(Constants.OS_TROLLEY)) {
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), overSizeFrozenTrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), ambientOverSizeTrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), overSizeRefrigeratedTrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.SMOS_TROLLEY).contains(trolley), smallOversizeOSFRTrolleyMessage);
        }
    }

    private void validateSmallOversizeAMRETrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap) {
        for (String trolley : trolleyValidationMap.get(Constants.SMOS_TROLLEY)) {
            Assert.assertTrue(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), "No" + smallOversizeAMRETrolleyMessage);
            Assert.assertTrue(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), "No" + smallOversizeAMRETrolleyMessage);
            if (!trolleyValidationMap.get(Constants.OS_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.OS_TROLLEY).contains(trolley), smallOversizeOSTrolleyMessage);
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), smallOversizeFRTrolleyMessage);
        }
    }

    private void validateAMTrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap, boolean isAMRECombined) {
        for (String trolley : trolleyValidationMap.get(Constants.AM_TROLLEY)) {
            if (!trolleyValidationMap.get(Constants.OS_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.OS_TROLLEY).contains(trolley), overSizeRefrigeratedTrolleyMessage);
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), refrigeratedFrozenTrolleyMessage);
            if (isAMRECombined)
                Assert.assertTrue(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), "No" + ambientRefrigeratedTrolleyMessage);
            else
                Assert.assertFalse(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), ambientRefrigeratedTrolleyMessage);
        }
    }

    private void validateRETrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap, boolean isAMRECombined) {
        for (String trolley : trolleyValidationMap.get(Constants.RE_TROLLEY)) {
            if (!trolleyValidationMap.get(Constants.OS_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.OS_TROLLEY).contains(trolley), overSizeRefrigeratedTrolleyMessage);
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty())
                Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), refrigeratedFrozenTrolleyMessage);
            if (isAMRECombined)
                Assert.assertTrue(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), "No" + ambientRefrigeratedTrolleyMessage);
            else
                Assert.assertFalse(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), ambientRefrigeratedTrolleyMessage);
        }
    }

    private void validateDyBOSTrolleyForNineTote(HashMap<String, Set<String>> trolleyValidationMap, boolean isDBOSEnabled, int totalOSContainers) {
        for (String trolley : trolleyValidationMap.get(Constants.OS_TROLLEY)) {
            if (!trolleyValidationMap.get(Constants.FR_TROLLEY).isEmpty()) {
                if (isDBOSEnabled && totalOSContainers > 18)
                    Assert.assertFalse(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), frozenContainerCombinedWithOSTrolleyWithDyBOSEnabled);
                else if (isDBOSEnabled)
                    Assert.assertTrue(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), "No" + frozenContainerCombinedWithOSTrolleyWithDyBOSEnabled);
                else
                    Assert.assertTrue(trolleyValidationMap.get(Constants.FR_TROLLEY).contains(trolley), "No" + overSizeFrozenTrolleyMessage);
            }
            Assert.assertFalse(trolleyValidationMap.get(Constants.AM_TROLLEY).contains(trolley), ambientOverSizeTrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.RE_TROLLEY).contains(trolley), overSizeRefrigeratedTrolleyMessage);
            Assert.assertFalse(trolleyValidationMap.get(Constants.SMOS_TROLLEY).contains(trolley), smallOversizeOSFRTrolleyMessage);
        }
    }

    public void verifyTemperatureCollapseForEnhancedBatching(HashMap<String, String> testOutputData, List<HashMap<String, String>> itemsList) {
        String tempCollapseType = testOutputData.get(ExcelUtils.ENHANCED_BATCHING_MAP);
        String maxSMOSCount = "0";
        if (testOutputData.containsKey(ExcelUtils.MAX_SMOS_COUNT))
            maxSMOSCount = testOutputData.get(ExcelUtils.MAX_SMOS_COUNT);
        String dbOSEnabled = "false";
        if (testOutputData.containsKey(ExcelUtils.IS_DB_OS_ENABLED))
            dbOSEnabled = testOutputData.get(ExcelUtils.IS_DB_OS_ENABLED);
        verifyTemperatureCollapseProperty(tempCollapseType, itemsList, Integer.parseInt(maxSMOSCount), Boolean.parseBoolean(dbOSEnabled));
    }

    private List<String> getListOfItemsBasedOnSize(List<HashMap<String, String>> itemsList) {
        List<String> listOfItems = new ArrayList<>();
        for (HashMap<String, String> itemDetails : itemsList) {
            if (itemDetails.get(ExcelUtils.ITEM_SIZE).equalsIgnoreCase(Constants.SMALL_OS_ITEM)) {
                listOfItems.add(itemDetails.get(ExcelUtils.ITEM_UPC));
            }
        }
        return listOfItems;
    }

    public void verifyTemperatureCollapseProperty(String tempCollapseType, List<HashMap<String, String>> itemsList, int maxSMOSCount, boolean isDBOSEnabled) {
        String[] columnHeadingList = {TROLLEY_NUMBER_COLUMN, TEMP_TYPE_COLUMN, CONTAINER_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        HashMap<String, List<String>> trolleyTemperatureListMap = new HashMap<>();
        List<String> ambientTrolleyList = new ArrayList<>();
        List<String> refrigeratedTrolleyList = new ArrayList<>();
        List<String> frozenTrolleyList = new ArrayList<>();
        List<String> overSizeTrolleyList = new ArrayList<>();
        List<String> smallOSTrolleyList = new ArrayList<>();
        List<String> OSContainers = new ArrayList<>();
        for (int i = 0; i < containerIdList.size(); i++) {
            String temperatureType = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TEMP_TYPE_COLUMN) + 1)));
            String container = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(CONTAINER_COLUMN) + 1)));
            String trolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            if (!container.startsWith(Constants.PickCreation.OVERSIZE)) {
                if (temperatureType.startsWith(Constants.PickCreation.AMBIENT)) {
                    ambientTrolleyList.add(trolley);
                }
                if (temperatureType.startsWith(Constants.PickCreation.FROZEN)) {
                    frozenTrolleyList.add(trolley);
                }
                if (temperatureType.startsWith(Constants.PickCreation.REFRIGERATED)) {
                    refrigeratedTrolleyList.add(trolley);
                }
            } else {
                if (tempCollapseType != null && tempCollapseType.contains("SMOS")) {
                    List<String> smallOSItemsList = getListOfItemsBasedOnSize(itemsList);
                    clickOnContainers(i + 1);
                    if (isSmallOSContainer(smallOSItemsList)) {
                        smallOSTrolleyList.add(trolley);
                    } else {
                        overSizeTrolleyList.add(trolley);
                        OSContainers.add(container);
                    }
                } else {
                    overSizeTrolleyList.add(trolley);
                }
            }
        }
        if (tempCollapseType != null && tempCollapseType.contains("SMOS") && smallOSTrolleyList.size() >= maxSMOSCount) {
            int totalSMOSContainers = smallOSTrolleyList.size();
            int largeOSContainers = 0;
            if (!overSizeTrolleyList.isEmpty())
                largeOSContainers = overSizeTrolleyList.size();
            for (int i = 0; i < containerIdList.size(); i++) {
                String trolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
                String container = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(CONTAINER_COLUMN) + 1)));
                if (container.startsWith(Constants.PickCreation.OVERSIZE) && (OSContainers.isEmpty() || !OSContainers.contains(container))) {
                    if (!ambientTrolleyList.contains(trolley)) {
                        smallOSTrolleyList.remove(trolley);
                        overSizeTrolleyList.add(trolley);
                    }
                }
            }
            Assert.assertEquals(smallOSTrolleyList.size(), maxSMOSCount, smallOSTrolleyList.size() + "SMOS combined with AMRE trolley");
            int pureOSContainers = totalSMOSContainers - smallOSTrolleyList.size() + largeOSContainers;
            if (!overSizeTrolleyList.isEmpty())
                Assert.assertEquals(overSizeTrolleyList.size(), pureOSContainers, pureOSContainers + " forms  OS trolley");
        }
        trolleyTemperatureListMap.put("Ambient trolleys", ambientTrolleyList);
        trolleyTemperatureListMap.put("Frozen trolley", frozenTrolleyList);
        trolleyTemperatureListMap.put("Refrigerated trolley", refrigeratedTrolleyList);
        trolleyTemperatureListMap.put("Oversize trolley", overSizeTrolleyList);
        trolleyTemperatureListMap.put("Small Oversize trolley", smallOSTrolleyList);
        int totalOSContainers = overSizeTrolleyList.size() + smallOSTrolleyList.size();
        verifyEnhancedBatchingTrolleys(trolleyTemperatureListMap, tempCollapseType, isDBOSEnabled, totalOSContainers);
    }

    private boolean isSmallOSContainer(List<String> smallOSItemsList) {
        String OSItemUpc = cueContainerDetailsPage.getOSItem();
        boolean isSmallOSItem = smallOSItemsList.contains(OSItemUpc);
        baseCommands.browserBack();
        return isSmallOSItem;
    }

    public List<String> getUniqueTrolleyDetails() {
        List<String> trolleyList = new ArrayList<>();
        String[] columnHeadingList = {TROLLEY_NUMBER_COLUMN};
        HashMap<String, Integer> columnHeading = getColumnHeadingMap(columnHeadingList);
        baseCommands.waitForElementVisibility(cueOrderDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueOrderDetailsMap.containerRowList());
        for (int i = 0; i < containerIdList.size(); i++) {
            String trolley = baseCommands.getElementText(cueOrderDetailsMap.containerDetails(String.valueOf(i + 1), String.valueOf(columnHeading.get(TROLLEY_NUMBER_COLUMN) + 1)));
            trolleyList.add(trolley);
        }
        return trolleyList.stream().distinct().collect(Collectors.toList());
    }

    public void openSpecificTrolley(String trolley) {
        baseCommands.click(cueOrderDetailsMap.openSpecificTrolley(trolley));
    }

    @SafeVarargs
    public static void verifyDBOSBatchingValidation(HashMap<String, String>... testData) {
        List<String> containerWithOSLabels = new ArrayList<>();
        Set<String> overSize = new HashSet<>();
        Set<String> frozen = new HashSet<>();
        HashMap<String, String> testOutputData;
        testOutputData = testData[0];
        HashMap<String, String> containerTrolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP));
        if (testData.length == 2) {
            containerTrolleyMap.putAll(ExcelUtils.convertStringToMap(testData[1].get(ExcelUtils.CONTAINER_MAP)));
        }
        for (Map.Entry<String, String> containerTrolley : containerTrolleyMap.entrySet()) {
            if (containerTrolley.getKey().startsWith(Constants.PickCreation.OVERSIZE)) {
                containerWithOSLabels.add(containerTrolley.getKey());
                overSize.add(containerTrolley.getKey().substring(8, 13));
            } else if (containerTrolley.getValue().startsWith(Constants.PickCreation.FROZEN_TEMP)) {
                frozen.add(containerTrolley.getKey().substring(7, 12));
            }
        }
        if (!overSize.isEmpty() && !frozen.isEmpty()) {
            validationOfDBOSTrolleys(overSize, frozen, containerWithOSLabels.size());
        } else {
            Assert.assertEquals(overSize.size(), 1, pureOverSizeTrolleyNotFoundError);
        }
    }

    public static void validationOfDBOSTrolleys(Set<String> oversizeTrolley, Set<String> frozenTrolley, int size) {
        for (String trolleyValue : oversizeTrolley) {
            if (size > Constants.MaxFrozenOversizeTrolleyCapacity) {
                Assert.assertFalse(frozenTrolley.contains(trolleyValue), OS_FR_CommonTrolleysNotFoundError);
            } else {
                Assert.assertTrue(frozenTrolley.contains(trolleyValue), OS_FR_CommonTrolleysNotFoundError);
            }
        }
    }

}
