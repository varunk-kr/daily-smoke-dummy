package com.krogerqa.mobile.ui.pages.login;

import com.krogerqa.mobile.ui.maps.login.LoginNativeMap;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginNativePage extends BaseTest {

    private static LoginNativePage instance;
    LoginNativeMap loginNativeMap = LoginNativeMap.getInstance();

    private LoginNativePage() {
    }

    public synchronized static LoginNativePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (LoginNativePage.class) {
            if (instance == null) {
                instance = new LoginNativePage();
            }
        }
        return instance;
    }

    MobileCommands mobileCommands = new MobileCommands();
    LoginNativeMap oktaLoginMap = LoginNativeMap.getInstance();

    public void enterUsername(String username) {
        mobileCommands.enterText(oktaLoginMap.loginUsernameInput(), username, true);
    }

    public void enterPassword(String password) {
        mobileCommands.enterText(oktaLoginMap.loginPasswordInput(), password, true);
    }

    public void tapSignInButton() {
        mobileCommands.tap(oktaLoginMap.signInButton());
    }

    public void loginNativeApp(String username, String password) {
        AndroidDriver androidDriver = (AndroidDriver) getDriver();
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(20));
        int retry = 1000;
        while (retry > 0) {
            try {
                androidDriver.context(Constants.LoginContexts.WEB_CONTEXT);
                break;
            } catch (Exception | AssertionError e) {
                retry--;
            }
        }
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(loginNativeMap.loginUsernameInput()));
        usernameInput.sendKeys(username);
        androidDriver.findElement(loginNativeMap.signInButton()).click();
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(loginNativeMap.loginPasswordInput()));
        passwordInput.sendKeys(password);
        androidDriver.findElement(loginNativeMap.signInButton()).click();
        androidDriver.context(Constants.LoginContexts.NATIVE_CONTEXT);
    }

    public void loginForTc52(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        tapSignInButton();
    }
}
