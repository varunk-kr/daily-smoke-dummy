package com.krogerqa.web.ui.maps.dash;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class DashMap {

    private static DashMap instance;

    private DashMap() {
    }

    public synchronized static DashMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DashMap.class) {
            if (instance == null) {
                instance = new DashMap();
            }
        }
        return instance;
    }


    BySizzle bySizzle = new BySizzle();

    public By divisionStoreText() {
        return bySizzle.css(".header-location");
    }

    public By rowList() {
        return bySizzle.css("div[data-testid='checkInTableBody'] div[data-testid='tableRow']");
    }

    public By todayOrderSection() {
        return bySizzle.css("div[data-testid='orders_today']");
    }

    public By todayOrder() {
        return bySizzle.css("div[data-testid='orders_today'] div.data");
    }

    public By loaderIcon() {
        return bySizzle.css("div.ant-spin");
    }

    public By noIncomingCustomers() {
        return bySizzle.css("div.noIncomingCustomers");
    }

    public By totalInstacartOrders() {
        return bySizzle.css("div[data-testid='readyForStagingTableBody'] div.tableRow");
    }

    public By readyForStagingOrderIdText(int i) {
        return bySizzle.css("div[data-testid='readyForStagingTableBody'] div.tableRow:nth-of-type(" + i + ") div.orderIdCell");
    }
}
