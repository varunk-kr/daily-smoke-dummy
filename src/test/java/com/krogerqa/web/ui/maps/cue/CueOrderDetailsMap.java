package com.krogerqa.web.ui.maps.cue;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class CueOrderDetailsMap {

    private static CueOrderDetailsMap instance;
    BySizzle bySizzle = new BySizzle();

    private CueOrderDetailsMap() {
    }

    public synchronized static CueOrderDetailsMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueOrderDetailsMap.class) {
            if (instance == null) {
                instance = new CueOrderDetailsMap();
            }
        }
        return instance;
    }

    public By containerRowList() {
        return bySizzle.css("table tr td a[href*='container'],table tr td a.font-bold");
    }

    public By getAllItemsOrderedText() {
        return bySizzle.css(".pt-24 h2");
    }

    public By trolleyRowList() {
        return bySizzle.css("table tr td a[href*='trolley']");
    }

    public By itemDetailsSection() {
        return bySizzle.css(".item-details.border-solid");
    }

    public By printPageButton() {
        return bySizzle.css("span[data-id='global.print']");
    }

    public By itemUpc(String itemNum) {
        return bySizzle.css(".flex.flex-wrap>div:nth-of-type(" + itemNum + ") span:nth-of-type(2)");
    }

    public By itemQty(String itemNum) {
        return bySizzle.css(".flex.flex-wrap>div:nth-of-type(" + itemNum + ") span:nth-of-type(3)");
    }

    public By containerDetails(String rowNum, String columnNum) {
        return bySizzle.css("tbody[class='table-body order-item-containers-table-body'] tr:nth-child(" + rowNum + ") td:nth-child(" + columnNum + ")");
    }

    public By containerIdDetails(String rowNum, String columnNum) {
        return bySizzle.css("tbody[class='table-body order-item-containers-table-body'] tr:nth-child(" + rowNum + ") td:nth-child(" + columnNum + ") a");
    }

    public By visualOrderIdText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(2)");
    }

    public By orderStatusText() {
        return bySizzle.css("div.inline-flex section:nth-child(1) span:nth-of-type(2)");
    }

    public By paymentStatusText() {
        return bySizzle.css("div.inline-flex section:nth-child(3) span:nth-of-type(2)");
    }

    public By orderDetailsSection() {
        return bySizzle.css(".inline-flex.pb-32");
    }

    public By containerColumnList() {
        return bySizzle.css("table thead tr[class*='item-containers'] th");
    }

    public By timeSlotLevelCheckBox() {
        return bySizzle.css("input.kds-Checkbox");
    }

    public By manualBatchingButton() {
        return bySizzle.css("span[aria-label='batchOrdersButton'] kds-button[disabled='false']");
    }

    public By confirmManualBatching() {
        return bySizzle.css("span[aria-label='continue-button'] >kds-button");
    }

    public By batchingMessage() {
        return bySizzle.css("div.ant-notification-notice");
    }

    public By trolleySection() {
        return bySizzle.css("div.top-nav-links a[href*='/trolleys']");
    }

    public By pickupTime() {
        return bySizzle.css("div.inline-flex section:nth-child(2) span:nth-of-type(1)");
    }

    public By openSpecificTrolley(String trolleyNo) {
        return bySizzle.css("table tr td a[href*='trolleys/" + trolleyNo + "']");
    }

    public By CheckRowForContainer() {
        return bySizzle.css("tbody.order-item-containers-table-body > tr");
    }

    public By checkForContainerOSUpc(int i) {
        return bySizzle.css("table tr:nth-of-type(" + i + ") td a[href*='container']");
    }

    public By instacartTag() {
        return bySizzle.css(".instacart");
    }

    public By instacartTagInOrderDashboard() {
        return bySizzle.css("div.instacart-message");
    }

    public By containerInstacartRowList() {
        return bySizzle.css("table tr td span");
    }

    public By ageRestrictedLabel() {
        return bySizzle.css("[aria-label='Tag: Age Restricted Item']");
    }
}
