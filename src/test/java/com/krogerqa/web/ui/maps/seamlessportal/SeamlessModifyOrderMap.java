package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessModifyOrderMap {
    private static SeamlessModifyOrderMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessModifyOrderMap() {
    }
    public synchronized static SeamlessModifyOrderMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessModifyOrderMap.class) {
                if (instance == null) {
                    instance = new SeamlessModifyOrderMap();
                }
            }
        }
        return instance;
    }

    public By finishChangesButton() {
        return bySizzle.css("button[data-testid='ModifyModeHeader-submit-button']");
    }
}
