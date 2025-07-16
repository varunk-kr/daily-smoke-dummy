package com.krogerqa.web.ui.maps.cue;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class CueTrolleyDetailsMap {

    private static CueTrolleyDetailsMap instance;
    BySizzle bySizzle = new BySizzle();

    private CueTrolleyDetailsMap() {
    }

    public synchronized static CueTrolleyDetailsMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueTrolleyDetailsMap.class) {
            if (instance == null) {
                instance = new CueTrolleyDetailsMap();
            }
        }
        return instance;
    }

    public By containerRowList() {
        return bySizzle.css("table tr td a[href*='container']");
    }

    public By searchBox() {
        return bySizzle.css("input[aria-label='trolleys']");
    }

    public By getAssociateText() {
        return bySizzle.css("tr[aria-label='table-body-row'] td:nth-of-type(9)");
    }

    public By trolleyNumber(String trolley) {
        return bySizzle.css("a[data-id='" + trolley + "']");
    }

    public By trolleyDetails() {
        return bySizzle.css(".italic");
    }

    public By numberOfContainers() {
        return bySizzle.css("tbody .trolleyDetails-table-row");
    }

    public By getRouteStep(int container) {
        return bySizzle.css("tbody .trolleyDetails-table-row:nth-of-type(" + container + ") td:nth-of-type(3)");
    }
}
