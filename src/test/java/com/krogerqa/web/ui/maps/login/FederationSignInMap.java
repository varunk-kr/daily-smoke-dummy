package com.krogerqa.web.ui.maps.login;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class FederationSignInMap {

    private static FederationSignInMap instance;
    BySizzle bySizzle = new BySizzle();

    private FederationSignInMap() {
    }

    public synchronized static FederationSignInMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (FederationSignInMap.class) {
            if (instance == null) {
                instance = new FederationSignInMap();
            }
        }
        return instance;
    }

    public By enterpriseUserIdInput() {
        return bySizzle.css("input[id='submittedIdentifier']");
    }

    public By passwordInput() {
        return bySizzle.css("input[id='password']");
    }

    public By continueAndSignInButton() {
        return bySizzle.css("button[id='btnSignIn']");
    }

    public By enterpriseUserIdInputKsp() {
        return bySizzle.css("input[id='signInName']");
    }

    public By passwordInputKsp() {
        return bySizzle.css("input[id='password']");
    }

    public By submitButton() {
        return bySizzle.css("button[type='submit']");
    }

}
