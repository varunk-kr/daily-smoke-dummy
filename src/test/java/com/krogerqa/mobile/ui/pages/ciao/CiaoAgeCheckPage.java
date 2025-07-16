package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoAgeCheckMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;

public class CiaoAgeCheckPage {
    private static CiaoAgeCheckPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoAgeCheckMap ciaoAgeCheckMap = CiaoAgeCheckMap.getInstance();

    private CiaoAgeCheckPage() {
    }

    public synchronized static CiaoAgeCheckPage getInstance() {
        if (instance == null) {
            synchronized (CiaoAgeCheckPage.class) {
                if (instance == null) {
                    instance = new CiaoAgeCheckPage();
                }
            }
        }
        return instance;
    }

    public void assertAgeRestrictedIcon() {
        mobileCommands.assertElementExists(ciaoAgeCheckMap.ageRestrictionIcon(), true);
    }

    public void enterDateOfBirth(String month, String day, String year) {
        mobileCommands.tap(ciaoAgeCheckMap.hamburgerMenuIcon());
        mobileCommands.tap(ciaoAgeCheckMap.verifyIdManually());
        mobileCommands.enterText(ciaoAgeCheckMap.dateOfBirthInput(), month + day + year, true);
    }

    public void completeAgeCheck(String month, String day, String year) {
        assertAgeRestrictedIcon();
        enterDateOfBirth(month, day, year);
        mobileCommands.tap(ciaoAgeCheckMap.verifyIdButton());
    }

    public Boolean ageRestrictedLabelPresent() {
        return mobileCommands.elementDisplayed(ciaoAgeCheckMap.ageRestrictionIcon());
    }
}
