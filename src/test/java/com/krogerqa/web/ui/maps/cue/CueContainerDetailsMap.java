package com.krogerqa.web.ui.maps.cue;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class CueContainerDetailsMap {

    private static CueContainerDetailsMap instance;
    BySizzle bySizzle = new BySizzle();

    private CueContainerDetailsMap() {
    }

    public synchronized static CueContainerDetailsMap getInstance() {
        if (instance == null) {
            synchronized (CueContainerDetailsMap.class) {
                if (instance == null) {
                    instance = new CueContainerDetailsMap();
                }
            }
        }
        return instance;
    }

    public By itemRows() {
        return bySizzle.css(".containerDetails-table tbody tr");
    }

    public By itemDetails(String rowNum, String colNum) {
        return bySizzle.css(".containerDetails-table tbody tr:nth-of-type(" + rowNum + ") td:nth-of-type(" + colNum + ")");
    }

    public By printContainerlabel(){
        return bySizzle.css("kds-button[aria-label='print-label'] button");
    }

    public By printPageButton(){
        return bySizzle.css("kds-button[aria-label='print'] button");
    }
}
