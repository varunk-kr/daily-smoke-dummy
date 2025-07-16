package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessAddAddressMap {
    private static SeamlessAddAddressMap instance;
    BySizzle bySizzle = new BySizzle();

    public synchronized static SeamlessAddAddressMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessAddAddressMap.class) {
                if (instance == null) {
                    instance = new SeamlessAddAddressMap();
                }
            }
        }
        return instance;
    }

    public By addressInput() {
        return bySizzle.css("input[name='Address']");
    }

    public By cityInput() {
        return bySizzle.css("input[name='City']");
    }

    public By stateDropList() {
        return bySizzle.css("select[name='State']");
    }

    public By postalZipCodeInput() {
        return bySizzle.css("input[name='Zip Code']");
    }

    public By saveAndContinue() {
        return bySizzle.css("button[data-qa='address-book-modal']");
    }

    public By confirmAddressPopup() {
        return bySizzle.css("button[data-qa='confirmSuggestedAddress']");
    }
}
