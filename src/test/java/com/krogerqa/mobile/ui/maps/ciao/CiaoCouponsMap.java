package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoCouponsMap {
    private static CiaoCouponsMap instance;

    private CiaoCouponsMap() {
    }

    public synchronized static CiaoCouponsMap getInstance() {
        if (instance == null) {
            synchronized (CiaoCouponsMap.class) {
                if (instance == null) {
                    instance = new CiaoCouponsMap();
                }
            }
        }
        return instance;
    }

    public By addCouponManuallyLink() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Add Manual Adjustments']");
    }

    public By selectDiscountTypeDropMenu() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Select']");
    }

    public By storeCouponOption() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Store Coupon']");
    }

    public By discountValueInput() {
        return AppiumBy.className("android.widget.EditText");
    }

    public By applyButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='APPLY']");
    }

    public By appliedCouponAmountText() {
        return AppiumBy.xpath("//*[contains(@text,'Save')]");
    }

    public By nextButton() {
        return AppiumBy.xpath("//*[@text='Next']");
    }

    public By closeBagFeeOption() {
        return AppiumBy.accessibilityId("Close");
    }

    public By bagFeePopup() {
        return AppiumBy.xpath("//*[@text='Bag Fee']");
    }
}
