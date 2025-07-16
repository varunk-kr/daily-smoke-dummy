package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoCouponsMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import org.testng.Assert;

public class CiaoCouponsPage {
    static String COUPON_NOT_APPLIED = "Coupon not applied";
    private static CiaoCouponsPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoCouponsMap ciaoCouponsMap = CiaoCouponsMap.getInstance();

    private CiaoCouponsPage() {
    }

    public synchronized static CiaoCouponsPage getInstance() {
        if (instance == null) {
            synchronized (CiaoCouponsPage.class) {
                if (instance == null) {
                    instance = new CiaoCouponsPage();
                }
            }
        }
        return instance;
    }

    public void tapAddCouponManually() {
        mobileCommands.tap(ciaoCouponsMap.addCouponManuallyLink());
    }

    public void tapSelectDiscountTypeDropMenu() {
        mobileCommands.tap(ciaoCouponsMap.selectDiscountTypeDropMenu());
    }

    public void tapStoreCouponOption() {
        mobileCommands.tap(ciaoCouponsMap.storeCouponOption());
    }

    public void enterDiscount(String discount) {
        mobileCommands.enterText(ciaoCouponsMap.discountValueInput(), discount, true);
    }

    public void tapApplyButton() {
        mobileCommands.tap(ciaoCouponsMap.applyButton());
    }

    public void assertCouponApplied(String discount) {
        Assert.assertTrue(mobileCommands.getElementText(ciaoCouponsMap.appliedCouponAmountText()).contains(discount), COUPON_NOT_APPLIED);
    }

    /**
     * Next button is common to Age check, Subs and Coupons screen, hence this method will be reused across all 3 pages
     */
    public void tapNextButton() {
        mobileCommands.tap(ciaoCouponsMap.nextButton());
    }

    public void addCoupon(String discount) {
        if (mobileCommands.elementDisplayed(ciaoCouponsMap.addCouponManuallyLink())) {
            tapAddCouponManually();
            tapSelectDiscountTypeDropMenu();
            tapStoreCouponOption();
            enterDiscount(discount);
            tapApplyButton();
            assertCouponApplied(discount);
            tapNextButton();
        }
    }

    public void closeBagFeeOption() {
        if (mobileCommands.elementDisplayed(ciaoCouponsMap.bagFeePopup())) {
            mobileCommands.waitForElementClickability(ciaoCouponsMap.closeBagFeeOption());
            try {
                mobileCommands.tap(ciaoCouponsMap.closeBagFeeOption());
                if (mobileCommands.elementDisplayed(ciaoCouponsMap.closeBagFeeOption())) {
                    mobileCommands.tap(ciaoCouponsMap.closeBagFeeOption());
                }
            } catch (Exception | AssertionError e) {
                mobileCommands.waitForElementVisibility(ciaoCouponsMap.closeBagFeeOption());
                mobileCommands.waitForElementClickability(ciaoCouponsMap.closeBagFeeOption());
                mobileCommands.tap(ciaoCouponsMap.closeBagFeeOption());
            }
        }
    }
}
