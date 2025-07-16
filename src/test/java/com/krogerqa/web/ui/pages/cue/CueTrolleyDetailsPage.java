package com.krogerqa.web.ui.pages.cue;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.cue.CueTrolleyDetailsMap;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CueTrolleyDetailsPage {
    static String wrongAssociateMessage = "For dynamic batching trolleys, The associate must be system only.";
    static List<String> containerList = new ArrayList<>();
    private static CueTrolleyDetailsPage instance;
    BaseCommands baseCommands = new BaseCommands();
    CueTrolleyDetailsMap cueTrolleyDetailsMap = CueTrolleyDetailsMap.getInstance();

    private CueTrolleyDetailsPage() {
    }

    public synchronized static CueTrolleyDetailsPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueTrolleyDetailsPage.class) {
            if (instance == null) {
                instance = new CueTrolleyDetailsPage();
            }
        }
        return instance;
    }

    public List<String> getContainerList() {
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueTrolleyDetailsMap.containerRowList());
        for (WebElement webElement : containerIdList) {
            String container = webElement.getText();
            if (!containerList.contains(container)) {
                containerList.add(container);
            }
        }
        return containerList;
    }

    public HashMap<String, String> verifyTrolleyAssociateAndRouteSteps(String trolley, HashMap<String, String> testOutputData) {
        try {
            verifyTrolleyAssociateNames(trolley);
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            verifyTrolleyAssociateNames(trolley);
        }
        testOutputData = verifyTrolleyRouteSteps(trolley, testOutputData);
        baseCommands.browserBack();
        return testOutputData;
    }

    public void verifyTrolleyAssociateNames(String trolley) {
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.searchBox());
        baseCommands.enterText(cueTrolleyDetailsMap.searchBox(), trolley, true);
        baseCommands.waitForElementClickability(cueTrolleyDetailsMap.trolleyNumber(trolley));
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.trolleyNumber(trolley));
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.getAssociateText());
        String associateText = baseCommands.getElementText(cueTrolleyDetailsMap.getAssociateText());
        Assert.assertEquals(associateText, Constants.PickCreation.DB_ASSOCIATE_TEXT, wrongAssociateMessage);
    }

    public HashMap<String, String> verifyTrolleyRouteSteps(String trolley, HashMap<String, String> testOutputData) {
        int baseRouteStep, currentRouteStep;
        try {
            baseCommands.waitForElementClickability(cueTrolleyDetailsMap.trolleyNumber(trolley));
            baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.trolleyNumber(trolley));
            baseCommands.click(cueTrolleyDetailsMap.trolleyNumber(trolley));
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            baseCommands.waitForElementClickability(cueTrolleyDetailsMap.trolleyNumber(trolley));
            baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.trolleyNumber(trolley));
            baseCommands.click(cueTrolleyDetailsMap.trolleyNumber(trolley));
        }
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.trolleyDetails());
        int numberOfContainers = baseCommands.numberOfElements(cueTrolleyDetailsMap.numberOfContainers());
        if (numberOfContainers > 1) {
            testOutputData.put(ExcelUtils.COLLAPSE_TEMPERATURE_TROLLEY_CONTAINERS, String.valueOf(getContainerList()));
        }
        currentRouteStep = Integer.parseInt(baseCommands.getElementText(cueTrolleyDetailsMap.getRouteStep(1)));
        if (currentRouteStep != 999) {
            Assert.assertEquals(currentRouteStep, 1, "Route step must start from 1.");
        }
        baseRouteStep = currentRouteStep;
        for (int i = 2; i <= numberOfContainers; i++) {
            currentRouteStep = Integer.parseInt(baseCommands.getElementText(cueTrolleyDetailsMap.getRouteStep(i)));
            if (currentRouteStep == 999) {
                break;
            }
            Assert.assertEquals(baseRouteStep + 1, currentRouteStep, "Route step is not in proper sequence.");
            baseRouteStep = baseRouteStep + 1;
        }
        return testOutputData;
    }

    public List<String> getContainersForEachTrolley() {
        List<String> containers = new ArrayList<>();
        baseCommands.webpageRefresh();
        baseCommands.waitForElementVisibility(cueTrolleyDetailsMap.containerRowList());
        List<WebElement> containerIdList = baseCommands.elements(cueTrolleyDetailsMap.containerRowList());
        for (WebElement webElement : containerIdList) {
            String container = webElement.getText();
            containers.add(container);
        }
        return containers;
    }
}
