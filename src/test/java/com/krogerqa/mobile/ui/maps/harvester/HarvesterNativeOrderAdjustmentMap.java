package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeOrderAdjustmentMap {

    private static HarvesterNativeOrderAdjustmentMap instance;

    private HarvesterNativeOrderAdjustmentMap() {
    }

    public synchronized static HarvesterNativeOrderAdjustmentMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeOrderAdjustmentMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeOrderAdjustmentMap();
            }
        }
        return instance;
    }

    public By orderAdjustmentBtn() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Order Adjustment Nav Card']");
    }

    public By clickOnOrder(String orderId) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"Order #" + orderId + "\"]");
    }

    public By enterBarcodeLink() {
        return AppiumBy.xpath("//*[contains(@content-desc,'Harvester More Options')] | //*[contains(@text,'Barcode')]");
    }

    public By barcodeInput() {
        return AppiumBy.xpath("//*[contains(@text,'insertion at end of text.')] | //android.widget.EditText[@text='Item barcode'] | //android.widget.EditText[@text='Container barcode']");
    }

    public By continueButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Enter barcode manually positive button']| //android.view.View[@content-desc='Right Button in Bottom Sheet']");
    }

    public By itemUpcText() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Item UPC']");
    }

    public By doneStagingButton() {
        return AppiumBy.accessibilityId("Done Staging Button");
    }

    public By orderAdjustmentStagingCompleteText() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='Star Image']//following-sibling::android.widget.TextView");
    }

    public By stepperCount(String upc) {
        return AppiumBy.accessibilityId("Plus one of " + upc);
    }

    public By itemsQtyText() {
        return AppiumBy.id("com.kroger.harvester.stage:id/quantityText");
    }

    public By getStagedLabelStatus() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='Item Image']/../android.view.View[@content-desc]/android.widget.TextView");
    }

    public By kebabMenu() {
        return AppiumBy.xpath("//*[contains(@content-desc,'Harvester More Options')]");
    }
}
