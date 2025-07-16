package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessPaymentMap {

    private static SeamlessPaymentMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessPaymentMap() {
    }

    public synchronized static SeamlessPaymentMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessPaymentMap.class) {
            if (instance == null) {
                instance = new SeamlessPaymentMap();
            }
        }
        return instance;
    }

    public By phoneNumberInput() {
        return bySizzle.css("input[name='Phone']");
    }

    public By existingPaymentSection() {
        return bySizzle.css("div[class='flex flex-col mt-8']");
    }

    public By creditCardDetails() {
        return bySizzle.css("[data-testid='hosted-payment-page-checkout']");
    }

    public By snapOrEbtRadioButton() {
        return bySizzle.css("input[value='EBT']");
    }

    public By snapEbtCheckBox() {
        return bySizzle.css("[aria-label*='SNAP EBT']+div input");
    }

    public By continueWithCheckoutButton() {
        return bySizzle.css(".kds-Modal-footer-buttons button");
    }

    public By snapEbtPinFrame() {
        return bySizzle.css("#eProtect-iframe");
    }

    public By snapEbtPinNumberText() {
        return bySizzle.css("[placeholder='SNAP EBT PIN']");
    }

    public By snapEbtDoneButton() {
        return bySizzle.css("[aria-label='Enter Pin']");
    }

    public By creditDebitTextToggle() {
        return bySizzle.css("[aria-label*='Credit & Debit']");
    }

    public By submitButton() {
        return bySizzle.css("button[type='submit']");
    }

    public By orderSummarySection() {
        return bySizzle.css(".OrderSummary-container");
    }

    public By snapEbtTotalText() {
        return bySizzle.css("[data-qa='ebt-total']");
    }

    public By nonSnapEbtTotalText() {
        return bySizzle.css("[data-qa='non-ebt-total']");
    }

    public By orderTotalText() {
        return bySizzle.css("[data-qa='checkout-total']");
    }

    public By addPhoneNumberButton() {
        return bySizzle.css("div.OnePageCheckout-max-content-width button");
    }

    public By updateContactButton() {
        return bySizzle.css("div.kds-Modal-footer-buttons button");
    }

    public By lastNameInput() {
        return bySizzle.css("input#lastName");
    }
}
