package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoDeStagingMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import org.testng.Assert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CiaoDeStagingPage {

    private static CiaoDeStagingPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoDeStagingMap ciaoDeStagingMap = CiaoDeStagingMap.getInstance();
    String bulkStagingAssertionMessage = "Cue bulk staging location didn't match with ciao";

    private CiaoDeStagingPage() {
    }

    public synchronized static CiaoDeStagingPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoDeStagingPage.class) {
            if (instance == null) {
                instance = new CiaoDeStagingPage();
            }
        }
        return instance;
    }

    public void assertStagingLocationText(Set<String> containerIds) {
        try {
            Assert.assertTrue(containerIds.contains(mobileCommands.getElementText(ciaoDeStagingMap.stagingZoneText())));
        } catch (Exception | AssertionError e) {
            for (String container : containerIds) {
                for (char c : mobileCommands.getElementText(ciaoDeStagingMap.stagingZoneText()).toCharArray()) {
                    if (!container.contains(String.valueOf(c))) {
                        Assert.fail(bulkStagingAssertionMessage);
                    }
                }
            }
        }
    }

    public void assertContainerIdText(Set<String> containerIds, HashMap<String, String> testOutputData) {
        try {
            Assert.assertTrue(containerIds.contains(mobileCommands.getElementText(ciaoDeStagingMap.containerIdText())));
        } catch (AssertionError e) {
            if (testOutputData.containsKey(ExcelUtils.OVER_SIZED_CONTAINER) && !testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).equals("{}")) {
                String upcId = mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText());
                int retry = 150;
                while (retry > 0) {
                    if (upcId.contains("UPC")) {
                        break;
                    } else {
                        retry--;
                        upcId = mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText());
                    }
                }
                Assert.assertTrue(containerIds.contains(mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText()).replace("UPC: ", "").trim()));
            }
        }
    }

    public void assertContainerIdPclOversizeText(Set<String> containerIds, HashMap<String, String> testOutputData) {
        boolean isMultiSubScenario = testOutputData.containsKey(ExcelUtils.MULTIPLE_SUBS) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_SUBS));
        mobileCommands.waitForElementVisibility(ciaoDeStagingMap.containerIdText());
        String containerId = mobileCommands.getElementText(ciaoDeStagingMap.containerIdText());
        if (!isMultiSubScenario) {
            if (containerId.startsWith(Constants.PickCreation.PERMANENT_LABEL) || containerId.startsWith(Constants.PickCreation.FLOATING_LABEL)) {
                Assert.assertTrue(containerIds.contains(mobileCommands.getElementText(ciaoDeStagingMap.containerIdText())));
            } else {
                String upcId = mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText());
                int retry = 150;
                while (retry > 0) {
                    if (upcId.contains("UPC")) {
                        break;
                    } else {
                        retry--;
                        upcId = mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText());
                    }
                }
                Assert.assertTrue(containerIds.contains(mobileCommands.getElementText(ciaoDeStagingMap.oversizeUpcIdText()).replace("UPC: ", "").trim()));
            }
        }
    }

    public void removeFloatingLabels() {
        mobileCommands.tap(ciaoDeStagingMap.removeFloatingLabelButton());
    }

    public void tapContainerKebabMenu() {
        mobileCommands.tap(ciaoDeStagingMap.containerKebabMenu());
    }

    public void tapStagingZoneKebabMenu() {
        mobileCommands.tap(ciaoDeStagingMap.stagingZoneKebabMenu());
    }

    public void tapAddContainerManuallyOption() {
        mobileCommands.tap(ciaoDeStagingMap.addContainerManuallyOption());
    }

    public void selectLabelDamagedRadioButton() {
        mobileCommands.tap(ciaoDeStagingMap.labelDamagedRadioButton());
    }

    public void tapOkButton() {
        if (mobileCommands.elementDisplayed(ciaoDeStagingMap.dialogOkButton())) {
            mobileCommands.waitForElementVisibility(ciaoDeStagingMap.dialogOkButton());
            mobileCommands.tap(ciaoDeStagingMap.dialogOkButton());
        }
    }

    /* Success toast will not appear after deStaging the last container */
    public void verifyDeStageSuccessToast(int containerNumber, int totalContainers) {
        if (containerNumber < totalContainers) {
            try {
                mobileCommands.assertElementDisplayed(ciaoDeStagingMap.deStageSuccessToastMessage(), true);
            } catch (Exception | AssertionError e) {
                mobileCommands.waitForElementInvisible(ciaoDeStagingMap.deStageSuccessToastMessage());
            }
        }
    }

    public void selectContainersToDeStage(String containerId, String rejectedItemContainer) {
        if (rejectedItemContainer.contains(containerId)) {
            tapContainerKebabForRejectedItems();
        } else {
            tapContainerKebabMenu();
        }
        tapAddContainerManuallyOption();
        selectLabelDamagedRadioButton();
        tapOkButton();
    }

    private void tapContainerKebabForRejectedItems() {
        mobileCommands.tap(ciaoDeStagingMap.containerKebabMenuRejectedItems());
    }

    public void selectContainersToDeStage() {
        tapContainerKebabMenu();
        tapAddContainerManuallyOption();
        selectLabelDamagedRadioButton();
        tapOkButton();
    }

    public void selectStagingLocationsToDeStage(String stagingZone) {
        tapStagingZoneKebabMenu();
        tapAddContainerManuallyOption();
        selectLabelDamagedRadioButton();
        tapOkButton();
        if (!stagingZone.startsWith("A")) {
            tapOkButton();
        }
    }

    public void deStageContainer(String scenario, HashMap<String, String> containerMap) {
        HashMap<String, String> testOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
        if (scenario.equals(ExcelUtils.SCENARIO_3)) {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, ExcelUtils.SCENARIO_3);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            containerMap = ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.CONTAINER_MAP));
        }
        Set<String> containerIds = containerMap.keySet();
        int size = containerIds.size();
        containerIds = updateContainerMapForPclOverSizeDeStaging(containerIds, testOutputData);
        if (!testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).equals("{}") && ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER)).size() > 1) {
            size = size + ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER)).size() - 1;
        }
        for (int i = 1; i <= size; i++) {
            assertContainerIdText(containerIds, testOutputData);
            selectContainersToDeStage();
            if (!scenario.equals(ExcelUtils.SCENARIO_3))
                verifyDeStageSuccessToast(i, containerIds.size());
        }
    }

    public Set<String> updateContainerMapForPclOverSizeDeStaging(Set<String> containerIds, HashMap<String, String> testOutputData) {
        Set<String> overSizeUpcs = new HashSet<>();
        Set<String> overSizedContainer = new HashSet<>();
        for (String container : containerIds) {
            if (container.contains(Constants.PickCreation.OVERSIZE)) {
                overSizedContainer.add(container);
                for (Map.Entry<String, String> oversizeUpc : ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER)).entrySet()) {
                    overSizeUpcs.add(oversizeUpc.getValue());
                }
            }
        }
        containerIds.removeAll(overSizedContainer);
        containerIds = Stream.concat(containerIds.stream(), overSizeUpcs.stream()).collect(Collectors.toSet());
        return containerIds;
    }

    public void deStagePclContainer(HashMap<String, String> testOutputData, String scenario, HashMap<String, String> containerMap) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            containerMap = ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
        } else {
            testOutputData = ExcelUtils.getTestDataMap(scenario, scenario);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                    containerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.INSTACART_CONTAINER_MAP));
                } else {
                    containerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
                }
            }
        }
        Set<String> containerIds = containerMap.keySet();
        String totalContainersToDeStage = mobileCommands.getElementText(ciaoDeStagingMap.getNumberOfContainersToDeStage());
        int size = Integer.parseInt(((totalContainersToDeStage.split("\\("))[1].split("\\)"))[0]);
        containerIds = updateContainerMapForPclOverSizeDeStaging(containerIds, testOutputData);
        for (int i = 1; i <= size; i++) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                assertStagingLocationText(containerIds);
                String stagingZone = mobileCommands.getElementText(ciaoDeStagingMap.stagingZoneText());
                selectStagingLocationsToDeStage(stagingZone);
            } else {
                if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                    assertContainerIdPclOversizeText(containerIds, testOutputData);
                }
                selectContainersToDeStage();
            }
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT)) && !Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)))
                verifyDeStageSuccessToast(i, containerIds.size());
        }
    }

    public void removeRejectedItems() {
        mobileCommands.tap(ciaoDeStagingMap.removeManually());
        mobileCommands.tap(ciaoDeStagingMap.selectDropdown());
        mobileCommands.tap(ciaoDeStagingMap.noBarcodeAvailable());
        mobileCommands.tap(ciaoDeStagingMap.removeButton());
        mobileCommands.waitForElementVisibility(ciaoDeStagingMap.completeButton());
        mobileCommands.tap(ciaoDeStagingMap.completeButton());
    }

    public void deStageRejectedSubsContainer(HashMap<String, String> testOutputData, HashMap<String, String> containerMap, String rejectedItemContainer) {
        if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_MAP_WITH_NR_ITEM)) {
            containerMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_MAP_WITH_NR_ITEM));
        }
        Set<String> containerIds = containerMap.keySet();
        int size = containerIds.size();
        containerIds = updateContainerMapForPclOverSizeDeStaging(containerIds, testOutputData);
        if (!testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER).equals("{}") && ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER)).size() > 1) {
            size = size + ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.OVER_SIZED_CONTAINER)).size() - 1;
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
            String totalContainersToDeStage = mobileCommands.getElementText(ciaoDeStagingMap.getNumberOfContainersToDeStage());
            size = Integer.parseInt(((totalContainersToDeStage.split("\\("))[1].split("\\)"))[0]);
        }
        for (int i = 1; i <= size; i++) {
            String containerId;
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                assertStagingLocationText(containerIds);
                containerId = mobileCommands.getElementText(ciaoDeStagingMap.stagingZoneTextForReStaging(1));
                selectStagingLocationsToDeStage(containerId);
                removeRejectedItems();
            } else {
                if (mobileCommands.elementDisplayed(ciaoDeStagingMap.containerIdText())) {
                    assertContainerIdText(containerIds, testOutputData);
                    containerId = mobileCommands.getElementText(ciaoDeStagingMap.containerIdText());
                    selectContainersToDeStage(containerId, rejectedItemContainer);
                    if (rejectedItemContainer.contains(containerId)) {
                        removeRejectedItems();
                    }
                }
            }
            verifyDeStageSuccessToast(i, containerIds.size());
        }
    }

    public void scanPally() {
        if (mobileCommands.elementDisplayed(ciaoDeStagingMap.scanPallyButton())) {
            mobileCommands.waitForElementVisibility(ciaoDeStagingMap.scanPallyButton());
            mobileCommands.tap(ciaoDeStagingMap.scanPallyButton());
            mobileCommands.waitForElementVisibility(ciaoDeStagingMap.pallyNotNeededOption());
            mobileCommands.tap(ciaoDeStagingMap.pallyNotNeededOption());
            mobileCommands.waitForElementVisibility(ciaoDeStagingMap.tapOkButton());
            mobileCommands.tap(ciaoDeStagingMap.tapOkButton());
        }
    }
}
