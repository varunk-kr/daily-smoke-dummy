package com.krogerqa.web.ui.maps.login;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class OktaSignInMap {

    private static OktaSignInMap instance;
    BySizzle bySizzle = new BySizzle();

    private OktaSignInMap() {
    }

    public synchronized static OktaSignInMap getInstance() {
        if (instance == null) {
            synchronized (OktaSignInMap.class) {
                if (instance == null) {
                    instance = new OktaSignInMap();
                }
            }
        }
        return instance;
    }

    public By enterpriseUserIdInput() {
        return bySizzle.css("#okta-signin-username");
    }

    public By passwordInput() {
        return bySizzle.css("#okta-signin-password");
    }

    public By signInButton() {
        return bySizzle.css("#okta-signin-submit");
    }
}
