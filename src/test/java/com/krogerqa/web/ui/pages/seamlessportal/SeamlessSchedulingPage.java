package com.krogerqa.web.ui.pages.seamlessportal;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.web.ui.maps.seamlessportal.SeamlessSchedulingMap;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.HashMap;

public class SeamlessSchedulingPage {
    static String PICKUP_SLOT_UNAVAILABLE = "No Times available.";
    private static SeamlessSchedulingPage instance;
    BaseCommands baseCommands = new BaseCommands();
    SeamlessSchedulingMap seamlessSchedulingMap = SeamlessSchedulingMap.getInstance();

    private SeamlessSchedulingPage() {
    }

    public synchronized static SeamlessSchedulingPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SeamlessSchedulingPage.class) {
            if (instance == null) {
                instance = new SeamlessSchedulingPage();
            }
        }
        return instance;
    }

    public void selectPickupTime() {
        baseCommands.waitForElementVisibility(seamlessSchedulingMap.fieldset());
        WebElement element = baseCommands.element(seamlessSchedulingMap.fieldset());
        WebElement todayKspDate = element.findElement(seamlessSchedulingMap.currentPickUpDate());
        String dateFromKSP = todayKspDate.getDomAttribute("value");
        String text = baseCommands.getElementText(seamlessSchedulingMap.getDateText(dateFromKSP));
        if (text.contains("Unavailable")) {
            Assert.fail(PICKUP_SLOT_UNAVAILABLE);
        }
        selectRequiredPickupSlot();
    }

    public void completeAgeCheck(String month, String date, String year) {
        baseCommands.enterText(seamlessSchedulingMap.dateInput(), month + date + year, true);
        baseCommands.click(seamlessSchedulingMap.termsConditionsCheckbox());
        baseCommands.click(seamlessSchedulingMap.ageVerificationContinueButton());
    }

    public void clickContinueButton() {
        baseCommands.click(seamlessSchedulingMap.continueButton());
    }


    public void selectPickupSlotEnterBirthDate(String month, String day, String year, boolean ageCheck, HashMap<String, String> testOutput) {
        if (baseCommands.elementDisplayed(seamlessSchedulingMap.continueToCheckoutButton())) {
            baseCommands.waitForElementVisibility(seamlessSchedulingMap.continueToCheckoutButton());
            baseCommands.click(seamlessSchedulingMap.continueToCheckoutButton());
        }
        if (Boolean.parseBoolean(testOutput.get(ExcelUtils.IS_EXPRESS_ORDER))) {
            selectExpressOrderPickupSlot();
            validationOfTimeSlotInformation(testOutput);
        } else {
            if (testOutput.containsKey(ExcelUtils.ORDER_SLOT)) {
                selectPickupTimeSlot(testOutput.get(ExcelUtils.ORDER_SLOT));
            } else {
                selectPickupTime();
            }
        }
        clickContinueButton();
        if (ageCheck) {
            completeAgeCheck(month, day, year);
            clickContinueButton();
        }
    }

    private void validationOfTimeSlotInformation(HashMap<String, String> testOutput) {
        String labelValue = baseCommands.getElementText(seamlessSchedulingMap.verifyExpressLabel());
        Assert.assertEquals(labelValue, Constants.EXPRESS_ORDER);
        String feesValue = baseCommands.getElementText(seamlessSchedulingMap.verifyExpressFees());
        Assert.assertEquals(feesValue, "$" + (testOutput.get(ExcelUtils.EXPRESS_FEE)));
    }

    private void selectExpressOrderPickupSlot() {
        baseCommands.waitForElementVisibility(seamlessSchedulingMap.deliverySlots());
        for (int i = 1; i <= baseCommands.numberOfElements(seamlessSchedulingMap.deliverySlots()); i++) {
            baseCommands.waitForElementVisibility(seamlessSchedulingMap.selectDeliverySlot(i));
            if (baseCommands.getElementText(seamlessSchedulingMap.deliverySlotsText(i)).contains(Constants.EXPRESS_ORDER)) {
                baseCommands.click(seamlessSchedulingMap.selectDeliverySlot(i));
                break;
            }
        }
    }

    private void selectRequiredPickupSlot() {
        baseCommands.waitForElementVisibility(seamlessSchedulingMap.deliverySlots());
        for (int i = 1; i <= baseCommands.numberOfElements(seamlessSchedulingMap.deliverySlots()); i++) {
            baseCommands.waitForElementVisibility(seamlessSchedulingMap.selectDeliverySlot(i));
            if (!baseCommands.getElementText(seamlessSchedulingMap.deliverySlotsText(i)).contains(Constants.TIMESLOT_AVAILABLE)) {
                baseCommands.click(seamlessSchedulingMap.selectDeliverySlot(i));
                break;
            }
        }
    }

    public void selectDeliveryTimeRadioButton(String deliveryOption) {
        baseCommands.waitForElementVisibility(seamlessSchedulingMap.deliverySlots());
        for (int i = 1; i <= baseCommands.numberOfElements(seamlessSchedulingMap.deliverySlots()); i++) {
            baseCommands.waitForElementVisibility(seamlessSchedulingMap.selectDeliverySlot(i));
            if (baseCommands.getElementText(seamlessSchedulingMap.deliverySlotsText(i)).contains(deliveryOption)) {
                baseCommands.click(seamlessSchedulingMap.selectDeliverySlot(i));
                break;
            }
        }
    }

    public void selectDeliveryTime(String deliveryOption, String month, String day, String year, boolean ageCheck) {
        if (baseCommands.elementDisplayed(seamlessSchedulingMap.continueToCheckoutButton())) {
            baseCommands.waitForElementVisibility(seamlessSchedulingMap.continueToCheckoutButton());
            baseCommands.click(seamlessSchedulingMap.continueToCheckoutButton());
        }
        try {
            selectDeliveryTimeRadioButton(deliveryOption);
        } catch (Exception | AssertionError e) {
            baseCommands.webpageRefresh();
            selectDeliveryTimeRadioButton(deliveryOption);
        }
        if (ageCheck) {
            clickContinueButton();
            completeAgeCheck(month, day, year);
        } else {
            baseCommands.click(seamlessSchedulingMap.DeliveryContinueButton());
        }
    }

    public void selectPickupTimeSlot(String pickupSlot) {
        switch (pickupSlot) {
            case ExcelUtils.FIRST_ORDER:
                baseCommands.waitForElementVisibility(seamlessSchedulingMap.firstPickupTimeRadioButton());
                baseCommands.click(seamlessSchedulingMap.firstPickupTimeRadioButton());
                break;
            case ExcelUtils.SECOND_ORDER:
                baseCommands.waitForElementVisibility(seamlessSchedulingMap.secondPickupTimeRadioButton());
                baseCommands.click(seamlessSchedulingMap.secondPickupTimeRadioButton());
                break;
            case ExcelUtils.THIRD_ORDER:
                baseCommands.waitForElementVisibility(seamlessSchedulingMap.thirdPickupTimeRadioButton());
                baseCommands.click(seamlessSchedulingMap.thirdPickupTimeRadioButton());
                break;
            default:
                baseCommands.waitForElementVisibility(seamlessSchedulingMap.fourthPickupTimeRadioButton());
                baseCommands.click(seamlessSchedulingMap.fourthPickupTimeRadioButton());
                break;
        }
    }
}
