package com.krogerqa.web.apps;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.utils.WebUtils;
import com.krogerqa.web.ui.maps.cue.CueHomeMap;
import com.krogerqa.web.ui.pages.login.FederationSignInPage;
import com.krogerqa.web.ui.pages.seamlessportal.SeamlessLoginPage;

public class LoginWeb {

    private static LoginWeb instance;
    BaseCommands baseCommands = new BaseCommands();
    CueHomeMap cueHomeMap = CueHomeMap.getInstance();
    FederationSignInPage federationSignInPage = FederationSignInPage.getInstance();
    SeamlessLoginPage seamlessLoginPage = SeamlessLoginPage.getInstance();
    WebUtils webUtils = WebUtils.getInstance();

    private LoginWeb() {
    }

    public synchronized static LoginWeb getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (LoginWeb.class) {
            if (instance == null) {
                instance = new LoginWeb();
            }
        }
        return instance;
    }

    public void loginKrogerSeamlessPortal(String userName, String password) {
        try {
            seamlessLoginPage.verifyLogin(userName, password);
        } catch(Exception | AssertionError e){
            seamlessLoginPage.verifyLogin(userName, password);
        }
    }

    public void loginCue(String storeId, String storeBanner) {
        try {
            if (!storeBanner.equalsIgnoreCase(ExcelUtils.BANNER_HARRIS_TEETER)) {
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(storeId));
                federationSignInPage.pingOneAuthentication(PropertyUtils.getCueUsername(), PropertyUtils.getCuePassword());
            } else {
                baseCommands.openNewUrl(PropertyUtils.getCueBaseUrl());
                federationSignInPage.pingOneAuthentication(PropertyUtils.getCueUsername(), PropertyUtils.getCuePassword());
                baseCommands.waitForElementVisibility(cueHomeMap.searchOrderInputField());
                baseCommands.openNewUrl(PropertyUtils.getCueUrl(storeId));
            }
        } catch (Exception | AssertionError e) {
            webUtils.captureScreenshot(Constants.ApplicationName.CUE, String.valueOf(e));
        }
    }

    public void federationLoginKrogerSeamlessPortal(String userName, String password) {
        federationSignInPage.federationLoginKsp(userName, password);
    }
}
