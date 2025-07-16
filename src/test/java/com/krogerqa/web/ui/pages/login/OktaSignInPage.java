package com.krogerqa.web.ui.pages.login;

import com.krogerqa.web.ui.maps.login.OktaSignInMap;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;

public class OktaSignInPage {

    private static OktaSignInPage instance;
    OktaSignInMap oktaSignInMap = OktaSignInMap.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    private OktaSignInPage() {
    }

    public synchronized static OktaSignInPage getInstance() {
        if (instance == null) {
            synchronized (OktaSignInPage.class) {
                if (instance == null) {
                    instance = new OktaSignInPage();
                }
            }
        }
        return instance;
    }

    public void enterUsername(String username) {
        baseCommands.enterText(oktaSignInMap.enterpriseUserIdInput(), username, true);
    }

    public void enterPassword(String password) {
        baseCommands.enterText(oktaSignInMap.passwordInput(), password, true);
    }

    public void oktaLogin(String userName, String password) {
        enterUsername(userName);
        enterPassword(password);
        baseCommands.click(oktaSignInMap.signInButton());
        baseCommands.waitForElementInvisible(oktaSignInMap.signInButton());
    }
}
