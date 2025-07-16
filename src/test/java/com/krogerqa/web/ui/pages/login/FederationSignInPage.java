package com.krogerqa.web.ui.pages.login;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.web.ui.maps.login.FederationSignInMap;

public class FederationSignInPage {

    private static FederationSignInPage instance;
    FederationSignInMap federationSignInMap = FederationSignInMap.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    private FederationSignInPage() {
    }

    public synchronized static FederationSignInPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (FederationSignInPage.class) {
            if (instance == null) {
                instance = new FederationSignInPage();
            }
        }
        return instance;
    }

    public void enterUsername(String username) {
        baseCommands.enterText(federationSignInMap.enterpriseUserIdInput(), username, true);
    }

    public void enterPassword(String password) {
        baseCommands.waitForElementVisibility(federationSignInMap.passwordInput());
        baseCommands.enterText(federationSignInMap.passwordInput(), password, true);
    }

    public void pingOneAuthentication(String userName, String password) {
        enterUsername(userName);
        baseCommands.click(federationSignInMap.continueAndSignInButton());
        enterPassword(password);
        baseCommands.click(federationSignInMap.continueAndSignInButton());
    }

    public void federationLoginKsp(String userName, String password) {
        enterUsernameKsp(userName);
        enterPasswordKsp(password);
        baseCommands.click(federationSignInMap.submitButton());
    }

    public void enterUsernameKsp(String username) {
        baseCommands.enterText(federationSignInMap.enterpriseUserIdInputKsp(), username, true);
    }

    public void enterPasswordKsp(String password) {
        baseCommands.enterText(federationSignInMap.passwordInputKsp(), password, true);
    }

}
