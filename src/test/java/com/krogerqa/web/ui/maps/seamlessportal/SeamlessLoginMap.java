package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessLoginMap {

    private static SeamlessLoginMap instance;
    private SeamlessLoginMap() {
    }

    public synchronized static SeamlessLoginMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessLoginMap.class) {
                if (instance == null) {
                    instance = new SeamlessLoginMap();
                }
            }
        }
        return instance;
    }

    BySizzle bySizzle = new BySizzle();

    public By loginEmailInput() {
        return bySizzle.css("#signInName");
    }

    public By loginPasswordInput() {
        return bySizzle.css("#password");
    }

    public By loginButton() {
        return bySizzle.css("button[data-content='Sign In'],button[data-content='Sign in']");
    }
}
