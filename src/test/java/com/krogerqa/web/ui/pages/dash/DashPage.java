package com.krogerqa.web.ui.pages.dash;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.dash.DashMap;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashPage {

    public static List<String> orderId = new ArrayList<>();
    private static DashPage instance;
    BaseCommands baseCommands = new BaseCommands();
    DashMap dashMap = DashMap.getInstance();

    private DashPage() {
    }

    public synchronized static DashPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DashPage.class) {
            if (instance == null) {
                instance = new DashPage();
            }
        }
        return instance;
    }

    public void getDivisionStoreText() {
        baseCommands.getElementText(dashMap.divisionStoreText());
    }

    public void verifyDashDetails(HashMap<String, String> testOutputData, Boolean postCheckOut) {
        baseCommands.getWebDriverWait();
        baseCommands.waitForElementInvisible(dashMap.loaderIcon());
        baseCommands.webpageRefresh();
        baseCommands.waitForElementInvisible(dashMap.loaderIcon());
        if (!postCheckOut) {
            if (baseCommands.elementDisplayed(dashMap.noIncomingCustomers())) {
                baseCommands.waitForElementInvisible(dashMap.noIncomingCustomers());
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
            baseCommands.webpageRefresh();
        }
        baseCommands.waitForElementVisibility(dashMap.todayOrderSection());
        baseCommands.waitForElementVisibility(dashMap.todayOrder());
        if (!postCheckOut) {
            List<WebElement> row;
            try {
                baseCommands.wait(5);
                baseCommands.waitForElementVisibility(dashMap.rowList());
                row = baseCommands.elements(dashMap.rowList());
            } catch(Exception | AssertionError e){
                baseCommands.wait(5);
                baseCommands.waitForElementVisibility(dashMap.rowList());
                row = baseCommands.elements(dashMap.rowList());
            }
            for (WebElement webElement : row) {
                String rowValue = webElement.getText();
                String[] values = rowValue.split("\\r?\\n");
                orderId.add(values[2]);
            }
        }
    }

    public void verifyDashAfterCustomerCheckIn(String visualOrderId) {
        baseCommands.getWebDriverWait();
        boolean val = orderId.contains(visualOrderId);
        Assert.assertTrue(val);
    }

    public void verifyOrderInDash(HashMap<String, String> testOutputData, String visualOrderId) {
        getDivisionStoreText();
        verifyDashDetails(testOutputData, false);
        verifyDashAfterCustomerCheckIn(visualOrderId);
    }

    public void verifyDashAfterDeStaging(HashMap<String, String> testOutputData, String visualOrderId, Boolean postCheckout) {
        verifyDashDetails(testOutputData, postCheckout);
        boolean val = orderId.equals(visualOrderId);
        Assert.assertFalse(val);
    }

    public void verifyInstacartOrderInDash(String visualOrderId) {
        getDivisionStoreText();
        baseCommands.getWebDriverWait();
        baseCommands.waitForElementInvisible(dashMap.loaderIcon());
        baseCommands.waitForElementVisibility(dashMap.todayOrderSection());
        baseCommands.waitForElementVisibility(dashMap.todayOrder());
        int totalOrdersInDash = baseCommands.numberOfElements(dashMap.totalInstacartOrders());
        boolean orderExist = false;
        baseCommands.wait(2);
        for (int i = 1; i <= totalOrdersInDash; i++) {
            String dashOrderId;
            try {
                baseCommands.wait(5);
                dashOrderId = baseCommands.getElementText(dashMap.readyForStagingOrderIdText(i));
            } catch(Exception | AssertionError e){
                baseCommands.wait(5);
                dashOrderId = baseCommands.getElementText(dashMap.readyForStagingOrderIdText(i));
            }
            if (dashOrderId.equals(visualOrderId)) {
                orderExist = true;
            }
        }
        Assert.assertTrue(orderExist);
    }
}
