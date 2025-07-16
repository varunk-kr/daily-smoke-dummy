package com.krogerqa.mobile.ui.pages.login;

import com.krogerqa.mobile.ui.maps.login.WelcomeToChromeMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.MobileUtils;

public class WelcomeToChromePage {
    private static WelcomeToChromePage instance;
    MobileCommands mobileCommands = new MobileCommands();
    WelcomeToChromeMap welcomeToChromeMap = WelcomeToChromeMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();

    private WelcomeToChromePage() {
    }

    public synchronized static WelcomeToChromePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (WelcomeToChromePage.class) {
            if (instance == null) {
                instance = new WelcomeToChromePage();
            }
        }
        return instance;
    }

    public boolean tapAcceptTermsButton() {
        String chromeHomePageText = mobileCommands.getElementText(welcomeToChromeMap.acceptTermsButton());
        mobileCommands.tap(welcomeToChromeMap.acceptTermsButton());
        return chromeHomePageText.equalsIgnoreCase("Accept & continue");
    }

    public void tapNoThanksButton() {
        mobileCommands.tap(welcomeToChromeMap.noThanksButton());
    }

    public void acceptChromeTerms() {
        try {
            if (tapAcceptTermsButton()) {
                tapNoThanksButton();
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.LOGIN, String.valueOf(e));
        }
    }
}
