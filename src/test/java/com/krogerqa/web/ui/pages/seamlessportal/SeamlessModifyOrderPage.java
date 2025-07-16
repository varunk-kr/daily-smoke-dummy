package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.web.ui.maps.seamlessportal.SeamlessModifyOrderMap;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;

public class SeamlessModifyOrderPage {

    private static SeamlessModifyOrderPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessModifyOrderMap seamlessModifyOrderMap = SeamlessModifyOrderMap.getInstance();

    private SeamlessModifyOrderPage() {
    }

    public synchronized static SeamlessModifyOrderPage getInstance() {
        if (instance == null) {
            synchronized (SeamlessModifyOrderPage.class) {
                if (instance == null) {
                    instance = new SeamlessModifyOrderPage();
                }
            }
        }
        return instance;
    }

    public void finishChanges() {
        baseCommands.click(seamlessModifyOrderMap.finishChangesButton());
    }
}
