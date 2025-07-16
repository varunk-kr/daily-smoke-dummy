package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoCheckoutCompleteMap {
    private static CiaoCheckoutCompleteMap instance;

    private CiaoCheckoutCompleteMap() {
    }

    public synchronized static CiaoCheckoutCompleteMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoCheckoutCompleteMap.class) {
            if (instance == null) {
                instance = new CiaoCheckoutCompleteMap();
            }
        }
        return instance;
    }

    public By checkoutCompleteText() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Checkout complete!']");
    }

    public By amountProcessedText() {
        return AppiumBy.xpath("//*[contains(@text,'Amount processed:')]//following-sibling::android.widget.TextView[1]");
    }

    public By savingsAmountText() {
        return AppiumBy.xpath("//*[contains(@text,'Savings this order:')]//following-sibling::android.widget.TextView[1]");
    }

    public By customerNameText() {
        return AppiumBy.xpath("//*[contains(@text,'Remember to Thank')]//following-sibling::android.widget.TextView[1]");
    }

    public By doneButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Done']");
    }

    public By nextButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Next']");
    }

    public By pallyConfirmationButton() {
        return AppiumBy.xpath("//*[contains(@text,'Checkout')]");
    }

    public By selectSubstituteAcceptBtn() {
        return AppiumBy.xpath("//android.widget.RadioButton[@content-desc='EPosRadioButton Accept']");
    }

    public By selectMultipleSubstituteAcceptBtn(int i) {
        return AppiumBy.xpath("(//android.widget.RadioButton[@content-desc='EPosRadioButton Accept'])[" + i + "]");
    }

    public By selectSubstituteRejectBtn() {
        return AppiumBy.xpath("//android.widget.RadioButton[@content-desc='EPosRadioButton Reject']");
    }
}
