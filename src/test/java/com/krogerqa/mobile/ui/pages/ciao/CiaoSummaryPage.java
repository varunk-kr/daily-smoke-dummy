package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoSummaryMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;

public class CiaoSummaryPage {
    private  static CiaoSummaryPage instance;
    private CiaoSummaryPage(){}
    public synchronized static CiaoSummaryPage getInstance() {
        if (instance == null) {
            synchronized (CiaoSummaryPage.class)
            {
                if (instance == null) {
                    instance = new CiaoSummaryPage();
                }
            }
        }
        return instance;
    }

    MobileCommands mobileCommands = new MobileCommands();
    CiaoSummaryMap ciaoSummaryMap =  CiaoSummaryMap.getInstance();

    public void tapCompleteButton() {
        if(mobileCommands.elementDisplayed(ciaoSummaryMap.completeButton())) {
            mobileCommands.tap(ciaoSummaryMap.completeButton());
        }
    }
}
