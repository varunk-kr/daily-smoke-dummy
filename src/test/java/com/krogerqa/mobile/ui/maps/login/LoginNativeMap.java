package com.krogerqa.mobile.ui.maps.login;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class LoginNativeMap {

    private static LoginNativeMap instance;

    private LoginNativeMap() {
    }

    public synchronized static LoginNativeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (LoginNativeMap.class) {
            if (instance == null) {
                instance = new LoginNativeMap();
            }
        }
        return instance;
    }

    public By loginUsernameInput() {
        return AppiumBy.xpath("//input[@data-id='submittedIdentifier-input']");
    }

    public By loginPasswordInput() {
        return AppiumBy.xpath("//input[@data-id='password-input']");
    }

    public By signInButton() {
        return AppiumBy.xpath("//button[@id='btnSignIn']");
    }
}
