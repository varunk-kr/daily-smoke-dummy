package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeContainerLookUpMap {

    private static HarvesterNativeContainerLookUpMap instance;

    private HarvesterNativeContainerLookUpMap() {
    }

    public synchronized static HarvesterNativeContainerLookUpMap getInstance() {
        if (instance == null) {
            synchronized (HarvesterNativeContainerLookUpMap.class) {
                if (instance == null) {
                    instance = new HarvesterNativeContainerLookUpMap();
                }
            }
        }
        return instance;
    }

    public By selectContainerLookup() {
        return AppiumBy.id("com.kroger.harvester.stage:id/drawer_option_container_lookup");
    }

    public By containerLookUpEnterBarcodeLink() {
        return AppiumBy.id("com.kroger.harvester.stage:id/enterBarcodeManuallyButton");
    }

    public By containerLookUpBarcodeInput() {
        return AppiumBy.id("com.kroger.harvester.stage:id/generic_input");
    }

    public By continueButton() {
        return AppiumBy.xpath("//*[contains(@text,'Continue')]");
    }

    public By containerLookUpMoveItems() {
        return AppiumBy.id("com.kroger.harvester.stage:id/kds_button_text");
    }

    public By moveItems() {
        return AppiumBy.xpath("//*[@content-desc=\"Move Items button\"]");
    }

    public By containerLookUpMoveAllItemsCheckbox() {
        return AppiumBy.id("com.kroger.harvester.stage:id/moveAllItemsCheckbox");
    }

    public By someItemsCheckbox(String Upc) {
        return AppiumBy.xpath("//*[@content-desc=\"Select item " + Upc + "\"]//android.widget.CheckBox");
    }


    public By getUpcOnItemMovement() {
        return AppiumBy.xpath("(//*[contains(@text,'UPC')])[2]");
    }

    public By nextButton() {
        return AppiumBy.accessibilityId("Next button");
    }

    public By toastPopup() {
        return AppiumBy.id("com.kroger.harvester.stage:id/toast_message");
    }

    public By toastMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/validation_text_form_field");
    }

    public By hamburgerMenuIcon() {
        return AppiumBy.accessibilityId("Open navigation drawer");
    }

    public By closeItemMovementPopup() {
        return AppiumBy.accessibilityId("Close button");
    }

    public By containerDetailsHeader() {
        return AppiumBy.xpath("//*[contains(@text,'Container Details')]");
    }
}
