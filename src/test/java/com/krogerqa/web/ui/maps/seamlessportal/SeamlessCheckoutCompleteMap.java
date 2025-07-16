package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessCheckoutCompleteMap {
    private static SeamlessCheckoutCompleteMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessCheckoutCompleteMap() {
    }

    public synchronized static SeamlessCheckoutCompleteMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessCheckoutCompleteMap.class) {
            if (instance == null) {
                instance = new SeamlessCheckoutCompleteMap();
            }
        }
        return instance;
    }

    public By orderPlacedMessage() {
        return bySizzle.css("h1[data-qa='order-placed-message']");
    }

    public By orderNumberText() {
        return bySizzle.css("a[usagecontext='order number']");
    }

    public By purchaseDetails() {
        return bySizzle.css("div[aria-label='Purchase History Order']");
    }

    public By byobPopUp() {
        return bySizzle.css("div.POE-BO-content");
    }

    public By acceptBagFeeButton() {
        return bySizzle.css("label input[name='isBringingBags'][value='false']");
    }

    public By changePrefButton() {
        return bySizzle.css("a[aria-label='Change Preference'] button");
    }

    public By bringYourOwnBagButton() {
        return bySizzle.css("label input[name='isBringingBags'][value='true']");
    }

    public By continueButton() {
        return bySizzle.css("button.kds-Button");
    }

    public By confirmBagFeePopup() {
        return bySizzle.css("div.POE-BO-container");
    }

    public By confirmBagCharges() {
        return bySizzle.css("button.kds-Button.variant-border");
    }

    public By headerKCP() {
        return bySizzle.css("button[id='WelcomeButton-A11Y-FOCUS-ID']");
    }

    public By signOutButtonKCP() {
        return bySizzle.css("button[data-testid='WelcomeMenuButtonSignOut']");
    }

    public By singInAgainButtonKCP() {
        return bySizzle.css("button[data-testid='WelcomeMenuButtonSignIn']");
    }
}
