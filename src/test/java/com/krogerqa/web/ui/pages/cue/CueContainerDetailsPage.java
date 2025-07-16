package com.krogerqa.web.ui.pages.cue;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.cue.CueContainerDetailsMap;
import org.testng.Assert;

import java.util.*;

public class CueContainerDetailsPage {

    public static HashMap<String, String> overSizeContainerUpcMap = new HashMap<>();
    static List<String> multiUpcContainers = new ArrayList<>();
    private static CueContainerDetailsPage instance;
    BaseCommands baseCommands = new BaseCommands();
    CueContainerDetailsMap cueContainerDetailsMap = CueContainerDetailsMap.getInstance();
    Set<String> upcs = new HashSet<>();

    private CueContainerDetailsPage() {
    }

    public synchronized static CueContainerDetailsPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueContainerDetailsPage.class) {
            if (instance == null) {
                instance = new CueContainerDetailsPage();
            }
        }
        return instance;
    }

    public void verifyItemsUpc(List<HashMap<String, String>> itemsList) {
        for (HashMap<String, String> itemDetailsMap : itemsList) {
            try {
                Assert.assertTrue(upcs.contains(itemDetailsMap.get(ExcelUtils.ITEM_UPC)), "Item with UPC: " + itemDetailsMap.get(ExcelUtils.ITEM_UPC) + " not found");
            } catch (Exception | AssertionError e) {
                break;
            }
        }
    }

    public void addItemsUpc(String containerText, HashMap<String, String> testOutputData) {
        baseCommands.webpageRefresh();
        baseCommands.waitForElementClickability(cueContainerDetailsMap.printContainerlabel());
        baseCommands.getElementText(cueContainerDetailsMap.printContainerlabel());
        baseCommands.waitForElementClickability(cueContainerDetailsMap.printPageButton());
        baseCommands.getElementText(cueContainerDetailsMap.printPageButton());
        baseCommands.waitForElement(cueContainerDetailsMap.itemRows());
        int totalItems = baseCommands.elements(cueContainerDetailsMap.itemRows()).size();
        for (int i = 1; i <= totalItems; i++) {
            baseCommands.waitForElement(cueContainerDetailsMap.itemDetails(Integer.toString(i), "3"));
            String upc = baseCommands.getElementText(cueContainerDetailsMap.itemDetails(Integer.toString(i), "3"));
            if (containerText.contains(Constants.PickCreation.OVERSIZE)) {
                overSizeContainerUpcMap.put(containerText, upc);
            }
            if (upc.endsWith("0000")) {
                testOutputData.put(ExcelUtils.PRE_WEIGHED_CONTAINER, containerText);
                ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
            }
            upcs.add(upc);
        }
        if (totalItems > 1) {
            multiUpcContainers.add(containerText);
        }
        testOutputData.put(ExcelUtils.MULTI_UPC_CONTAINER, String.valueOf(multiUpcContainers).replaceAll("\\[", "")
                .replaceAll("\\]", ""));
        testOutputData.put(ExcelUtils.OVER_SIZED_CONTAINER, String.valueOf(overSizeContainerUpcMap));
    }

    public String getOSItem() {
       return baseCommands.getElementText(cueContainerDetailsMap.itemDetails("1", "3"));
    }
}
