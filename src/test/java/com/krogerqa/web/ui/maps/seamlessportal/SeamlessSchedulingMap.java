package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessSchedulingMap {

    private static SeamlessSchedulingMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessSchedulingMap() {
    }

    public synchronized static SeamlessSchedulingMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessSchedulingMap.class) {
            if (instance == null) {
                instance = new SeamlessSchedulingMap();
            }
        }
        return instance;
    }

    public By fieldset() {
        return By.cssSelector("fieldset.Fieldset-container div.day-carousel");
    }

    public By currentPickUpDate() {
        return By.cssSelector("input[name='days'][aria-label*='Today']");
    }

    public By getDateText(String date) {
        return By.cssSelector("label[id='" + date + "'] div");
    }

    public By continueButton() {
        return bySizzle.css("button[type='submit']");
    }

    public By dateInput() {
        return bySizzle.css(".kds-FormDate input");
    }

    public By termsConditionsCheckbox() {
        return bySizzle.css(".kds-Checkbox");
    }

    public By ageVerificationContinueButton() {
        return bySizzle.css(".kds-Modal-actionButton--primary");
    }

    public By DeliveryContinueButton() {
        return bySizzle.css("button[data-qa='scheduling-page/quote-selection-form']");
    }

    public By deliverySlots() {
        return bySizzle.css("div[data-qa='select-quote-option']");
    }

    public By deliverySlotsText(int i) {
        return bySizzle.css("div[data-qa='select-quote-option']:nth-child(" + i + ")");
    }

    public By selectDeliverySlot(int i) {
        return bySizzle.css("div.QuotesContainer div:nth-of-type(" + i + ")>div>label");
    }

    public By firstPickupTimeRadioButton() {
        return bySizzle.css("div[data-qa='select-quote-option']:first-child div[class='flex flex-row justify-between']");
    }

    public By secondPickupTimeRadioButton() {
        return bySizzle.css("div[data-qa='select-quote-option']:nth-child(2) div[class='flex flex-row justify-between']");
    }

    public By thirdPickupTimeRadioButton() {
        return bySizzle.css("div[data-qa='select-quote-option']:nth-child(3) div[class='flex flex-row justify-between']");
    }

    public By fourthPickupTimeRadioButton() {
        return bySizzle.css("div.QuotesContainer div:nth-of-type(4)>div>label");
    }

    public By continueToCheckoutButton() {
        return bySizzle.css(".CheckoutSimpleLayout-page-content h1+button");
    }

    public By verifyExpressLabel() {
        return bySizzle.css(".kds-Tag-text");
    }

    public By verifyExpressFees() {
        return bySizzle.css("div[data-qa='select-quote-option']:nth-of-type(1) div.inline-block");
    }
}
