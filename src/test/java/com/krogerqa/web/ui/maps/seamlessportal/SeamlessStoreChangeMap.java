package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessStoreChangeMap {
    private static SeamlessStoreChangeMap instance;
    BySizzle bysizzle = new BySizzle();

    private SeamlessStoreChangeMap() {
    }

    public synchronized static SeamlessStoreChangeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessStoreChangeMap.class) {
            if (instance == null) {
                instance = new SeamlessStoreChangeMap();
            }
        }
        return instance;
    }

    public By currentStoreNameText() {
        return bysizzle.css("span[data-testid='CurrentModality-vanityName'], div.POE-BO-content");
    }

    public By currentModalityButton() {
        return bysizzle.css(".CurrentModality-button");
    }

    public By postalCodeDropdown() {
        return bysizzle.css(".PostalCodeSearchBox--drawer");
    }

    public By searchPostalCodeInput() {
        return bysizzle.css("[data-testid='PostalCodeSearchBox-input']");
    }

    public By changeStoreButton() {
        return bysizzle.css("button[data-testid='ModalityOption-Button-PICKUP']");
    }

    public By resendConfirmation() {
        return bysizzle.css("[aria-label='Resend confirmation email']");
    }

    public By startShoppingButton(String div, String store) {
        return bysizzle.css("button[data-testid=SelectStore-" + div + "00" + store + "]");
    }

    public By storeChangeSuccessMessage() {
        return bysizzle.css("div#kds-Portal-toast>div>section");
    }

    public By selectDeliveryShopping() {
        return bysizzle.css("button[data-testid='ModalityOption-Button-DELIVERY']");
    }

    public By changeStoreBanner() {
        return bysizzle.css("button[data-testid='Leave-Site-Cross-Banner-Modal-Button']");
    }

    public By changeStoreLink(String div, String store) {
        return bysizzle.css("button[data-testid=SelectStore-" + div + "00" + store + "]");
    }
}
