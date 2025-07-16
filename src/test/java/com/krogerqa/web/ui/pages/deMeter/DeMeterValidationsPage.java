package com.krogerqa.web.ui.pages.deMeter;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.ui.maps.deMeter.DeMeterValidationsMap;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;

public class DeMeterValidationsPage {
    public static String flexWindowHandle;
    public static String deMeterWindowHandle = "";
    private static DeMeterValidationsPage instance;
    DeMeterValidationsMap deMeterValidationsMap = DeMeterValidationsMap.getInstance();
    BaseCommands baseCommands = new BaseCommands();

    private DeMeterValidationsPage() {
    }

    public synchronized static DeMeterValidationsPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DeMeterValidationsPage.class) {
            if (instance == null) {
                instance = new DeMeterValidationsPage();
            }
        }
        return instance;
    }

    public void performLoginOperation(String username, String password) {
        baseCommands.openNewUrl(PropertyUtils.getDeMeterUrl());
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void enterUsername(String username) {
        if (baseCommands.elementDisplayed(deMeterValidationsMap.loginUserNameInput()))
            baseCommands.enterText(deMeterValidationsMap.loginUserNameInput(), username, true);
    }

    public void enterPassword(String password) {
        if (baseCommands.elementDisplayed(deMeterValidationsMap.loginPasswordInput()))
            baseCommands.enterText(deMeterValidationsMap.loginPasswordInput(), password, true);
    }

    public void clickLoginButton() {
        baseCommands.click(deMeterValidationsMap.loginButton());
    }

    public void loginDeMeterAndsetUpStore(String storeId) {
        baseCommands.getWebDriver().switchTo().window(deMeterWindowHandle);
        performLoginOperation(PropertyUtils.getDemeterUsername(), PropertyUtils.getDemeterPassword());
        try {
            enterStoreLocation(storeId);
        } catch (Exception e) {
            baseCommands.waitForElementVisibility(deMeterValidationsMap.hamburgerIcon());
            baseCommands.click(deMeterValidationsMap.hamburgerIcon());
            baseCommands.waitForElementVisibility(deMeterValidationsMap.changeSetupIcon());
            baseCommands.click(deMeterValidationsMap.changeSetupIcon());
            baseCommands.click(deMeterValidationsMap.changeSetupIcon());
            enterStoreLocation(storeId);
        }
    }


    public void enterStoreLocation(String storeId) {
        baseCommands.waitForElementVisibility(deMeterValidationsMap.enterStoreLocationInput());
        baseCommands.enterText(deMeterValidationsMap.enterStoreLocationInput(), storeId, true);
        baseCommands.waitForElementClickability(deMeterValidationsMap.clickFLexRadioButton());
        try {
            baseCommands.click(deMeterValidationsMap.clickFLexRadioButton());
            baseCommands.click(deMeterValidationsMap.clickSubmitButtonFlex());
        } catch (ElementClickInterceptedException radioAlreadySelected) {
            baseCommands.click(deMeterValidationsMap.clickSubmitButtonFlex());
        }
    }

    public void openDeMeterInNewTabAndPerformLogin() {
        flexWindowHandle = baseCommands.getWebDriver().getWindowHandle();
        ((JavascriptExecutor) baseCommands.getWebDriver()).executeScript("window.open()");
        for (String windowHandle : baseCommands.getWebDriver().getWindowHandles()) {
            if (!flexWindowHandle.contentEquals(windowHandle)) {
                deMeterWindowHandle = windowHandle;
            }
        }
    }
}
