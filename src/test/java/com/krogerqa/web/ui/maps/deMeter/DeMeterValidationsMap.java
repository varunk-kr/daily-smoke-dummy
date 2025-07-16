package com.krogerqa.web.ui.maps.deMeter;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class DeMeterValidationsMap {
    private static DeMeterValidationsMap instance;

    BySizzle bySizzle = new BySizzle();

    public synchronized static DeMeterValidationsMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DeMeterValidationsMap.class) {
            if (instance == null) {
                instance = new DeMeterValidationsMap();
            }
        }
        return instance;
    }

    public By hamburgerIcon() {
        return bySizzle.css("button[aria-label='Open drawer']");
    }

    public By changeSetupIcon() {
        return bySizzle.css("div.MuiGrid-root.MuiGrid-item h6 span");
    }

    public By loginUserNameInput() {
        return bySizzle.css("input#username");
    }

    public By loginPasswordInput() {
        return bySizzle.css("input#password");
    }

    public By loginButton() {
        return bySizzle.css("button#signin_button");
    }

    public By enterStoreLocationInput() {
        return bySizzle.css("input[data-testid='input-dialog-textfield']");
    }

    public By clickFLexRadioButton() {
        return bySizzle.css("span.MuiIconButton-label > div");
    }

    public By clickSubmitButtonFlex() {
        return bySizzle.css("button[data-testid='input-dialog-accept']>span");
    }
}
