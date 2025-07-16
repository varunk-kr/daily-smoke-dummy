package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.web.ui.maps.seamlessportal.SeamlessLoginMap;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessStoreChangeMap;

public class SeamlessLoginPage {

    BaseCommands baseCommands = new BaseCommands();
    SeamlessLoginMap seamlessLoginMap = SeamlessLoginMap.getInstance();
    private static SeamlessLoginPage instance;
    SeamlessStoreChangeMap seamlessStoreChangeMap = SeamlessStoreChangeMap.getInstance();

    private SeamlessLoginPage() {
    }

    public synchronized static SeamlessLoginPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessLoginPage.class) {
            if (instance == null) {
                instance = new SeamlessLoginPage();
            }
        }
        return instance;
    }

    public void enterUsername(String username) {
        if (baseCommands.elementDisplayed(seamlessLoginMap.loginEmailInput()))
            baseCommands.enterText(seamlessLoginMap.loginEmailInput(), username, true);
    }

    public void enterPassword(String password) {
        if (baseCommands.elementDisplayed(seamlessLoginMap.loginPasswordInput()))
            baseCommands.enterText(seamlessLoginMap.loginPasswordInput(), password, true);
    }

    public void clickLoginButton(String username, String password) {
        baseCommands.click(seamlessLoginMap.loginButton());
        try {
            baseCommands.waitForElementVisibility(seamlessStoreChangeMap.currentStoreNameText());
        } catch (Exception | AssertionError e) {
            verifyLogin(username, password);
        }
    }

    public void verifyLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton(username, password);
    }
}
