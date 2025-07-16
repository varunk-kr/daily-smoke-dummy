package com.krogerqa.web.ui.pages.deMeter;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.ui.maps.cue.CueFlexMap;
import com.krogerqa.web.ui.maps.deMeter.DeMeterPickingMap;
import com.krogerqa.web.ui.pages.cue.CueFlexPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class DeMeterPickingPage {
    static String enterBracodeManually = "Enter barcode manually";
    static String inProgressStatus = "IN PROGRESS";

    private static DeMeterPickingPage instance;
    DeMeterPickingMap deMeterPickingMap = DeMeterPickingMap.getInstance();
    CueFlexMap cueFlexMap = CueFlexMap.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    private DeMeterPickingPage() {
    }

    public synchronized static DeMeterPickingPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DeMeterPickingPage.class) {
            if (instance == null) {
                instance = new DeMeterPickingPage();
            }
        }
        return instance;
    }

    public void verifyNonPclFlexPickingWithDemeter(HashMap<String, String> testOutputData, String flexWindowHandle, String dmeterWindowHandle) {
        clickBeginButton();
        scanNonPclContainersForDeMeterPicking(testOutputData, flexWindowHandle, dmeterWindowHandle);
    }


    public void verifyPclFlexPickingWithDemeter(String scenario, HashMap<String, String> testOutputData, String flexWindowHandle, String deMeterWindowHandle) {
        clickBeginButton();
        scanPclContainersForDeMeterPicking(scenario, testOutputData, flexWindowHandle, deMeterWindowHandle);
    }

    private void clickBeginButton() {
        baseCommands.click(deMeterPickingMap.clickBeginButton());
    }


    public void scanPclContainersForDeMeterPicking(String scenario, HashMap<String, String> testOutputData, String flexWindowHandle, String deMeterWindowHandle) {
        HashMap<String, String> deMeterMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.DE_METER_PICKING_MAP));
        HashMap<String, String> harvesterMap = new HashMap<>();
        int i = 0;
        int j = 0;
        for (Map.Entry<String, String> trolley : deMeterMap.entrySet()) {
            moveToTrolleyAndStartPicking(trolley.getValue());
            scanContainer(trolley.getKey());
            tapFirstContainerLocationAndStartPicking();
            baseCommands.waitForElementVisibility(deMeterPickingMap.getUpc());
            if ((j++ == deMeterMap.entrySet().size() - 1)) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
                    testOutputData.put(ExcelUtils.TAKE_OVER_TROLLEY, trolley.getValue());
                    testOutputData.put(ExcelUtils.TAKE_OVER_CONTAINER_NO, trolley.getKey());
                    harvesterMap.put(trolley.getKey(), trolley.getValue());
                    testOutputData.put(ExcelUtils.DE_METER_CONTAINER_MAP, String.valueOf(harvesterMap));
                    ExcelUtils.writeToExcel(scenario, testOutputData);
                    break;
                }
            }
            String upc = baseCommands.getElementText(deMeterPickingMap.getUpc()).split(":")[1].trim();
            scanItem(upc, trolley.getKey());
            if (!(i++ == deMeterMap.entrySet().size() - 1)) {
                validateInProgessStatusWhilePicking(flexWindowHandle, deMeterWindowHandle);
                baseCommands.click(deMeterPickingMap.takeOthertrolley());
            }
        }
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKE_OVER_TROLLEY))) {
            endAssignmentEarly();
        }
    }

    public void scanNonPclContainersForDeMeterPicking(HashMap<String, String> testOutputData, String flexWindowHandle, String dmeterWindowHandle) {
        HashMap<String, String> dmeterMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.DE_METER_PICKING_MAP));
        int i = 0;
        for (String containerId : dmeterMap.keySet()) {
            scanContainer(containerId);
            baseCommands.waitForElementVisibility(deMeterPickingMap.getUpc());
            String upc = baseCommands.getElementText(deMeterPickingMap.getUpc()).split(":")[1].trim();
            scanItem(upc, containerId);
            if (!(i++ == dmeterMap.entrySet().size() - 1)) {
                validateInProgessStatusWhilePicking(flexWindowHandle, dmeterWindowHandle);
                baseCommands.click(deMeterPickingMap.takeOthertrolley());
            }
        }
        endAssignmentEarly();
    }

    public void moveToTrolleyAndStartPicking(String trolley) {
        int totalTrolleys = baseCommands.numberOfElements(deMeterPickingMap.totalTrolleysDisplayed());
        for (int i = 1; i <= totalTrolleys; i++) {
            if (baseCommands.getElementText(deMeterPickingMap.requiredTrolley(i)).contains(trolley)) {
                baseCommands.click(deMeterPickingMap.clickBeginTrolleyButton(i));
                if (i > 1) {
                    baseCommands.waitForElementVisibility(deMeterPickingMap.acceptPopup());
                    baseCommands.click(deMeterPickingMap.acceptPopup());
                }
                break;
            }
        }
    }

    private void endAssignmentEarly() {
        baseCommands.click(deMeterPickingMap.endAssignmentEarly());
        baseCommands.click(deMeterPickingMap.confirmFinishButton());
    }

    public void changeHiddenFieldOpacity(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) baseCommands.getWebDriver();
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "opacity: 1;");
    }

    public void scanContainer(String containerId) {
        changeHiddenFieldOpacity(baseCommands.element(deMeterPickingMap.hiddenInputField()));
        baseCommands.enterText(deMeterPickingMap.hiddenInputField(), containerId, true);
    }

    public void scanItem(String upc, String containerId) {
        baseCommands.click(deMeterPickingMap.selectOptions());
        selectEnterBarcodeManually(upc);
        baseCommands.click(deMeterPickingMap.selectOptions());
        selectEnterBarcodeManually(containerId);
        baseCommands.click(deMeterPickingMap.reviewSelected());
        baseCommands.waitForElementVisibility(deMeterPickingMap.endSelecting());
        baseCommands.click(deMeterPickingMap.endSelecting());
        baseCommands.waitForElementVisibility(deMeterPickingMap.confirmSelectingPopup());
        baseCommands.click(deMeterPickingMap.confirmSelectingPopup());
    }


    public void selectEnterBarcodeManually(String barcode) {
        for (int i = 1; i <= baseCommands.numberOfElements(deMeterPickingMap.optionsAvailable()); i++) {
            if (baseCommands.getElementText(deMeterPickingMap.selectEnterBarcodeManually(i)).equalsIgnoreCase(enterBracodeManually)) {
                baseCommands.click(deMeterPickingMap.selectEnterBarcodeManually(i));
                break;
            }
        }
        baseCommands.waitForElementVisibility(deMeterPickingMap.enterBarcodeField());
        baseCommands.click(deMeterPickingMap.enterBarcodeField());
        baseCommands.enterText(deMeterPickingMap.enterBarcodeField(), barcode, true);
        baseCommands.click(deMeterPickingMap.enterButton());
    }

    public void validateInProgessStatusWhilePicking(String flexWindowHandle, String dmeterWindowHandle) {
        baseCommands.getWebDriver().switchTo().window(flexWindowHandle);
        baseCommands.webpageRefresh();
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            if ((baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(CueFlexPage.flexNumberOfitems) &&
                    baseCommands.getElementText(cueFlexMap.checkShopperId(i)).equalsIgnoreCase(PropertyUtils.getDemeterUsername()))) {
                Assert.assertTrue(baseCommands.getElementText(cueFlexMap.flexJobStatus(i)).equalsIgnoreCase(inProgressStatus));
                break;
            }
        }
        baseCommands.getWebDriver().switchTo().window(dmeterWindowHandle);
    }

    private void tapFirstContainerLocationAndStartPicking() {
        for (int i = 1; i <= baseCommands.numberOfElements(deMeterPickingMap.totalLocations()); i++) {
            if (baseCommands.getElementText(deMeterPickingMap.firstLocation(i)).equals("1")) {
                baseCommands.click(deMeterPickingMap.firstLocation(i));
                break;
            }
        }
        baseCommands.click(deMeterPickingMap.tapSelectingButton());
    }
}
