package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoCheckoutCompleteMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.ExcelUtils;
import org.testng.Assert;

import java.util.HashMap;

public class CiaoCheckoutCompletePage {

    static String KROGER_SAVINGS_AMOUNT_IS_INCORRECT = "Kroger Savings amount is incorrect";
    private static CiaoCheckoutCompletePage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoCheckoutCompleteMap ciaoCheckoutCompleteMap = CiaoCheckoutCompleteMap.getInstance();

    private CiaoCheckoutCompletePage() {
    }

    public synchronized static CiaoCheckoutCompletePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoCheckoutCompletePage.class) {
            if (instance == null) {
                instance = new CiaoCheckoutCompletePage();
            }
        }
        return instance;
    }

    public void verifyCheckoutCompleteText() {
        mobileCommands.elementExists(ciaoCheckoutCompleteMap.checkoutCompleteText());
    }

    public void verifySavingsAmountText(String couponTotal, String krogerSavings, String ciaoCoupon) {
        double totalSavings = Double.parseDouble(couponTotal) + Double.parseDouble(krogerSavings) + Double.parseDouble(ciaoCoupon);
        double ciaoSavingsText = Double.parseDouble(mobileCommands.getElementText(ciaoCheckoutCompleteMap.savingsAmountText()).substring(1));
        Assert.assertEquals(ciaoSavingsText, totalSavings, KROGER_SAVINGS_AMOUNT_IS_INCORRECT);
    }

    public void verifyCustomerNameText(String name) {
        mobileCommands.assertElementText(ciaoCheckoutCompleteMap.customerNameText(), name, true);
    }

    public void verifyReducedOrderTotal(String originalAmount) {
        double totalAmountSubsOutOfStockCoupon = Double.parseDouble(mobileCommands.getElementText(ciaoCheckoutCompleteMap.amountProcessedText()).substring(1));
        Assert.assertTrue(totalAmountSubsOutOfStockCoupon <= Double.parseDouble(originalAmount.substring(1)));
    }

    public void tapNextButton() {
        if (mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.nextButton())) {
            mobileCommands.tap(ciaoCheckoutCompleteMap.nextButton());
        }
    }

    public void pallyConfirmationCheckout() {
        if (mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.pallyConfirmationButton())) {
            mobileCommands.waitForElementVisibility(ciaoCheckoutCompleteMap.pallyConfirmationButton());
            mobileCommands.tap(ciaoCheckoutCompleteMap.pallyConfirmationButton());
        }
    }

    public void tapDoneButton() {
        mobileCommands.waitForElementVisibility(ciaoCheckoutCompleteMap.doneButton());
        mobileCommands.tap(ciaoCheckoutCompleteMap.doneButton());
    }

    public void completeCheckout(HashMap<String, String> testOutputData, String couponTotal, String krogerSavings, String ciaoCoupon, String totalAmount, String name, boolean isCarryover, boolean isCompositePickupOrder, boolean isDeliveryOrder) {
        if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BYOB_FLOW))) {
            if (!isCarryover && !isCompositePickupOrder && !isDeliveryOrder) {
                verifySavingsAmountText(couponTotal, krogerSavings, ciaoCoupon);
                verifyReducedOrderTotal(totalAmount);
            }
        }
        verifyCheckoutCompleteText();
        verifyCustomerNameText(name);
        tapDoneButton();
    }

    public String getAmountProcessed() {
        mobileCommands.waitForElementVisibility(ciaoCheckoutCompleteMap.amountProcessedText());
        return mobileCommands.getElementText(ciaoCheckoutCompleteMap.amountProcessedText());
    }

    public void selectAcceptSubstitute() {
        if (mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.selectSubstituteAcceptBtn())) {
            mobileCommands.tap(ciaoCheckoutCompleteMap.selectSubstituteAcceptBtn());
        }
    }

    public void selectAcceptMultipleSubstitute(int i) {
        if (mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.selectMultipleSubstituteAcceptBtn(i))) {
            mobileCommands.tap(ciaoCheckoutCompleteMap.selectMultipleSubstituteAcceptBtn(i));
        }
    }

    public boolean isSubsAcceptElementDisplayed(int i) {
        return mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.selectMultipleSubstituteAcceptBtn(i));
    }

    public void selectRejectSubstitute() {
        if (mobileCommands.elementDisplayed(ciaoCheckoutCompleteMap.selectSubstituteRejectBtn())) {
            mobileCommands.tap(ciaoCheckoutCompleteMap.selectSubstituteRejectBtn());
        }
    }
}
